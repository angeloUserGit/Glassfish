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
 *     Gordon Yorke - initial development
 ******************************************************************************/
package org.eclipse.persistence.testing.models.jpa.cacheable;

import static javax.persistence.GenerationType.TABLE;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.OneToOne;

@Embeddable
public class ProtectedEmbeddable {
    protected CacheableFalseEntity cacheableFalseEntity;
    protected String sub_name;
    
    public ProtectedEmbeddable() {}
    
    /**
     * @return the name
     */
    @Column(name="EMB_NAME")
    public String getName() {
        return sub_name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.sub_name = name;
    }

    /**
     * @return the protectedEntity
     */
    @OneToOne
    @JoinColumn(name="PROTECTED_FK")
    public CacheableFalseEntity getCacheableFalseEntity() {
        return cacheableFalseEntity;
    }

    /**
     * @param protectedEntity the protectedEntity to set
     */
    public void setCacheableFalseEntity(CacheableFalseEntity protectedEntity) {
        this.cacheableFalseEntity = protectedEntity;
    }
}
