/*******************************************************************************
 * Copyright (c) 1998, 2009 Oracle. All rights reserved.
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
package org.eclipse.persistence.tools.workbench.uitools.app;

import java.util.Collection;

/**
 * Implementation of CollectionValueModel that can be used for
 * returning an iterator on a static collection, but still allows listeners to be added.
 * Listeners will NEVER be notified of any changes, because there should be none.
 */
public class ReadOnlyCollectionValueModel
	extends AbstractReadOnlyCollectionValueModel
{
	/** The value. */
	protected Collection value;

	private static final long serialVersionUID = 1L;


	/**
	 * Construct a read-only CollectionValueModel for the specified value.
	 */
	public ReadOnlyCollectionValueModel(Collection value) {
		super();
		if (value == null) {
			throw new NullPointerException();
		}
		this.value = value;
	}

	// ********** CollectionValueModel implementation **********

	/**
	 * @see CollectionValueModel#size()
	 */
	public int size() {
		return this.value.size();
	}


	// ********** ValueModel implementation **********

	/**
	 * @see ValueModel#getValue()
	 */
	public Object getValue() {
		return this.value.iterator();
	}

}
