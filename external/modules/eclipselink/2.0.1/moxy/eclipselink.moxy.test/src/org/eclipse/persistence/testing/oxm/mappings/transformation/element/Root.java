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
*     bdoughan - Feb 23/2009 - 2.0 - Initial implementation
******************************************************************************/
package org.eclipse.persistence.testing.oxm.mappings.transformation.element;

public class Root {

    private String element;

    public Root() {
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public boolean equals(Object object) {
        try {
            Root root = (Root) object;
            if(null == root.getElement()) {
                return null == element;
            } else {
                return root.getElement().equals(element);
            }
        } catch(ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        return "Root(element=" + element + ")";
    }
}
