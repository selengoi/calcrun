package ru.corp.az.azrun.azcalc.task.utils;

import java.util.List;

public interface AzTaskUtils {

	void startAzCalc()        ;
	void stopAzCalc()         ;
	void shutdownSchedulers() ;
	void shutdownThreadPools();
	List<String> getTaskList();
} 
