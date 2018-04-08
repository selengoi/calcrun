package ru.corp.az.azrun.azcalc.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ru.corp.az.azrun.azcalc.dao.AzCalcData;
import ru.corp.az.azrun.azcalc.dao.AzParallelDegree;
import ru.corp.az.azrun.azcalc.dao.api.AzCalcDao;
import ru.corp.az.azrun.common.CommonUtils;
import ru.corp.az.azrun.common.GlobalConfig;
import ru.corp.az.azrun.common.taskx.GenericManagedTask;
import ru.corp.az.azrun.common.taskx.TaskStatus;
import ru.corp.az.azrun.common.taskx.TaskType;

public class AzQueueTask extends GenericManagedTask {

	@Autowired
	@Qualifier("azTaskConf")
	private AzTaskConf azTaskConf;
	
	@Autowired
	@Qualifier("azCalcDao")
	private AzCalcDao azCalcDao;
	
	private Logger logger = LoggerFactory.getLogger(AzQueueTask.class);
	
	private Map<String, ThreadPoolExecutor> taskExecutorMap;
	
	public AzQueueTask() {
		super();
		taskDescriptor.setName(AzTaskNames.AZ_QUEUE_TASK+"_"+taskDescriptor.getId());
		taskDescriptor.setAction("");
		taskDescriptor.setType(TaskType.SCHEDULED);
		taskDescriptor.setLabel(AzTaskNames.AZ_QUEUE_TASK);
		taskDescriptor.setStatus(TaskStatus.STOPPED);
	}

	@Override
	public void preRun() {	
		azCalcDao.nodeMon(GlobalConfig.HOST_HAME);
		List<AzParallelDegree>  degList = azCalcDao.getParallelDegree(GlobalConfig.HOST_HAME);
		logger.debug("Thread pools to init/resize: "+degList.size());
		
		int    degree;
		String typeName;
		ThreadPoolExecutor te = null;
		for(AzParallelDegree deg : degList) {
			degree   = deg.getParallelDegree();
			typeName = deg.getTypeName();
			
			if(taskExecutorMap.containsKey(typeName) && degree <= 0) {
				te = taskExecutorMap.get(typeName);
				taskExecutorMap.remove(typeName);
				te.shutdown();
				logger.debug("task_pool('"+typeName+"'): shutdown due size="+degree);
				continue;
			}
			else if(taskExecutorMap.containsKey(typeName)) 
				te = taskExecutorMap.get(typeName);
			else if(degree > 0) {												
				te = azTaskConf.getTaskExecutor();
				te.setCorePoolSize(degree);
				taskExecutorMap.put(typeName, te);				
				logger.debug("task_pool('"+typeName+"'): created with size="+degree);
				continue;
			}
			else {
				logger.debug(typeName+": nothing to init/create size="+degree);
				continue;
			}
			
			if(te.getCorePoolSize() < degree) {
				te.setMaximumPoolSize(Integer.MAX_VALUE);
				te.setCorePoolSize(degree);					
				logger.debug("task_pool('"+typeName+"'): set size="+degree);					
			}
			else if(te.getCorePoolSize() > degree) {
				te.setCorePoolSize(degree);
				te.setMaximumPoolSize(degree);  // без установки MaxPoolSize пул не уменьшается				
				logger.debug("task_pool('"+typeName+"'): set size="+degree);
			}
			else
				logger.debug("task_pool('"+typeName+"'): same size");
								
		}		
	}

	@Override
	public void postRun() {
		
	}

	@Override
	public void doTask() {
		List<AzCalcData> dataList = azCalcDao.getTask(GlobalConfig.HOST_HAME);
		logger.debug("Task to execute: "+dataList.size());
		for(AzCalcData data : dataList) 
			if(taskExecutorMap.containsKey(data.getTypeName())) {
				AzCalcTask calcTask = azTaskConf.getAzCalcTask(data);
				taskExecutorMap.get(data.getTypeName()).execute(calcTask);
			}
			else {
				logger.warn("Сброс статуса задания (thread pool не инициализирован): taskType='"+data.getTypeName()+"', id_task="+data.getIdTask());
				azCalcDao.resetTaskStatus(data.getIdTask(), GlobalConfig.HOST_HAME);
			}
	}

	@Override
	public void onError(Throwable caught) {
		logger.error(CommonUtils.getStackTrace(caught));
	}
	
	public AzCalcDao getAzCalcDao() {
		return azCalcDao;
	}

	public void setAzCalcDao(AzCalcDao azCalcDao) {
		this.azCalcDao = azCalcDao;
	}

	public Map<String, ThreadPoolExecutor> getTaskExecutorMap() {
		return taskExecutorMap;
	}

	public void setTaskExecutorMap(Map<String, ThreadPoolExecutor> taskExecutorMap) {
		this.taskExecutorMap = taskExecutorMap;
	}

}
