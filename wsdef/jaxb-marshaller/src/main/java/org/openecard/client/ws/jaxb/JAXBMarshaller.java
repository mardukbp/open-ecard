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

package org.openecard.client.ws.jaxb;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.openecard.client.ws.MarshallingTypeException;
import org.openecard.client.ws.WSMarshaller;
import org.openecard.client.ws.WSMarshallerException;
import org.openecard.client.ws.WhitespaceFilter;
import org.openecard.client.ws.soap.MessageFactory;
import org.openecard.client.ws.soap.SOAPBody;
import org.openecard.client.ws.soap.SOAPException;
import org.openecard.client.ws.soap.SOAPMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public final class JAXBMarshaller implements WSMarshaller {

    private final JAXBContext jaxbCtx;
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    // w3 factory
    private final DocumentBuilderFactory w3Factory;
    private final DocumentBuilder w3Builder;
    private final Transformer serializer;

    // soap
    private final MessageFactory soapFactory;

    public JAXBMarshaller() {
	this(new Class[0]);
    }
    public JAXBMarshaller(Class... additionalClasses) {
	JAXBContext tmpJaxbCtx = null;
	Marshaller tmpMarshaller = null;
	Unmarshaller tmpUnmarshaller = null;
	DocumentBuilderFactory tmpW3Factory = null;
	DocumentBuilder tmpW3Builder = null;
	Transformer tmpSerializer = null;
	MessageFactory tmpSoapFactory = null;

	try {
	    // prepare marshaller
	    Class[] jaxbClasses = getJaxbClasses();
	    LinkedList<Class> allClassesList = new LinkedList<Class>();
	    allClassesList.addAll(Arrays.asList(jaxbClasses));       // add predefined classes
	    allClassesList.addAll(Arrays.asList(additionalClasses)); // add additional classes from constructor call
	    tmpJaxbCtx = JAXBContext.newInstance(allClassesList.toArray(new Class[0]));

	    // prepare xml parser
	    // create marshaller and unmarshaller
	    tmpMarshaller = tmpJaxbCtx.createMarshaller();
	    tmpUnmarshaller = tmpJaxbCtx.createUnmarshaller();

	    // instantiate w3 stuff
	    tmpW3Factory = DocumentBuilderFactory.newInstance();
	    tmpW3Factory.setNamespaceAware(true);
	    tmpW3Factory.setIgnoringComments(true);
	    tmpW3Builder = tmpW3Factory.newDocumentBuilder();

	    TransformerFactory tfactory = TransformerFactory.newInstance();
	    tmpSerializer = tfactory.newTransformer();
	    tmpSerializer.setOutputProperty(OutputKeys.INDENT, "yes");
	    tmpSerializer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            tmpSerializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    tmpSerializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

	    // instantiate soap stuff
	    tmpSoapFactory = MessageFactory.newInstance();
	} catch (Exception ex) {
	    ex.printStackTrace(System.err);
	    System.exit(1); // non recoverable
	}

	jaxbCtx = tmpJaxbCtx;
	marshaller = tmpMarshaller;
	unmarshaller = tmpUnmarshaller;
	w3Factory = tmpW3Factory;
	w3Builder = tmpW3Builder;
	serializer = tmpSerializer;
	soapFactory = tmpSoapFactory;
    }

    private static Class[] getJaxbClasses() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
	List<Class> classes = new LinkedList<Class>();
        InputStream classListStream = cl.getResourceAsStream("classes.lst");
        InputStream classListStreamC = cl.getResourceAsStream("/classes.lst");

        if (classListStream == null && classListStreamC == null) {
            System.err.println("Error loading classes.lst");
        } else {
            // select the one stream that is set
            classListStream = (classListStream != null) ? classListStream : classListStreamC;

            try {
                LineNumberReader r = new LineNumberReader(new InputStreamReader(classListStream));
                String next;
                // read all entries from file
                while ((next = r.readLine()) != null) {
                    try {
                        // load class and see if it is a JAXB class
                        Class c = cl.loadClass(next);
                        if (c.getAnnotation(XmlType.class) != null) {
                            classes.add(c);
                        }
                    } catch (ClassNotFoundException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

	return classes.toArray(new Class[classes.size()]);
    }


    ////////////////////////////////////////////////////////////////////////////
    // public functions to marshal and convert stuff
    ////////////////////////////////////////////////////////////////////////////


    public synchronized MessageFactory getSoapFactory() {
	return soapFactory;
    }

    @Override
    public synchronized Document str2doc(String docStr) throws SAXException {
        try {
            // read dom as w3
            StringReader strReader = new StringReader(docStr);
            InputSource inSrc = new InputSource(strReader);
            Document doc = w3Builder.parse(inSrc);

            WhitespaceFilter.filter(doc);

            return doc;
        } catch (IOException ex) {
            throw new SAXException(ex);
        }
    }

    @Override
    public synchronized Document str2doc(InputStream docStr) throws SAXException, IOException {
	// read dom as w3
	Document doc = w3Builder.parse(docStr);

	WhitespaceFilter.filter(doc);

	return doc;
    }

    @Override
    public synchronized String doc2str(Node doc) throws TransformerException {
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	serializer.transform(new DOMSource(doc), new StreamResult(out));
	String result;
        try {
            result = out.toString("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new TransformerException(ex);
        }
        return result;
    }

    @Override
    public synchronized Object unmarshal(Node n) throws MarshallingTypeException, WSMarshallerException {
	Document newDoc = null;
	if (n instanceof Document) {
	    newDoc = (Document) n;
	} else if(n instanceof Element) {
	    newDoc = w3Builder.newDocument();
	    Node root = newDoc.importNode(n, true);
	    newDoc.appendChild(root);
	} else {
	    throw new WSMarshallerException("Only w3c Document and Element are accepted.");
	}

	Object result;
        try {
            result = unmarshaller.unmarshal(newDoc); //NOI18N
        } catch (JAXBException ex) {
            throw new MarshallingTypeException(ex);
        }
	return result;
    }

    @Override
    public synchronized Document marshal(Object o) throws MarshallingTypeException {
	Document doc = w3Builder.newDocument();
        try {
            marshaller.marshal(o, doc);
        } catch (JAXBException ex) {
            throw new MarshallingTypeException(ex);
        }
	return doc;
    }

    @Override
    public synchronized SOAPMessage doc2soap(Document envDoc) throws SOAPException {
	SOAPMessage msg = soapFactory.createMessage(envDoc);
	return msg;
    }

    @Override
    public synchronized SOAPMessage add2soap(Document content) throws SOAPException {
	SOAPMessage msg = soapFactory.createMessage();
	SOAPBody body = msg.getSOAPBody();
	body.addDocument(content);

	return msg;
    }

}
