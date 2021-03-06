/*******************************************************************************
 * Copyright (c) 1998, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Matt MacIvor - January 18/2010 - 2.0 - Initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.oxm.mappings.binarydata;

import javax.activation.DataHandler;

import org.eclipse.persistence.oxm.XMLMarshaller;
import org.eclipse.persistence.oxm.mappings.XMLBinaryDataMapping;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.testing.oxm.mappings.XMLMappingTestCases;
import org.eclipse.persistence.testing.oxm.mappings.binarydatacollection.MyAttachmentMarshaller;
import org.eclipse.persistence.testing.oxm.mappings.binarydatacollection.MyAttachmentUnmarshaller;
import org.w3c.dom.Document;

public class BinaryDataCompositeSelfTestCases extends XMLMappingTestCases{
    private final static String XML_RESOURCE = "org/eclipse/persistence/testing/oxm/mappings/binarydata/BinaryDataCompositeSelf.xml";
    private MyAttachmentMarshaller attachmentMarshaller;
    
    public BinaryDataCompositeSelfTestCases(String name) throws Exception {
        super(name);
        setControlDocument(XML_RESOURCE);
        Project p = new BinaryDataCompositeSelfProject();
        setProject(p);
    }
    
    public void setUp() throws Exception {
    	super.setUp();
    	
    	xmlUnmarshaller.setAttachmentUnmarshaller(new MyAttachmentUnmarshaller());
    	
    	byte[] bytes = new byte[] {1, 2, 3, 4, 5, 6};    	
    	MyAttachmentMarshaller.attachments.put(MyAttachmentUnmarshaller.ATTACHMENT_TEST_ID, bytes);
    	
    }

    @Override
    protected XMLMarshaller createMarshaller() {
        XMLMarshaller marshaller = super.createMarshaller();
        this.attachmentMarshaller = new MyAttachmentMarshaller();
        marshaller.setAttachmentMarshaller(this.attachmentMarshaller);
        return marshaller;
    }

    protected Object getControlObject() {
        Employee emp = new Employee(123);
        
        MyImage image = new MyImage();
        image.setMyBytes(new byte[]{1, 2, 3, 4, 5, 6});
        emp.setMyImage(image);
        return emp;
    }
    
    public Object getReadControlObject() {
        Employee emp = new Employee(123);
        
        MyImage image = new MyImage();
        image.setMyBytes(new byte[]{1, 2, 3, 4, 5, 6});
        emp.setMyImage(image);
        
        return emp;
    }
    
    public void objectToXMLDocumentTest(Document testDocument) throws Exception {
        super.objectToXMLDocumentTest(testDocument);
        assertNotNull(this.attachmentMarshaller.getLocalName());
    }
}
