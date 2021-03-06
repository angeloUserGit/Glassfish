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
package com.sun.appserv.management.util.jmx;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;

import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationListener;
import javax.management.NotificationFilter;
import javax.management.relation.MBeanServerNotificationFilter;
import javax.management.AttributeChangeNotificationFilter;
import javax.management.NotificationFilterSupport;
import javax.management.Notification;
import javax.management.AttributeChangeNotification;
import javax.management.MBeanServerNotification;
import javax.management.ListenerNotFoundException;


import com.sun.appserv.management.util.misc.ListUtil;

/**
	Features:
	<ul>
	<li>Maintains information on all NotificationListeners so that queries can
	be made on the number of listeners, and the number of listeners of each type</li>
	<li>optionally sends all Notifications asynchronously via a separate Thread</li>
	</ul>
 */
public class NotificationEmitterSupport
    extends NotificationBroadcasterSupport
{
	private final boolean		mAsyncDelivery;
	private SenderThread		mSenderThread;
	private final Map<String,Integer>   mListenerTypeCounts;
	private final NotificationListenerTracking   mListeners;
	
	
		public
	NotificationEmitterSupport(
		final boolean	asyncDelivery)
	{
		mAsyncDelivery	= asyncDelivery;
		// don't create a thread until needed
		mSenderThread	= null;
		
		mListenerTypeCounts = Collections.synchronizedMap( new HashMap<String,Integer>() );
		
		mListeners = new NotificationListenerTracking( true );
	}
	
		private synchronized SenderThread
	getSenderThread()
	{
		if ( mSenderThread == null )
		{
			mSenderThread	= mAsyncDelivery ? new SenderThread() : null;
			if ( mSenderThread != null )
			{
				mSenderThread.start();
			}
		}
		
		return( mSenderThread );
	}
	
		public synchronized void
	cleanup()
	{
		if ( mSenderThread != null )
		{
			mSenderThread.quit();
			mSenderThread	= null;
		}
	}
	
	/**
		Synchronously (on current thread), ensure that all Notifications
		have been delivered.
	 */
		public void
	sendAll( )
	{
		if ( mSenderThread != null )
		{
			mSenderThread.sendAll();
		}
	}
	
		public int
	getListenerCount()
	{
		return( mListeners.getListenerCount() );
	}
	
		public int
	getNotificationTypeListenerCount( final String type )
	{
	    final Integer   count   = mListenerTypeCounts.get( type );
	    
	    int resultCount = 0;
	    
	    if ( count == null )
	    {
	        final Integer allCount  = mListenerTypeCounts.get( WILDCARD_TYPE );
	        if ( allCount != null )
	        {
	            resultCount = allCount;
	        }
	        else
	        {
	            // no wildcards are in use
	        }
	    }
	    
		return( resultCount );
	}
	
	
	private static final String[]   NO_TYPES  = new String[0];
	private static final String     WILDCARD_TYPE  = "***";
	private static final String[]   ALL_TYPES  = new String[] { WILDCARD_TYPE };
	
	private static final String[]   ATTRIBUTE_CHANGE_TYPES  = new String[]
	{
	    AttributeChangeNotification.ATTRIBUTE_CHANGE
	};
	
	private static final String[]   MBEAN_SERVER_NOTIFICATION_TYPES  = new String[]
	{
	    MBeanServerNotification.REGISTRATION_NOTIFICATION,
	    MBeanServerNotification.UNREGISTRATION_NOTIFICATION,
	};
	
	
	private final Integer   COUNT_1 = new Integer( 1 );
	
	    private void
	incrementListenerCountForType( final String type )
	{
	    synchronized( mListenerTypeCounts )
	    {
	        final Integer count   = mListenerTypeCounts.get( type );
	        
	        final Integer newCount  = (count == null ) ?
	                                    COUNT_1 : new Integer( count.intValue() + 1 );
	        
	        mListenerTypeCounts.put( type, newCount );
	    }
	}
	
	   private void
	decrementListenerCountForType( final String type )
	{
	    synchronized( mListenerTypeCounts )
	    {
	        final Integer count   = mListenerTypeCounts.get( type );
	        if ( count == null )
	        {
	            throw new IllegalArgumentException( type );
	        }
	        
	        final int   oldValue    = count.intValue();
	        if ( oldValue == 1 )
	        {
	            mListenerTypeCounts.remove( type );
	        }
	        else
	        {
	            mListenerTypeCounts.put( type, new Integer( oldValue - 1 ) );
	        }
	    }
	}
	
	
	    private String[]
	getTypes(
		final NotificationFilter filter )
	{
	    String[]    types   = NO_TYPES;
	    
	    if ( filter instanceof NotificationFilterSupport )
	    {
	        final NotificationFilterSupport fs  = (NotificationFilterSupport)filter;
	        
	        types   = ListUtil.toStringArray( fs.getEnabledTypes() );
	    }
	    else if ( filter instanceof AttributeChangeNotificationFilter )
	    {
	        types   = ATTRIBUTE_CHANGE_TYPES;
	    }
	    else if ( filter instanceof MBeanServerNotificationFilter )
	    {
	        types   = MBEAN_SERVER_NOTIFICATION_TYPES;
	    }
	    else
	    {
	        // no filter, or non-standard one, have to assume all types
	        types   = ALL_TYPES;
	    }
	    
	    return types;
	}
	
	    private void
	addFilterTypeCounts( final NotificationFilter filter )
	{
	    String[]  types  = getTypes( filter );
    	    
	    for( String type : types )
	    {
	        incrementListenerCountForType( type );
	    }
	}
	
	    private void
	removeFilterTypeCounts( final NotificationFilter filter )
	{
    	final String[]  types   = getTypes( filter );
    	    
	    for( String type : types )
	    {
	        decrementListenerCountForType( type );
	    }
	}
	
	    private void
	removeFilterTypeCounts( final List<NotificationListenerInfo> infos )
	{
	    for( NotificationListenerInfo info : infos )
	    {
	        removeFilterTypeCounts( info.getFilter() );
	    }
	}
	
		public void
	addNotificationListener(
		final NotificationListener listener,
		final NotificationFilter filter,
		final Object handback)
	{
		super.addNotificationListener( listener, filter, handback );
		
		mListeners.addNotificationListener( listener, filter, handback );
		addFilterTypeCounts( filter );
	}
	
		public void
	removeNotificationListener(final NotificationListener listener)
		throws ListenerNotFoundException
	{
		super.removeNotificationListener( listener );
		
		final List<NotificationListenerInfo>    infos =
		    mListeners.removeNotificationListener( listener );
		removeFilterTypeCounts( infos );
	}
	
		public void
	removeNotificationListener(
		final NotificationListener	listener,
		final NotificationFilter	filter,
		final Object				handback)
		throws ListenerNotFoundException
	{
		super.removeNotificationListener( listener, filter, handback );
		
		mListeners.removeNotificationListener( listener );
		if ( filter != null )
		{
		    removeFilterTypeCounts( filter );
		}
		
	}
	
		protected void
	internalSendNotification( final Notification notif )
	{
		super.sendNotification( notif );
	}

	/**
		Send the Notification.  If created with async=true,
		then this routine returns immediately and the Notification is sent
		on a separate Thread.
	 */
		public void
	sendNotification( final Notification notif )
	{
		if ( getListenerCount() != 0 )
		{
			if ( getSenderThread() != null )
			{
				mSenderThread.enqueue( notif );
			}
			else
			{
				internalSendNotification( notif );
			}
		}
	}
	
	private final class SenderThread extends Thread
	{
		private boolean	mQuit;
		private List<Notification>	mPendingNotifications;
		
			public
		SenderThread()
		{
			mQuit	= false;
			mPendingNotifications	=
			    Collections.synchronizedList( new LinkedList<Notification>() );
		}
		
			public void
		quit()
		{
			mQuit	= true;
			notifySelf();
		}
		
		
			private void
		notifySelf()
		{
			synchronized( this )
			{
				this.notify();
			}
		}
		
			private void
		enqueue( final Notification notif )
		{
			mPendingNotifications.add( notif );
			notifySelf();
		}
		
			public boolean
		sendAll()
		{
			Notification	notif			= null;
			boolean			sentSomething	= false;
			
			while ( ! mPendingNotifications.isEmpty() )
			{
				sentSomething	= true;	// or rather, we'll try to
				
				try
				{
					notif = mPendingNotifications.remove( 0 );
					internalSendNotification( notif );
				}
				catch( ArrayIndexOutOfBoundsException e )
				{
					// can happen if more than one Thread is in here
				}
			}
			
			return( sentSomething );
		}
		
			public void
		run()
		{
			// wake up every 5 minutes
			final int	INTERVAL	=  5 * 1000;
			
			mQuit	= false;
			
			while ( ! mQuit )
			{
				try
				{
					synchronized( this )
					{
						wait( INTERVAL );
					}
				}
				catch( InterruptedException e )
				{
				}
				
				if ( mQuit )
				{
					break;
				}
				
				final boolean	sentSomething	= sendAll();
				
				// nothing available, get rid of ourself till needed
				if ( ! sentSomething )
				{
					cleanup();
					// now no new ones can be added, but ensure any pending are sent
					sendAll();
					break;
				}
			}
		}
	}

}

