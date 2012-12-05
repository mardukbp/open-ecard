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

package org.openecard.gui.definition;

import org.openecard.common.util.ValueGenerators;


/**
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public abstract class IDTrait implements InfoUnit {

    private String id;

    public IDTrait() {
	this(ValueGenerators.generateSessionID());
    }

    public IDTrait(String id) {
	this.id = id;
    }


    @Override
    public final String getID() {
	return id;
    }
    @Override
    public final void setID(String id) {
	this.id = id;
    }

}