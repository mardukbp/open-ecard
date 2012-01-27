/*
 * Copyright 2012 Tobias Wich ecsec GmbH
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

package org.openecard.client.ifd.scio;

import oasis.names.tc.dss._1_0.core.schema.Result;
import org.openecard.client.common.ECardException;


/**
 * Exception class for IFD layer.
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public class IFDException extends ECardException {

    public IFDException(String msg) {
	makeException(this, msg);
    }

    public IFDException(String minor, String msg) {
	makeException(this, minor, msg);
    }

    public IFDException(Result r) {
	makeException(this, r);
    }

    public IFDException(Throwable cause) {
	makeException(this, cause);
    }

}
