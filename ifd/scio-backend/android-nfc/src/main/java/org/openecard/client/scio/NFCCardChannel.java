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
import java.nio.ByteBuffer;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

/**
 * NFC implementation of smartcardio's cardChannel interface.
 * 
 * @author Dirk Petrautzki <petrautzki@hs-coburg.de>
 * 
 */
public class NFCCardChannel extends CardChannel {

    private NFCCard card;

    public NFCCardChannel(NFCCard card) {
	this.card = card;
    }

    @Override
    public void close() throws CardException {
	/*
	 * we only have one channel and this will be open as long as we are
	 * connected to the tag
	 */
    }

    @Override
    public Card getCard() {
	return this.card;
    }

    @Override
    public int getChannelNumber() {
	return 0;
    }

    @Override
    public ResponseAPDU transmit(CommandAPDU arg0) throws CardException {

	try {
	    return new ResponseAPDU(this.card.isodep.transceive(arg0.getBytes()));
	} catch (IOException e) {
	    throw new CardException("Transmit failed", e);
	}

    }

    @Override
    public int transmit(ByteBuffer arg0, ByteBuffer arg1) throws CardException {
	throw new CardException("not yet  implemented");
    }

}
