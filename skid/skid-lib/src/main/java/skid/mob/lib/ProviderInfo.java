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
public interface ProviderInfo {

    String displayName();
    String displayName(String lang);

    String informationUrl();
    String informationUrl(String lang);

    String privacyStatementUrl();
    String privacyStatementUrl(String lang);

    LogoUrl logoUrl();
    LogoUrl logoUrl(String lang);

}