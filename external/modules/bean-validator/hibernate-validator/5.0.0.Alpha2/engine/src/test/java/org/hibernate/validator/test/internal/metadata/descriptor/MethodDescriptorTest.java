/*
* JBoss, Home of Professional Open Source
* Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.validator.test.internal.metadata.descriptor;

import java.util.List;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import javax.validation.metadata.Scope;

import org.testng.annotations.Test;

import org.hibernate.validator.internal.metadata.descriptor.ExecutableDescriptorImpl;
import org.hibernate.validator.test.internal.metadata.Customer;
import org.hibernate.validator.test.internal.metadata.CustomerRepository;
import org.hibernate.validator.test.internal.metadata.CustomerRepository.ValidationGroup;
import org.hibernate.validator.test.internal.metadata.CustomerRepositoryExt;
import org.hibernate.validator.test.internal.metadata.CustomerRepositoryExt.CustomerExtension;

import static org.fest.assertions.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.testutil.ValidatorUtil.getMethodDescriptor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Gunnar Morling
 */
@Test
public class MethodDescriptorTest {

	@Test
	public void testGetMethod() throws Exception {
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "foo" );
		assertEquals( methodDescriptor.getName(), "foo" );
	}

	@Test
	public void testPropertyDescriptorType() {
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "foo" );
		assertEquals( methodDescriptor.getKind(), ElementDescriptor.Kind.METHOD );
	}

	@Test
	public void testNarrowDescriptor() {
		ElementDescriptor descriptor = getMethodDescriptor( CustomerRepositoryExt.class, "foo" );
		MethodDescriptor methodDescriptor = descriptor.as( MethodDescriptor.class );
		assertTrue( methodDescriptor != null );

		methodDescriptor = descriptor.as( ExecutableDescriptorImpl.class );
		assertTrue( methodDescriptor != null );
	}

	@Test(expectedExceptions = ClassCastException.class, expectedExceptionsMessageRegExp = "HV000118.*")
	public void testUnableToNarrowDescriptor() {
		ElementDescriptor descriptor = getMethodDescriptor( CustomerRepositoryExt.class, "foo" );
		descriptor.as( BeanDescriptor.class );
	}

	@Test
	public void testIsCascaded() {
		MethodDescriptor cascadingMethodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "foo" );
		assertTrue( cascadingMethodDescriptor.getReturnValueDescriptor().isCascaded() );

		MethodDescriptor nonCascadingMethodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "baz" );
		assertFalse( nonCascadingMethodDescriptor.getReturnValueDescriptor().isCascaded() );
	}

	@Test
	public void testHasConstraints() {
		MethodDescriptor constrainedMethodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "bar" );
		assertTrue( constrainedMethodDescriptor.hasConstraints() );

		MethodDescriptor unconstrainedMethodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "qux" );
		assertNull( unconstrainedMethodDescriptor );
	}

	@Test
	public void testGetElementClass() {
		//the return type as defined in the base type
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepository.class, "bar" );
		assertEquals( methodDescriptor.getElementClass(), Customer.class );

		//the return type is now the one as defined in the derived type (covariant return type)
		methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "bar" );
		assertEquals( methodDescriptor.getElementClass(), CustomerExtension.class );
	}

	@Test
	public void testGetConstraintDescriptors() {
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "bar" );

		assertEquals( methodDescriptor.getConstraintDescriptors().size(), 1 );
		assertEquals(
				methodDescriptor.getConstraintDescriptors().iterator().next().getAnnotation().annotationType(),
				NotNull.class
		);

		methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "baz" );
		assertEquals( methodDescriptor.getConstraintDescriptors().size(), 2 );
	}

	@Test(description = "HV-443")
	public void testFindReturnValueConstraintLookingAt() {
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "baz" );

		Set<ConstraintDescriptor<?>> constraintDescriptors = methodDescriptor.findConstraints()
				.lookingAt( Scope.LOCAL_ELEMENT )
				.getConstraintDescriptors();
		assertEquals( constraintDescriptors.size(), 1 );
		assertEquals( constraintDescriptors.iterator().next().getAnnotation().annotationType(), Min.class );

		constraintDescriptors = methodDescriptor.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.getConstraintDescriptors();
		assertEquals( constraintDescriptors.size(), 2 );
	}

	@Test(description = "HV-443")
	public void testFindParameterConstraintLookingAt() {
		ParameterDescriptor parameterDescriptor = getMethodDescriptor(
				CustomerRepositoryExt.class,
				"zap",
				int.class
		).getParameterDescriptors().get( 0 );

		Set<ConstraintDescriptor<?>> constraintDescriptors = parameterDescriptor.findConstraints()
				.lookingAt( Scope.LOCAL_ELEMENT )
				.getConstraintDescriptors();
		assertEquals( constraintDescriptors.size(), 1 );
		assertEquals( constraintDescriptors.iterator().next().getAnnotation().annotationType(), Min.class );

		constraintDescriptors = parameterDescriptor.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.getConstraintDescriptors();
		assertEquals( constraintDescriptors.size(), 2 );
	}

	@Test
	public void testFindConstraintMatchingGroups() {
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "baz" );

		Set<ConstraintDescriptor<?>> constraintDescriptors = methodDescriptor.findConstraints()
				.unorderedAndMatchingGroups( ValidationGroup.class )
				.getConstraintDescriptors();

		assertEquals( constraintDescriptors.size(), 1 );
		assertEquals( constraintDescriptors.iterator().next().getAnnotation().annotationType(), NotNull.class );
	}

	@Test
	public void testGetParameterConstraints() {
		MethodDescriptor methodDescriptor = getMethodDescriptor(
				CustomerRepositoryExt.class, "createCustomer", CharSequence.class, String.class
		);

		List<ParameterDescriptor> parameterConstraints = methodDescriptor.getParameterDescriptors();
		assertNotNull( parameterConstraints );
		assertEquals( parameterConstraints.size(), 2 );

		ParameterDescriptor parameterDescriptor1 = parameterConstraints.get( 0 );
		assertEquals( parameterDescriptor1.getElementClass(), CharSequence.class );
		assertFalse( parameterDescriptor1.hasConstraints() );

		ParameterDescriptor parameterDescriptor2 = parameterConstraints.get( 1 );
		assertEquals( parameterDescriptor2.getElementClass(), String.class );
		assertTrue( parameterDescriptor2.hasConstraints() );
	}

	@Test
	public void testGetParameterConstraintsForParameterlessMethod() {
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "baz" );

		List<ParameterDescriptor> parameterConstraints = methodDescriptor.getParameterDescriptors();
		assertNotNull( parameterConstraints );
		assertEquals( parameterConstraints.size(), 0 );
	}

	@Test
	public void testGetReturnValueDescriptorForVoidMethod() {
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "zap", int.class );
		assertThat( methodDescriptor.getReturnValueDescriptor() ).isNull();
	}

	@Test
	public void testIsReturnValueConstrainedForConstrainedMethod() {
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "baz" );
		assertThat( methodDescriptor.isReturnValueConstrained() ).isTrue();
	}

	@Test
	public void testIsReturnValueConstrainedForCascadedMethod() {
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "foo" );
		assertThat( methodDescriptor.isReturnValueConstrained() ).isTrue();
	}

	@Test
	public void testIsReturnValueConstrainedForParameterConstrainedMethod() {
		MethodDescriptor methodDescriptor = getMethodDescriptor(
				CustomerRepositoryExt.class,
				"createCustomer",
				CharSequence.class,
				String.class
		);
		assertThat( methodDescriptor.isReturnValueConstrained() ).isFalse();
	}

	@Test
	public void testIsReturnValueConstrainedForVoidMethod() {
		MethodDescriptor methodDescriptor = getMethodDescriptor( CustomerRepositoryExt.class, "zap", int.class );
		assertThat( methodDescriptor.isReturnValueConstrained() ).isFalse();
	}

	@Test
	public void testAreParametersConstrainedForParameterConstrainedMethod() {
		MethodDescriptor methodDescriptor = getMethodDescriptor(
				CustomerRepositoryExt.class,
				"createCustomer",
				CharSequence.class,
				String.class
		);
		assertThat( methodDescriptor.areParametersConstrained() ).isTrue();
	}

	@Test
	public void testAreParametersConstrainedForParameterCascadedMethod() {
		MethodDescriptor methodDescriptor = getMethodDescriptor(
				CustomerRepositoryExt.class,
				"saveCustomer",
				Customer.class
		);
		assertThat( methodDescriptor.areParametersConstrained() ).isTrue();
	}

	@Test
	public void testAreParametersConstrainedForNonParameterConstrainedMethod() {
		MethodDescriptor methodDescriptor = getMethodDescriptor(
				CustomerRepositoryExt.class,
				"zip",
				int.class
		);
		assertThat( methodDescriptor.areParametersConstrained() ).isFalse();
	}

	@Test
	public void testAreParametersConstrainedForParameterlessMethod() {
		MethodDescriptor methodDescriptor = getMethodDescriptor(
				CustomerRepositoryExt.class,
				"zip"
		);
		assertThat( methodDescriptor.areParametersConstrained() ).isFalse();
	}
}
