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
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package org.eclipse.persistence.internal.jpa.parsing;

import org.eclipse.persistence.expressions.*;

/**
 * INTERNAL
 * <p><b>Purpose</b>: Represent a '<>' in EJBQL
 * <p><b>Responsibilities</b>:<ul>
 * <li> Generate the correct expression for an '<>' in EJBQL
 * </ul>
 *    @author Jon Driscoll and Joel Lucuik
 *    @since TopLink 4.0
 */
public class NotEqualsNode extends BinaryOperatorNode {

    /**
     * EqualsNode constructor comment.
     */
    public NotEqualsNode() {
        super();
    }

    /**
     * INTERNAL
     * Validate node and calculates its type.
     */
    public void validate(ParseTreeContext context) {
        super.validate(context);
        TypeHelper typeHelper = context.getTypeHelper();
        setType(typeHelper.getBooleanType());
    }

    /**
     * Resolve the expression. The steps are:
     * 1. Set the expressionBuilder for the left and right nodes
     * 2. Generate the expression for the left node
     * 3. Add the .notEqual to the where clause returned from step 2
     * 4. Generate the expression for the right side and use it as the parameter for the .equals()
     * 5. Return the completed where clause to the caller
     */
    public Expression generateExpression(GenerationContext context) {
        Expression whereClause = getLeft().generateExpression(context);
        whereClause = whereClause.notEqual(getRight().generateExpression(context));
        return whereClause;
    }

    /**
     * INTERNAL
     * Get the string representation of this node.
     */
    public String getAsString() {
        return left.getAsString() + " <> " + right.getAsString();
    }
}
