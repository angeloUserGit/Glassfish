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

package com.sun.org.apache.jdo.impl.enhancer.classfile.asm;

import java.io.PrintWriter;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;

import com.sun.org.apache.jdo.enhancer.classfile.FieldInfo;


public class FieldInfoImpl
	extends MemberInfoImpl
	implements FieldInfo, Opcodes
{
	
	private FieldNode fieldNode;
	
	public FieldInfoImpl(FieldNode fieldNode) {
		this.fieldNode = fieldNode;
		super.initializeMemberInfo(fieldNode);
	}

	FieldNode getFieldNode() {
		return this.fieldNode;
	}

}
