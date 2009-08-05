package com.acme;


import javax.ejb.*;
import javax.annotation.*;

import javax.naming.InitialContext;

import java.util.concurrent.*;

import com.sun.ejte.ccl.reporter.SimpleReporterAdapter;

public class Client {

    private static SimpleReporterAdapter stat = 
        new SimpleReporterAdapter("appserv-tests");

    private static String appName;
    private static String principal;

    @EJB static Hello hello;

    @EJB(lookupName="java:app/env/forappclient") 
    static Hello hello3;

    @EJB(mappedName="java:app/ejb-ejb31-security-simple-ejb/SingletonBean!com.acme.Hello") 
    static Hello hello4;

    public static void main(String args[]) {

	appName = args[0];
	principal = args[1];
	stat.addDescription(appName);
	Client client = new Client(args);       
        client.doTest();	
        stat.printSummary(appName + "ID");
    }

    public Client(String[] args) {}

    public void doTest() {

	System.out.println("Executing test with user principal " + principal);
	boolean havePermission = principal.equals("bob");
	if( havePermission ) {
	    System.out.println("Expecting permission to access protected methods");
	} else {
	    System.out.println("NOT expecting permission to access protected methods");
	}

	String results;

	try {

	    //	    ProgrammaticLogin login = new com.sun.appserv.security.api.ProgrammaticLogin();
	    
	    if( hello == null ) {
		hello = (Hello) new InitialContext().lookup("com.acme.Hello");
	    } else {
		// In an appclient.  
		Hello hello2 = (Hello) new InitialContext().lookup("java:app/env/forappclient");
		System.out.println("java:app/env/forappclient lookup = " + hello2);
		System.out.println("hello3 = " + hello3);

		Hello hello5 = (Hello) new InitialContext().lookup("java:app/ejb-ejb31-security-simple-ejb/SingletonBean!com.acme.Hello");
		System.out.println("hello4 = " + hello4);
		System.out.println("hello5 = " + hello5);
		String env = (String) new InitialContext().lookup("java:app/enventryforappclient");
		System.out.println("java:app env entry = " + env);
	    }

	    boolean pass;

	    try {
		hello.protectedSyncRemote();
		pass = havePermission; 
	    } catch(EJBAccessException e) {
		pass = !havePermission;
	    }
	    

	    System.out.println("pass = " + pass);

	    try {
		hello.unprotectedSyncRemote();
		pass = true;
	    } catch(EJBAccessException e) {
		pass = false;
	    }


	    System.out.println("pass = " + pass);

	    try {
		Future<Object> future = hello.protectedAsyncRemote();
		Object obj = future.get();
		pass = havePermission;
		
	    } catch(ExecutionException ee) {
		if( ee.getCause() instanceof EJBAccessException ) {
		    pass = !havePermission;
		} else {
		    pass = false;
		}
	    }


	    System.out.println("pass = " + pass);


	    try {
		Future<Object> future = hello.unprotectedAsyncRemote();
		Object obj = future.get();
		pass = true;
	    } catch(ExecutionException ee) {
		pass = false;
	    }



	    System.out.println("pass = " + pass);


	    try {
		hello.testProtectedSyncLocal();
		pass = havePermission;
	    }  catch(EJBAccessException e) {
		pass = !havePermission;
	    }



	    System.out.println("pass = " + pass);


	    try {
		hello.testUnprotectedSyncLocal();
		pass = true;
	    }  catch(Exception e) {
		pass = false;
	    }


	    System.out.println("pass = " + pass);


	    try {
		hello.testProtectedAsyncLocal();
		pass = havePermission;
	    }  catch(EJBAccessException e) {
		pass = !havePermission;
	    }



	    System.out.println("pass = " + pass);

	    try {
		hello.testUnprotectedAsyncLocal();
		pass = true;
	    }  catch(Exception e) {
		pass = false;
	    }

	    System.out.println("pass = " + pass);


	    stat.addStatus("local main", stat.PASS);

	} catch(Exception e) {
	    stat.addStatus("local main", stat.FAIL);
	    e.printStackTrace();
	}
    }


}
