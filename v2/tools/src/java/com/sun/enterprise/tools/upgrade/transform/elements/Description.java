/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/*
 * Element.java
 *
 * Created on August 4, 2003, 2:04 PM
 */

package com.sun.enterprise.tools.upgrade.transform.elements;

/**
 *
 * @author  prakash
 */
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import com.sun.enterprise.tools.upgrade.transform.ElementToObjectMapper;

public class Description extends BaseElement {
    
    /** Creates a new instance of Element */
    public Description() {
    }
    /**
     * element - description
     * parentSource - parent of description
     * parentResult - result element to which description to be updated
     */
    public void transform(Element element, Element parentSource, Element parentResult){
        // description is updated if exists, else added if not exists.
        NodeList resultDescs = parentResult.getElementsByTagName("description");
        Element resultDesc = null;
        if(resultDescs.getLength() == 0){
            resultDesc = parentResult.getOwnerDocument().createElement("description");
            Node textNode = parentResult.getOwnerDocument().createTextNode(this.getTextNodeData(element));
            resultDesc.appendChild(textNode);
            parentResult.appendChild(resultDesc);
        }else {
            resultDesc = (Element)resultDescs.item(0);
            if((this.getTextNodeData(element)).equals(this.getTextNodeData((Element)resultDesc))){
                // If both are same there is nothing to be done, just break.
            }else if(this.getTextNode(resultDesc) == null){
                Node textNode = parentResult.getOwnerDocument().createTextNode(this.getTextNodeData(element));
                resultDesc.appendChild(textNode);
            }else{
                this.setTextNodeData(resultDesc, this.getTextNodeData(element));
            }
        }                
    }
    private String getTextNodeData(Element element){
        NodeList children = element.getChildNodes();
        for(int index=0; index < children.getLength(); index++){
            if(children.item(index).getNodeType() == Node.TEXT_NODE){
                return children.item(index).getNodeValue();
            }
        }
        return "";
    }
    private Node getTextNode(Element element){
        NodeList children = element.getChildNodes();
        for(int index=0; index < children.getLength(); index++){
            if(children.item(index).getNodeType() == Node.TEXT_NODE){
                return children.item(index);
            }
        }
        return null;
    }
    private void setTextNodeData(Element element, String textNodeData){
        NodeList children = element.getChildNodes();
        for(int index=0; index < children.getLength(); index++){
            if(children.item(index).getNodeType() == Node.TEXT_NODE){
                children.item(index).setNodeValue(textNodeData);
                return;
            }
        }        
    }
    
}
