/* Copyright 2012, Hochschule fuer angewandte Wissenschaften Coburg 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openecard.client.scio;

import java.io.IOException;

import android.nfc.tech.IsoDep;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;

/**
 * NFC implementation of smartcardio's Card interface.
 * 
 * @author Dirk Petrautzki <petrautzki@hs-coburg.de>
 * 
 */
public class NFCCard extends Card {

    protected IsoDep isodep = null;
    private NFCCardChannel nfcCardChannel = new NFCCardChannel(this);

    public NFCCard(IsoDep tag) {
	this.isodep = tag;
    }

    @Override
    public void beginExclusive() throws CardException {
	// TODO
    }

    @Override
    public void disconnect(boolean arg0) throws CardException {
	try {
	    this.isodep.close();
	} catch (IOException e) {
	    throw new CardException("Disconnect failed", e);
	}
	return;
    }

    @Override
    public void endExclusive() throws CardException {
	// TODO
    }

    @Override
    public ATR getATR() {
	/* for now theres no way to get the ATR in android nfc api */

	return new ATR(new byte[0]);
    }

    @Override
    public CardChannel getBasicChannel() {
	return this.nfcCardChannel;
    }

    @Override
    public String getProtocol() {
	/* for now theres no way to get the used protocol in android nfc api */
	return "";
    }

    @Override
    public CardChannel openLogicalChannel() throws CardException {
	return this.nfcCardChannel;
    }

    @Override
    public byte[] transmitControlCommand(int arg0, byte[] arg1) throws CardException {
	return new byte[0];
    }

}
