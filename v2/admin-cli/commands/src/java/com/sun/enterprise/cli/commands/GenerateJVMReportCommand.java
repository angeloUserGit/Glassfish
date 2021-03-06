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

package com.sun.enterprise.cli.commands;

import com.sun.enterprise.cli.framework.*;

/**
 *  This command will get the version of the application server
 *  @version  $Revision: 1.4 $ 
 */
public class GenerateJVMReportCommand extends GenericCommand
{

    private static final String SUMMARY_OPERATION = "getSummary";
    private static final String CLASS_OPERATION = "getClassInformation";
    private static final String MEMORY_OPERATION = "getMemoryInformation";
    private static final String THREADDUMP_OPERATION = "getThreadDump";
    private static final String TYPE_OPTION = "type";
    private String typeOption;

    /**
     *  An abstract method that validates the options
     *  on the specification in the xml properties file
     *  This method verifies for the correctness of number of
     *  operands and if all the required options are supplied by the client.
     *  @return boolean returns true if success else returns false
     */
    public boolean validateOptions() throws CommandValidationException
    {
        if (!super.validateOptions()) 
            return false;

        typeOption = getOption(TYPE_OPTION);
        if (typeOption.matches("summary|memory|class|thread"))
        {
            return true;
        }
        else
        {
              throw new CommandValidationException(getLocalizedString(
                                                     "InvalidTypeOption"));
        }
    }


    /*
     * Returns the Operation name from the properties
     * @return operationName returns operation name
     */
    protected String getOperationName() throws CommandException
    {
        String operationName = SUMMARY_OPERATION;
        if (typeOption.equals("memory")) 
            operationName = MEMORY_OPERATION;
        else if (typeOption.equals("class"))
            operationName = CLASS_OPERATION;
        else if (typeOption.equals("thread"))
            operationName = THREADDUMP_OPERATION;
        return operationName;
    }

    protected void handleReturnValue(Object returnval)
    {
        final Class cl = returnval.getClass();
        if (cl.getName().equals(String.class.getName()))
        {
            final String returnString = (String)returnval;
	        CLILogger.getInstance().printMessage(returnString);
	    }
    }

}
