package ru.corp.az.azrun.jmx;

import java.util.List;

import javax.management.MBeanException;


public interface AzRunJmx {

	List<String>   getTaskList();
	void           stopConnPool()     throws MBeanException;
	void           startConnPool()    throws MBeanException;
	void           startAzCalc()     throws MBeanException;
	void           stopAzCalc()       throws MBeanException;
	String         getRootLoggingLevel();
	void           setRootLoggingLevel(String level);
	String         getAzCalcLoggingLevel();
	void           setAzCalcLoggingLevel(String level);	
} 
