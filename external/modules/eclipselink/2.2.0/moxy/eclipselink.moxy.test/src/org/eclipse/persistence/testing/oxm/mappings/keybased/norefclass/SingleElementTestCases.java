/*******************************************************************************
* Copyright (c) 2010 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
* which accompanies this distribution.
* The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
* and the Eclipse Distribution License is available at
* http://www.eclipse.org/org/documents/edl-v10.php.
*
* Contributors:
*     bdoughan - April 14/2010 - 2.1 - Initial implementation
******************************************************************************/
package org.eclipse.persistence.testing.oxm.mappings.keybased.norefclass;

import org.eclipse.persistence.testing.oxm.xmlmarshaller.setschemas.SetXmlSchemaTestCases;

public class SingleElementTestCases extends AbstractTestCases {

    private static String XML_RESOURCE = "org/eclipse/persistence/testing/oxm/mappings/keybased/norefclass/singleelement.xml"; 

    public SingleElementTestCases(String name) throws Exception {
        super(name);
        setProject(new SingleElementProject());
        setControlDocument(XML_RESOURCE);
    }

}