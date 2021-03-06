/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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

package org.jboss.weld.tests.decorators.resolution;

import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.jboss.weld.test.AbstractWeldTest;
import org.testng.annotations.Test;

import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.util.AnnotationLiteral;

import java.util.*;

/**
 * @author Marius Bogoevici
 */
@Artifact
@BeansXml("beans-basic.xml")
public class BasicDecoratorResolutionTest extends AbstractWeldTest
{

   @Test
   public void testBasicDecoratorInvocation()
   {
      SimpleBean simpleBean = getReference(SimpleBean.class, new AnnotationLiteral<Simple>(){});
      String result = simpleBean.hello("world");
      assert "simple-Hello, world-simple".equals(result);
   }

   @Test
   public void testComplexDecoratorInvocation()
   {
      ComplexBean complexBean = getReference(ComplexBean.class, new AnnotationLiteral<Complex>(){});
      String result = complexBean.hello("world");
      assert "simple-complex-Sophisticated Hello, world-complex-simple".equals(result);
   }
}
