/****************************************************************************
 * Copyright (C) 2018 ecsec GmbH.
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

package org.openecard.richclient.gui.manage.core;

import java.io.IOException;
import javax.swing.JCheckBox;
import javax.xml.bind.JAXBException;
import org.openecard.common.I18n;
import org.openecard.mdlw.sal.config.MiddlewareConfigLoader;
import org.openecard.mdlw.sal.config.MiddlewareSALConfig;
import org.slf4j.LoggerFactory;


/**
 * Custom settings group for Middleware selection
 *
 * @author Sebastian Schuberth
 */
public class MiddlewareSelectionGroup extends OpenecardPropertiesSettingsGroup {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MiddlewareSelectionGroup.class);
    private static final I18n LANG = I18n.getTranslation("addon");

    public MiddlewareSelectionGroup() {
	super(LANG.translationForKey("addon.core.middleware.group_name"));
	addMiddlewares();
    }

    private void addMiddlewares() {
	try {
	    MiddlewareConfigLoader mwConfigLoader = new MiddlewareConfigLoader();
	    mwConfigLoader.getMiddlewareSALConfigs().forEach((cfg) -> {
		addMiddleware(cfg);
	    });
	} catch (IOException | JAXBException ex) {
	    LOG.error("Could not read middleware config.", ex);
	}
    }

    private void addMiddleware(MiddlewareSALConfig mwSALConfig) {
	if (! mwSALConfig.getMwSpec().isDisabled()) {
	    String mwName = mwSALConfig.getMiddlewareName();
	    String label = LANG.translationForKey("addon.core.middleware.item.label", mwName);
	    String prop = mwName + ".enabled";

	    JCheckBox box;
	    if (mwSALConfig.isSALRequired()) {
		String desc = LANG.translationForKey("addon.core.middleware.item.desc");
		box = addBoolItem(label, desc, prop);
	    } else {
		String desc = LANG.translationForKey("addon.core.middleware.item.desc");
		box = addBoolItem(label, desc, prop);
	    }
	    box.setEnabled(! mwSALConfig.isDisabled());
	}
    }

}
