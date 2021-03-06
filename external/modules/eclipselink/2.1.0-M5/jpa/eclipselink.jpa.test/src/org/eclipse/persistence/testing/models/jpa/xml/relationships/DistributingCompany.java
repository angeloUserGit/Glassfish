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
 *     12/2/2009-2.1 Guy Pelletier 
 *       - 296289: Add current annotation metadata support on mapped superclasses to EclipseLink-ORM.XML Schema 
 ******************************************************************************/ 
package org.eclipse.persistence.testing.models.jpa.xml.relationships;

public class DistributingCompany extends Company {
    private Integer distributorId;
    
    public Integer getDistributorId() { 
        return distributorId; 
    }
    
    public void setDistributorId(Integer distributorId) { 
        this.distributorId = distributorId;
    }
}
