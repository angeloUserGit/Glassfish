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
package com.sun.enterprise.management.monitor;

import javax.management.ObjectName;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenDataException;

import javax.management.j2ee.statistics.Statistic;
import javax.management.j2ee.statistics.Stats;
import javax.management.j2ee.statistics.CountStatistic;
import javax.management.j2ee.statistics.BoundaryStatistic;
import javax.management.j2ee.statistics.RangeStatistic;
import javax.management.j2ee.statistics.BoundedRangeStatistic;
import javax.management.j2ee.statistics.TimeStatistic;

import com.sun.appserv.management.util.j2ee.J2EEUtil;
import com.sun.appserv.management.util.misc.TypeCast;

import com.sun.appserv.management.j2ee.statistics.*;


import com.sun.enterprise.management.AMXTestBase;
import com.sun.enterprise.management.Capabilities;


public final class StatisticTest
	extends AMXTestBase
{
    	public
    StatisticTest()
    {
    }
    
    	private void
    checkOpenDataConversion( final Statistic s )
    	throws OpenDataException
    {
    	final CompositeData	d	= J2EEUtil.statisticToCompositeData( s );
    	final Statistic	roundTrip	= StatisticFactory.create( d );
    	
		assert( s.equals( roundTrip ) ) :
			"Conversion to CompositeData and back to Statistic failed:\n" +
			toString( s ) + " != " + toString( roundTrip );
    }
    
    public static final class TestStatistic extends StatisticImpl
    {
        public static final long  serialVersionUID = 9999999;
        
    	private final int		Foo;
    	private final String	Bar;
    	
    	public TestStatistic()
    	{
    		super( "Test", "test dummy", "none", 0, System.currentTimeMillis() );
    		
    		Foo	= 999;
    		Bar	= "Bar";
    	}
    	
    	public int		getFoo()	{ return( Foo ); }
    	public String	getBar()	{ return( Bar ); }
    }
    
    	public void
	testAnyOpenDataConversion()
    	throws OpenDataException
	{
		// verify that anything implementing Statistic works correctly
		final TestStatistic		test	= new TestStatistic();
		final MapStatisticImpl	testMap	= new MapStatisticImpl( test );
		assert( testMap.getValue( "Foo" ).equals( new Integer( test.getFoo() ) ) );
		assert( testMap.getValue( "Bar" ).equals(  test.getBar() ) );
		
    	final CompositeData	d	= J2EEUtil.statisticToCompositeData( testMap );
    	final CompositeType	t	= d.getCompositeType();
    	final Set<String> values	= TypeCast.asSet( t.keySet() );
    	assert( values.contains( "Name" ) );
    	assert( values.contains( "Foo" ) );
    	assert( values.contains( "Bar" ) );
    	
    	final MapStatisticImpl	roundTrip	= (MapStatisticImpl)StatisticFactory.create( d );
		assert( new Integer( test.getFoo() ).equals( roundTrip.getValue( "Foo" ) ) );
		assert( test.getBar().equals( roundTrip.getValue( "Bar" ) ) );
	}
	
    	public void
	testStdOpenDataConversion()
    	throws OpenDataException
	{
		final CountStatisticImpl	c	=
			new CountStatisticImpl( "Count", "desc", "number", 0, now(), 99);
			
		final RangeStatisticImpl	r	=
			new RangeStatisticImpl( "Range", "desc", "number", 0, now(), 0, 50, 100);
			
		final BoundaryStatisticImpl	b	=
			new BoundaryStatisticImpl( "Boundary", "desc", "number", 0, now(), 0, 100);
			
		final BoundedRangeStatisticImpl	br	=
			new BoundedRangeStatisticImpl( "BoundedRange", "desc", "number", 0, now(), 0, 50, 100, 0, 100);
			
		final TimeStatisticImpl	t		=
			new TimeStatisticImpl( "Time", "desc", "seconds", 0, now(), 1, 10, 100, 1000);
			
		final StringStatisticImpl	s		=
			new StringStatisticImpl( "String", "desc", "chars", 0, now(), "hello" );
		
		final NumberStatisticImpl	n		=
			new NumberStatisticImpl( "Number", "desc", "number", 0, now(), 1234.56 );
                
		checkOpenDataConversion( c );
		checkOpenDataConversion( r );
		checkOpenDataConversion( b );
		checkOpenDataConversion( br );
		checkOpenDataConversion( t );
		checkOpenDataConversion( s );
		checkOpenDataConversion( n );
	}
 
}






