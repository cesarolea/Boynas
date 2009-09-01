package com.divinesoft.boynas.persistence;

import hu.netmind.persistence.Store;

public class StoreUtil {
	//TODO: Read database parameters from an external file
	private static Store store = null;
	
	public static Store getStore(){
		return store;
	}
	
	static{
		store = new Store("org.hsqldb.jdbcDriver","jdbc:hsqldb:file:./db/boynas");
	}

}
