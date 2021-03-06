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
package org.eclipse.persistence.tools.workbench.framework.ui.chooser;

import java.util.Iterator;

/**
 * Used by the UI to get and refresh a collection of "class descriptions"
 * that can be queried via a ClassDescriptionAdapter.
 */
public interface ClassDescriptionRepository {

	/**
	 * Return an iterator on the collection of "class descriptions"
	 * currently in the repository.
	 */
	Iterator classDescriptions();

	/**
	 * Refresh the collection of "class descriptions" in the repository.
	 */
	void refreshClassDescriptions();

}
