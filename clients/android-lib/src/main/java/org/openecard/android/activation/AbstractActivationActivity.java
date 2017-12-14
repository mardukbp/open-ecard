/****************************************************************************
 * Copyright (C) 2017 ecsec GmbH.
 * All rights reserved.
 * Contact: ecsec GmbH (info@ecsec.de)
 *
 * This file is part of the Open eCard App.
 *
 * GNU General Public License Usage
 * This file may be used under the terms of the GNU General Public
 * License version 3.0 as published by the Free Software Foundation
 * and appearing in the file LICENSE.GPL included in the packaging of
 * this file. Please review the following information to ensure the
 * GNU General Public License version 3.0 requirements will be met:
 * http://www.gnu.org/copyleft/gpl.html.
 *
 * Other Usage
 * Alternatively, this file may be used in accordance with the terms
 * and conditions contained in a signed written agreement between
 * you and ecsec GmbH.
 *
 ***************************************************************************/

package org.openecard.android.activation;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import org.openecard.android.system.ServiceResponse;
import org.openecard.android.system.OpeneCardContext;
import org.openecard.android.ex.ApduExtLengthNotSupported;
import org.openecard.android.system.OpeneCardServiceClient;
import static org.openecard.android.system.ServiceResponseLevel.INFO;
import org.openecard.android.utils.NfcUtils;
import org.openecard.common.event.EventObject;
import org.openecard.common.event.EventType;
import org.openecard.common.interfaces.EventCallback;
import org.openecard.gui.android.EacNavigatorFactory;
import org.openecard.gui.android.eac.EacGui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class provides the basic eID activation functionality as specified in BSI TR-03124-1.
 * <p>It takes care of performing the Intent handling, initializing the Open eCard Stack (Service) and provides
 * the EacGui interface which is needed to implement the UI.</p>
 * <p>An example implementation can be found in the CustomActivationActivity in
 * <a href="https://github.com/ecsec/open-ecard-android">ecsec/open-ecard-android</a>).</p>
 *
 * @author Mike Prechtl
 * @author Tobias Wich
 */
