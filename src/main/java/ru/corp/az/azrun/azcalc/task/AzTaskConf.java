package ru.corp.az.azrun.azcalc.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import ru.corp.az.azrun.azcalc.dao.AzCalcData;
import ru.corp.az.azrun.azcalc.dao.api.AzCalcDao;
import ru.corp.az.azrun.common.taskx.GenericManagedTask;
import ru.corp.az.azrun.common.taskx.TaskPool;

@Configuration("azTaskConf")
public class AzTaskConf {
	
	@Autowired
	@Qualifier("azCalcDao")
	private AzCalcDao azCalcDao;
	
	@Autowired
	@Qualifier("taskPool")
	private TaskPool taskPool;	
	
	@Bean
	@Scope("prototype")
	
	public ThreadPoolExecutor getTaskExecutor() {
		BlockingQueue queue = new PriorityBlockingQueue<GenericManagedTask>(10, new AzTaskComparator());
		ThreadPoolExecutor te = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10, TimeUnit.SECONDS, queue);		
		return te;		
	}
	
	@Bean
	@Scope("prototype")
	public AzCalcTask getAzCalcTask(AzCalcData azCalcData) {
		AzCalcTask azCalcTask = new AzCalcTask();
		azCalcTask.setAzCalcData(azCalcData);		
		azCalcTask.setAzCalcDao(azCalcDao);
		azCalcTask.setTaskPool(taskPool);	
		return azCalcTask;
	}

	public AzCalcDao getAzCalcDao() {
		return azCalcDao;
	}

	public void setAzCalcDao(AzCalcDao azCalcDao) {
		this.azCalcDao = azCalcDao;
	}

	public TaskPool getTaskPool() {
		return taskPool;
	}

	public void setTaskPool(TaskPool taskPool) {
		this.taskPool = taskPool;
	}
}
