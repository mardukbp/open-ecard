/****************************************************************************
 * Copyright (C) 2012 ecsec GmbH.
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

package org.openecard.client.common.tlv.iso7816;

import org.openecard.client.common.tlv.TLV;
import org.openecard.client.common.tlv.TLVException;
import org.openecard.client.common.tlv.Tag;
import org.openecard.client.common.tlv.TagClass;


/**
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public class CardFlags extends TLVBitString {

    private boolean readOnly;
    private boolean authRequired;
    private boolean prnGeneration;

    public CardFlags(TLV tlv) throws TLVException {
	super(tlv, new Tag(TagClass.UNIVERSAL, true, 3).getTagNumWithClass());

	readOnly = isSet(0);
	authRequired = isSet(1);
	prnGeneration = isSet(2);
    }

    public boolean readOnly() {
	return readOnly;
    }

    public boolean authRequired() {
	return authRequired;
    }

    public boolean prnGeneration() {
	return prnGeneration;
    }

}
