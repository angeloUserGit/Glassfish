/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the "License").  You may not use this file except 
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt or 
 * https://glassfish.dev.java.net/public/CDDLv1.0.html. 
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * HEADER in each file and include the License file at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt.  If applicable, 
 * add the following below this CDDL HEADER, with the 
 * fields enclosed by brackets "[]" replaced with your 
 * own identifying information: Portions Copyright [yyyy] 
 * [name of copyright owner]
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-1973 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.04.20 at 08:27:00 IST 
//


package com.sun.persistence.api.deployment;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.sun.persistence.api.deployment.DescriptorNode;
import com.sun.persistence.api.deployment.GeneratorType;

@XmlAccessorType(value = AccessType.FIELD)
@XmlType(name = "id", namespace = "http://java.sun.com/xml/ns/persistence_ORM")
public class IdDescriptor
    extends DescriptorNode
{

    @XmlElement(defaultValue = "NONE", name = "generate", namespace = "http://java.sun.com/xml/ns/persistence_ORM", type = GeneratorType.class)
    protected GeneratorType generate;
    @XmlElement(defaultValue = "", name = "generator", namespace = "http://java.sun.com/xml/ns/persistence_ORM", type = String.class)
    protected String generator;

    /**
     * Gets the value of the generate property.
     * 
     * @return
     *     possible object is
     *     {@link com.sun.persistence.api.deployment.GeneratorType}
     */
    public GeneratorType getGenerate() {
        return generate;
    }

    /**
     * Sets the value of the generate property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.sun.persistence.api.deployment.GeneratorType}
     */
    public void setGenerate(GeneratorType value) {
        this.generate = value;
    }

    public boolean isSetGenerate() {
        return (this.generate!= null);
    }

    public void unsetGenerate() {
        this.generate = null;
    }

    /**
     * Gets the value of the generator property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getGenerator() {
        return generator;
    }

    /**
     * Sets the value of the generator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setGenerator(String value) {
        this.generator = value;
    }

    public boolean isSetGenerator() {
        return (this.generator!= null);
    }

    public void unsetGenerator() {
        this.generator = null;
    }

}
