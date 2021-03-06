package org.jboss.weld.tests.unit.deployment.structure.extensions;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessManagedBean;

public class CountingObserver2 implements Extension
{
   
   private int beforeBeanDiscovery;
   private int processFooManagedBean;
   private int processBarManagedBean;

   public void observeBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager)
   {
      beforeBeanDiscovery++;
      event.addAnnotatedType(beanManager.createAnnotatedType(Bar.class));
   }
   
   public void observerProcessFooManagedBean(@Observes ProcessManagedBean<Foo> event)
   {
      processFooManagedBean++;
   }
   
   public void observerProcessBarManagedBean(@Observes ProcessManagedBean<Bar> event)
   {
      processBarManagedBean++;
   }
   
   public int getBeforeBeanDiscovery()
   {
      return beforeBeanDiscovery;
   }
   
   public int getProcessFooManagedBean()
   {
      return processFooManagedBean;
   }
   
   public int getProcessBarManagedBean()
   {
      return processBarManagedBean;
   }
     
}
