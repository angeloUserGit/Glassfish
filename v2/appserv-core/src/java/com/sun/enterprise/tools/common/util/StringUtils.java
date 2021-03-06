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
 * StringUtils.java
 *
 * Created on November 16, 2000, 12:01 AM
 */

package com.sun.enterprise.tools.common.util;

/**
 * 
 * @author  bnevins
 * @version 1.0
 */
public class StringUtils
{
	public static String UpperCaseFirstLetter(String s)
	{
		if(s == null || s.length() <= 0)
			return s;
		
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	////////////////////////////////////////////////////////////////////////////
	
	public static String padRight(String s, int len)
	{
		if(s == null || s.length() >= len)
			return s;

		for(int i = len - s.length(); i > 0; --i)
			s += ' ';

		return s;
	}


	////////////////////////////////////////////////////////////////////////////
	
	public static String padLeft(String s, int len)
	{
		if(s == null || s.length() >= len)
			return s;

		String ret = ""; // NOI18N
		
		for(int i = len - s.length(); i > 0; --i)
			ret += ' ';

		return ret + s;
	}

	////////////////////////////////////////////////////////////////////////////
	
	public static String toShortClassName(String className)
	{
		int index = className.lastIndexOf('.');

		if(index >= 0 && index < className.length() - 1)
			return className.substring(index + 1);

		return className;
	}

                ////////////////////////////////////////////////////////////////////////////

        public static String replace(String s, String token, String replace)
	{
		if(s == null || s.length() <= 0 || token == null || token.length() <= 0)
			return s;
		
		int index = s.indexOf(token);

		if(index < 0)
			return s;

		int tokenLength = token.length();
		String ret = s.substring(0, index);
		ret += replace;
		ret += s.substring(index + tokenLength);

		return ret;
	}

        ////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args)
	{
		String[] test = new String[] { "xyz", "HITHERE", "123aa", "aSSS", "yothere" };//NOI18N
		
		for(int i = 0; i < test.length; i++)
		{
			System.out.println(test[i] + " >>> " + UpperCaseFirstLetter(test[i]));//NOI18N
		}
	}
        public static boolean isEmpty(String s)
	{
            if((s != null) && (! s.trim().equals(""))) // NOI18N
            {
                return false;
            }else
            {
                return true;
            }
        }
        

}
