/****************************************************************************
 * Copyright (C) 2020 ecsec GmbH.
 * All rights reserved.
 * Contact: ecsec GmbH (info@ecsec.de)
 *
 * This file may be used in accordance with the terms and conditions
 * contained in a signed written agreement between you and ecsec GmbH.
 *
 ***************************************************************************/

package skid.mob.lib;

import org.openecard.mobile.activation.ActivationResultCode;
import org.openecard.robovm.annotations.FrameworkInterface;

/**
 *
 * @author Tobias Wich
 */
@FrameworkInterface
public interface EacResult {

    String getRedirectUrl();

    ActivationResultCode getResultCode();

    String getErrorMessage();

    /**
     * If present, represents the minor error code of the error leading to the termination of the process.
     *
     * @see ECardConstants.Minor;
     * @return The minor result code or {@code null} if not present.
     */
    String getProcessResultMinor();

}
