package ru.corp.az.azrun.azcalc.task.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import ru.corp.az.azrun.azcalc.dao.api.AzCalcDao;
import ru.corp.az.azrun.azcalc.task.AzTaskNames;
import ru.corp.az.azrun.common.GlobalConfig;
import ru.corp.az.azrun.common.taskx.ManagedTask;
import ru.corp.az.azrun.common.taskx.OperationNotAllowed;
import ru.corp.az.azrun.common.taskx.TaskDescriptor;
import ru.corp.az.azrun.common.taskx.TaskPool;

@Component("azTaskUtils")
public class GenericAzTaskUtils  implements AzTaskUtils {
	
	private static final int operationTimeout = 3600;
	
	private Logger logger = LoggerFactory.getLogger(GenericAzTaskUtils.class);
	
	@Autowired
	@Qualifier("azCalcDao")
	private AzCalcDao azCalcDao;
	
	@Autowired
	@Qualifier("taskPool")
	private TaskPool taskPool;
	
	@Autowired
	@Qualifier("taskExecutorMap")
	private HashMap<String, ThreadPoolExecutor> taskExecutorMap;		
	
	@Autowired
	@Qualifier("mainScheduler")
	private ThreadPoolTaskScheduler mainScheduler;	
	

	private void stopAzQueue() {
		try {
			azCalcDao.setTaskStatusJob(false);
			for(ManagedTask t : taskPool.getAll() ) 
				if(t.getTaskDescriptor().getLabel().equals(AzTaskNames.AZ_QUEUE_TASK))			
					t.stop();	
			logger.info("stop AZ_QUEUE_TASK: OK");
		}
		catch(OperationNotAllowed e) {			
		}		
	}

	@Override
	public void startAzCalc() {
		try {			
			azCalcDao.setTaskStatusJob(true);
			for(ManagedTask t : taskPool.getAll() ) 
				if(t.getTaskDescriptor().getLabel().equals(AzTaskNames.AZ_QUEUE_TASK))			
					t.start();
			logger.info("start AZ Calc: OK");
		}
		catch(OperationNotAllowed e) {
		}		
	}

	@Override
	public void stopAzCalc() {
		stopAzQueue();
		int i = 0;
		int n = operationTimeout;
		while(n > i) {
			sleep(5000);
			boolean exit = true;
			for(ManagedTask t : taskPool.getAll() ) 
				if(t.getTaskDescriptor().getLabel().equals(AzTaskNames.AZ_CALC_TASK))			
					exit = false;
			if(exit){
				logger.info("stop AZ Calc: OK");
				return;
			}
			++i;	
			/*
			 * если не вызывать nodeMon при остановке, задания могут захватиться другими узлами 
			 * при остановленном AZ_QUEUE_TASK
			 */			
			azCalcDao.nodeMon(GlobalConfig.HOST_HAME);
		}
		if(i == n) {
			logger.error("stop AZ_CALC_TASK's: FAIL");
			throw new OperationTimeoutException("AZ_CALC_TASK wait completion timeout");
		}
	}

	@Override
	public void shutdownThreadPools() {
		if(taskExecutorMap != null)
			for(String key : taskExecutorMap.keySet()) {
				taskExecutorMap.get(key).shutdown();
				logger.info("shutdown ThreadPoolTaskExecutor: "+key);
			}		
	}

	@Override
	public void shutdownSchedulers() {
		if(mainScheduler != null) {
			mainScheduler.shutdown();
			logger.info("shutdown ThreadPoolTaskScheduler: mainScheduler");
		}			
	}
	
	@Override
	public List<String> getTaskList() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("ID; NAME; STATUS; ACTION; START_TIME; LAST_START_TIME");
		List<ManagedTask> tList = taskPool.getAll();
		Collections.sort(tList);
		for(ManagedTask t : tList ) {
			TaskDescriptor td = t.getTaskDescriptor();
			result.add(td.getId()+"; "+td.getName()+"; "+td.getStatus()+"; "+td.getAction()+"; "+
			           formatDate(td.getStartTime())+"; "+formatDate(td.getLastStartTime()));			
		}
		return result;
	}

	private String formatDate(Date date) {
		//SimpleDateFormat не thread safe
		DateFormat       dateFormat   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return  (date == null) ? "" : dateFormat.format(date);
	}
	
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
}
