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

package org.openecard.client.control.handler;

import java.util.ArrayList;
import java.util.List;


/**
 * Implements a list of ControlHandlers.
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public class ControlHandlers {

    private volatile List<ControlHandler> controlHandlers = new ArrayList<ControlHandler>();

    /**
     * Creates a new list of ControlHandlers.
     *
     * @return List of ControlHandlers.
     */
    public List<ControlHandler> getControlHandlers() {
	return controlHandlers;
    }

    /**
     * Adds a ControlHandler.
     *
     * @param handler Handler.
     */
    public void addControlHandler(ControlHandler handler) {
	controlHandlers.add(handler);
    }

    /**
     * Removes the ControlHandler.
     *
     * @param handler Handler
     */
    public void removeControlHandler(ControlHandler handler) {
	controlHandlers.remove(handler);
    }

}