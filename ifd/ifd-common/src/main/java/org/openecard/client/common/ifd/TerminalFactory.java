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

package org.openecard.client.common.ifd;

import javax.smartcardio.CardTerminals;


/**
 * TerminalFactory interface similar to javax.smartcardio.TerminalFactory, but without
 * the static factory elements which are not present in systems like Android.<br/>
 * The ecsec IFD contains a generic loader class which takes a class name from a config file
 * and executes a method with the following signature:<br/>
 * <code>public static TerminalFactory getInstance();</code>
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public interface TerminalFactory {

    /**
     * Returns the type of this TerminalFactory. Examples would be PC/SC or AndroidNFC.
     *
     * @return the type of this TerminalFactory
     */
    public String getType();

    /**
     * Returns a new CardTerminals object encapsulating the terminals
     * supported by this factory.
     * See the class comment of the {@linkplain CardTerminals} class
     * regarding how the returned objects can be shared and reused.
     *
     * @return a new CardTerminals object encapsulating the terminals
     * supported by this factory.
     */
    public CardTerminals terminals();

}
