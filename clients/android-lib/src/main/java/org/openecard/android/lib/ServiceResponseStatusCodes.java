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

package org.openecard.android.lib;


/**
 * This class contains status codes which are used in service responses.
 *
 * @author Mike Prechtl
 */
public interface ServiceResponseStatusCodes {

    /**
     * indicates that nfc is not available on the corresponding device.
     */
    int NFC_NOT_AVAILABLE = 100;

    /**
     * indicates that nfc is not enabled, please move to the device settings.
     */
    int NFC_NOT_ENABLED = 101;

    /**
     * indicates that the initialization was successfully finished.
     */
    int INIT_SUCCESS = 200;

    /**
     * indicates that the app was successfully terminated.
     */
    int SHUTDOWN_SUCCESS = 200;

    /**
     * indicates that eac service started and is binded.
     */
    int EAC_SERVICE_CONNECTED = 201;

    /**
     * indicates that eac service is unbinded.
     */
    int EAC_SERVICE_DISCONNECTED = 203;

    /**
     * indicates that the corresponding device does not support the required API level.
     */
    int NOT_REQUIRED_API_LEVEL = 102;

    /**
     * indicates other internal errors.
     */
    int INTERNAL_ERROR = 500;


    /**
     * indicates that the shutdown of the app failed.
     */
    int SHUTDOWN_FAILED = 501;

}
