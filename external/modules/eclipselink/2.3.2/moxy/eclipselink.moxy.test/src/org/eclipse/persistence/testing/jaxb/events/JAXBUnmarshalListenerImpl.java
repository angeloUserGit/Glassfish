/*******************************************************************************
 * Copyright (c) 1998, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package org.eclipse.persistence.testing.jaxb.events;

import java.util.ArrayList;

import javax.xml.bind.Unmarshaller;

public class JAXBUnmarshalListenerImpl extends Unmarshaller.Listener {
    static Integer EMPLOYEE_BEFORE_UNMARSHAL = new Integer(0);
    static Integer ADDRESS_BEFORE_UNMARSHAL = new Integer(1);
    static Integer PHONE_BEFORE_UNMARSHAL = new Integer(2);
    static Integer EMPLOYEE_AFTER_UNMARSHAL = new Integer(3);
    static Integer ADDRESS_AFTER_UNMARSHAL = new Integer(4);
    static Integer PHONE_AFTER_UNMARSHAL = new Integer(5);
    
    public ArrayList events = null;
    
    public JAXBUnmarshalListenerImpl() {
        events = new ArrayList();
    }
    public void beforeUnmarshal(Object obj, Object parent) {
        if(obj instanceof Employee) {
            events.add(EMPLOYEE_BEFORE_UNMARSHAL);
        } else if(obj instanceof Address) {
            events.add(ADDRESS_BEFORE_UNMARSHAL);
        } else if(obj instanceof PhoneNumber) {
            events.add(PHONE_BEFORE_UNMARSHAL);
        }
    }
    public void afterUnmarshal(Object obj, Object parent) {
        if(obj instanceof Employee) {
            events.add(EMPLOYEE_AFTER_UNMARSHAL);
        } else if(obj instanceof Address) {
            events.add(ADDRESS_AFTER_UNMARSHAL);
        } else if(obj instanceof PhoneNumber) {
            events.add(PHONE_AFTER_UNMARSHAL);
        }
    }
}
