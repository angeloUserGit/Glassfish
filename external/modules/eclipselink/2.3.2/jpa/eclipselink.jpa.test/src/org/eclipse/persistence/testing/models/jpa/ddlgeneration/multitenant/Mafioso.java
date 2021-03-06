/*******************************************************************************
 * Copyright (c) 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     04/28/2011-2.3 Guy Pelletier 
 *       - 337323: Multi-tenant with shared schema support (part 6)
 ******************************************************************************/  
package org.eclipse.persistence.testing.models.jpa.ddlgeneration.multitenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.ObjectTypeConverter;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.InheritanceType.JOINED;

@Entity
@Table(name="DDL_MAFIOSO")
@Multitenant
@TenantDiscriminatorColumn(name="TENANT_ID", contextProperty="tenant.id", primaryKey=true)
@Inheritance(strategy=JOINED)
@DiscriminatorColumn(name="DTYPE")
public abstract class Mafioso {
    public enum Gender { Female, Male }
    
    private int id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private MafiaFamily family;

    public Mafioso() {}

    @ManyToOne
    @JoinColumn(name="FAMILY_ID")
    public MafiaFamily getFamily() { 
        return family; 
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    @ObjectTypeConverter(
        name="gender",
        dataType=String.class,
        objectType=org.eclipse.persistence.testing.models.jpa.ddlgeneration.multitenant.Mafioso.Gender.class,
        conversionValues={
            @ConversionValue(dataValue="F", objectValue="Female"),
            @ConversionValue(dataValue="M", objectValue="Male")
        }
    )
    public Gender getGender() {
        return gender;
    }
    
    @Id
    @Column(name="ID")
    @GeneratedValue
    public int getId() { 
        return id; 
    }

    public String getLastName() {
        return lastName;
    }

    public void setFamily(MafiaFamily family) {
        this.family = family;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
