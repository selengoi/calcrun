package ru.corp.az.azrun.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GlobalConfig {
	
	public  static       String      HOST_HAME;
	
	private GlobalConfig() {		
	}
	
	static {
		try {				
			InetAddress addr = InetAddress.getLocalHost();
			HOST_HAME = addr.getHostName();
		} catch (UnknownHostException e) {
			HOST_HAME = "localhost";
		}		
	}
	
}
