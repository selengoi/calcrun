package ru.corp.az.azrun.common;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommonUtils {

	private CommonUtils() {		
	}
	
	public static String getStackTrace(Throwable t){
		 StringWriter sw = new StringWriter();
		 PrintWriter pw = new PrintWriter(sw);			 
		 t.printStackTrace(pw);
		 return sw.getBuffer().toString();
	}
	
}