public abstract class AbstractActivationActivity extends Activity implements ActivationImplementationInterface {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractActivationActivity.class);

    private Dialog cardRemoveDialog;

    private Thread authThread;
    private boolean cardPresent;
    private EacGui eacGui;

    private OpeneCardContext octx;
    private Class<?> returnClass;

    @Override
    protected synchronized void onResume() {
	super.onResume();

	// enable dispatch with nfc tag
	NfcUtils.getInstance().enableNFCDispatch(this);
    }

    @Override
    protected synchronized void onPause() {
	super.onPause();

	try {
	    // disable dispatch with nfc tag
	    NfcUtils.getInstance().disableNFCDispatch(this);
	} catch (Exception e) {
	    LOG.info(e.getMessage(), e);
	}
    }

    @Override
    protected void onStart() {
	super.onStart();

	final OpeneCardServiceClient client = new OpeneCardServiceClient(this);
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		ServiceResponse r = client.startService();
		switch (r.getResponseLevel()) {
		    case INFO:
			onOecInitSuccess(client.getContext());
			break;
		    default:
			onAuthenticationFailure(new ActivationResult(ActivationResultCode.INTERNAL_ERROR, r.getMessage()));
		}
	    }
	}, "Oec Service Initializer").start();
    }

    private void onOecInitSuccess(OpeneCardContext ctx) {
	this.octx = ctx;
	final ActivationController ac = new ActivationController(octx);

	// add callback to this abstract activity when card is removed
	octx.getEventDispatcher().add(insertionHandler, EventType.CARD_REMOVED, EventType.CARD_INSERTED);
	octx.getEventDispatcher().add(cardDetectHandler, EventType.CARD_RECOGNIZED);

	Intent actIntent = getIntent();
	Uri data = actIntent.getData();
	returnClass = forClassName(actIntent.getStringExtra(RETURN_CLASS));
	final String eIDUrl = data.toString();

	if (eIDUrl != null) {
	    waitForEacGui();
	    // startService TR procedure according to [BSI-TR-03124-1]
	    authThread = new Thread(new Runnable() {
		@Override
		public void run() {
		    ActivationResult result = ac.activate(eIDUrl);
		    handleActivationResult(result);
		}
	    }, "OeC Activation Process");
	    authThread.start();
	    // when app is closed or minimized the authentication process is interrupted and have to startService again
	} else {
	    handleActivationResult(new ActivationResult(ActivationResultCode.INTERNAL_ERROR,
		    "Authentication process already finished."));
	}
    }

    private Class<?> forClassName(@Nullable String className) {
	if (className != null) {
	    try {
		return getClassLoader().loadClass(className);
	    } catch (ClassNotFoundException ex) {
		LOG.error("Invalid return class named in activation intent.", ex);
	    }
	}

	return null;
    }


    @Override
    protected void onNewIntent(Intent intent) {
	super.onNewIntent(intent);
	checkNfcTag(intent);
    }

    private void checkNfcTag(Intent intent) {
	if (this.octx != null) {
	    try {
		// extract nfc tag
		NfcUtils.getInstance().retrievedNFCTag(intent);
	    } catch (ApduExtLengthNotSupported ex) {
		LOG.error(ex.getMessage());
	    }
	}
    }


    @Override
    protected void onStop() {
	super.onStop();

	// remove callback which is set onStart
	if (octx != null) {
	    octx.getEventDispatcher().del(insertionHandler);
	}
	returnClass = null;
	octx = null;
    }

    private final EventCallback insertionHandler = new EventCallback() {
	@Override
	public void signalEvent(EventType eventType, EventObject eventData) {
	    switch (eventType) {
		case CARD_REMOVED:
		    cardPresent = false;
		    Dialog d = cardRemoveDialog;
		    if (d != null && d.isShowing()) {
			d.dismiss();
		    }
		    break;
		case CARD_INSERTED:
		    cardPresent = true;
		default:
		    LOG.debug("Received an unsupported Event: " + eventType.name());
		    break;
	    }
	}
    };

    private final EventCallback cardDetectHandler = new EventCallback() {
	@Override
	public void signalEvent(EventType eventType, EventObject eventData) {
	    switch (eventType) {
		case CARD_RECOGNIZED:
		    Set<String> supportedCards = getSupportedCards();
		    final String type = eventData.getHandle().getRecognitionInfo().getCardType();
		    if (supportedCards == null || supportedCards.contains(type)) {
			AbstractActivationActivity.this.runOnUiThread(new Runnable() {
			    @Override
			    public void run() {
				onCardInserted(type);
			    }
			});

			// remove handler when the correct card is present
			if (octx != null) {
			    octx.getEventDispatcher().del(this);
			}
		    }
		    break;
		default:
		    LOG.debug("Received an unsupported Event: " + eventType.name());
		    break;
	    }
	}
    };

    private synchronized void handleActivationResult(final ActivationResult result) {
	// only this first invocation must be processed, in order to prevent double finish when cancelling the auth job
	if (authThread == null) {
	    return;
	} else {
	    authThread = null;
	}

	switch (result.getResultCode()) {
	    case REDIRECT:
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			onAuthenticationSuccess(result);
		    }
		});
		break;
	    default:
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			onAuthenticationFailure(result);
		    }
		});
		break;
	}
    }

    /**
     * This method starts a thread which is waiting for the Eac Gui. If the Eac Gui is
     *
     */
    private void waitForEacGui() {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		EacNavigatorFactory eacNavFactory = octx.getEacNavigatorFactory();
		try {
		    eacGui = eacNavFactory.getIfacePromise().deref();
		    onEacIfaceSet(eacGui);
		} catch (InterruptedException ex) {
		    LOG.error("Waiting for Eac Gui was interrupted.", ex);
		}
	    }
	}, "WaitForEacGuiThread").start();
    }

    private static final Set<String> SUPPORTED_CARDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
	    "http://bsi.bund.de/cif/npa.xml"
    )));
    @Override
    public Set<String> getSupportedCards() {
	return SUPPORTED_CARDS;
    }

    @Override
    public void onCardInserted(String cardType) {
	// default implementation does nothing
	LOG.info("Card recognized event received in activity: cardType={}", cardType);
    }

    @Override
    public void cancelAuthentication() {
	Thread at = authThread;
	if (at != null) {
	    try {
		LOG.info("Stopping Authentication thread ...");
		at.interrupt();
		at.join();
		LOG.info("Authentication thread has stopped.");

		// cancel task and handle event
		String msg = "";
		ActivationResult r = new ActivationResult(ActivationResultCode.INTERRUPTED, msg);
		handleActivationResult(r);
	    } catch (InterruptedException ex) {
		LOG.error("Waiting for Authentication thread interrupted.");
	    }
	}
    }


    @Override
    public void onAuthenticationSuccess(final ActivationResult result) {
	// show card remove dialog before the redirect occurs
	final String location = result.getRedirectUrl();

	// only display if a card is available
	if (cardPresent) {
	    Dialog d = showCardRemoveDialog();
	    if (d != null) {
		cardRemoveDialog = d;
		d.setCanceledOnTouchOutside(false);
		d.setCancelable(false);
		// if card remove dialog is not shown, then show it
		if (! d.isShowing()) {
		    d.show();
		}
		// redirect to the termination uri when the card remove dialog is closed
		d.setOnDismissListener(new DialogInterface.OnDismissListener() {
		    @Override
		    public void onDismiss(DialogInterface dialog) {
			// clean dialog field
			cardRemoveDialog = null;
			// perform redirect
			if (location != null) {
			    // redirect to result location
			    startActivity(createRedirectIntent(location));
			}
		    }
		});

		// return as the redirect is handled in the DismissListener
		return;
	    }
	}

	// no dialog shown, just redirect
	if (location != null) {
	    // redirect to result location
	    startActivity(createRedirectIntent(location));
	}
    }

    private Intent createRedirectIntent(String location) {
	Intent i;
	Uri redirectUri = Uri.parse(location);
	if (returnClass != null) {
	    i = new Intent(Intent.ACTION_VIEW, redirectUri, this, returnClass);
	} else {
	    i = new Intent(Intent.ACTION_VIEW, redirectUri);
	}
	return i;
    }

}
