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

import org.openecard.robovm.annotations.FrameworkInterface;

/**
 *
 * @author Tobias Wich
 */
@FrameworkInterface
public interface EacModule {

    Cancellable runEac(SkidEacInteraction interactionComponent);

}