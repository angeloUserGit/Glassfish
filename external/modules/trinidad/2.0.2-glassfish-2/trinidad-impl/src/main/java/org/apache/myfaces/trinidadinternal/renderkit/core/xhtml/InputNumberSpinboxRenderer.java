/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.myfaces.trinidadinternal.renderkit.core.xhtml;

import javax.faces.component.UIComponent;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.core.input.CoreInputNumberSpinbox;


public class InputNumberSpinboxRenderer extends InputLabelAndMessageRenderer
{
  public InputNumberSpinboxRenderer()
  {
    super(CoreInputNumberSpinbox.TYPE);
  }

  protected InputNumberSpinboxRenderer(
    FacesBean.Type type)
  {
    super(type);
  }

  @Override
  protected void findTypeConstants(
    FacesBean.Type type)
  {
    super.findTypeConstants(type);
    _simpleInputNumberSpinbox = new SimpleInputNumberSpinboxRenderer(type);
  }

  @Override
  protected String getRootStyleClass(
    UIComponent component,
    FacesBean   bean)
  {
    return "af|inputNumberSpinbox";
  }

  @Override
  protected String getDefaultLabelValign(
    UIComponent component,
    FacesBean   bean)
  {
    return null;
  }

  @Override
  protected FormInputRenderer getFormInputRenderer()
  {
    return _simpleInputNumberSpinbox;
  }

  private SimpleInputNumberSpinboxRenderer _simpleInputNumberSpinbox;
}
