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
 *     Oracle = 2.2 - Initial contribution
 ******************************************************************************/
package org.eclipse.persistence.oxm.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for a user defined property.
 * A single Property may be specified directly on a mapped attribute or its 
 * get/set method. Multiple Properties should be wrapped into Properties 
 * annotation.
 */
@Target({METHOD, FIELD, TYPE})
@Retention(RUNTIME)
public @interface XmlProperty {
    /**
     * Property name.
     */ 
    String name();

    /**
     * String representation of Property value,
     * converted to an instance of valueType.
     */ 
    String value();

    /**
     * Property value type.
     * The value converted to valueType by ConversionManager.
     * If specified must be a simple type that could be handled by ConversionManager: 
     * numerical, boolean, temporal.  
     */ 
    Class valueType() default String.class;
}
