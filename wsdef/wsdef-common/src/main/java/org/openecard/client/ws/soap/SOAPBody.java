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

package org.openecard.client.ws.soap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public class SOAPBody extends SOAPElement {

    protected SOAPBody(Element element) {
	super(element);
    }


    public void addDocument(Document document) throws SOAPException {
	Document doc = element.getOwnerDocument();
	Node newNode = doc.importNode(document.getDocumentElement(), true);
	element.appendChild(newNode);
    }

}
