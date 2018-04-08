package ru.corp.az.azrun.azcalc.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.corp.az.azrun.azcalc.dao.AzCalcData;
import ru.corp.az.azrun.azcalc.dao.api.AzCalcDao;
import ru.corp.az.azrun.common.CommonUtils;
import ru.corp.az.azrun.common.GlobalConfig;
import ru.corp.az.azrun.common.taskx.GenericManagedTask;
import ru.corp.az.azrun.common.taskx.ManagedTask;
import ru.corp.az.azrun.common.taskx.OperationNotAllowed;
import ru.corp.az.azrun.common.taskx.TaskStatus;
import ru.corp.az.azrun.common.taskx.TaskType;

public class AzCalcTask extends GenericManagedTask {

	private Logger logger = LoggerFactory.getLogger(AzCalcTask.class);
	private AzCalcData azCalcData = null;
	private AzCalcDao azCalcDao;				
	
	public AzCalcTask() {
		super();		
	}
	
	@Override
	public String toString() {
		if(azCalcData != null)
			return "id_task="+Long.toString(azCalcData.getIdTask()); //azCalcData.getDcName()+"-"+azCalcData.getWsName()+"-"+azCalcData.getAlgName();
		else
			return super.toString();
	}
	
	@Override
	public void doTask() {
		boolean stopped = false;
		
		for(ManagedTask t : taskPool.getAll() ) 
			if(t.getTaskDescriptor().getLabel().equals(AzTaskNames.AZ_QUEUE_TASK)) {		
				stopped = t.getTaskDescriptor().getStatus() == TaskStatus.STOPPED ? true : false;
				continue;
			}
		
		if(stopped) {			
			azCalcDao.resetTaskStatus(azCalcData.getIdTask(), GlobalConfig.HOST_HAME);
			logger.debug("Сброс статуса (очередь остановлена): " + toString());
		}
		else {
			logger.debug("Запуск расчета: " + toString());
			azCalcDao.runAzCalc(azCalcData);
			logger.debug("Расчет завершен: " + toString());
		}
	}
	
	@Override
	public void onError(Throwable caught) {
		logger.error("id_task="+azCalcData.getIdTask()+": "+CommonUtils.getStackTrace(caught));
		azCalcDao.resetTaskStatus(azCalcData.getIdTask(), GlobalConfig.HOST_HAME);
	}

	@Override
	public void preRun() {		
	}

	@Override
	public void postRun() {				
	}

	public AzCalcDao getAzCalcDao() {
		return azCalcDao;
	}

	public void setAzCalcDao(AzCalcDao azCalcDao) {
		this.azCalcDao = azCalcDao;
	}

	public AzCalcData getAzCalcData() {
		return azCalcData;
	}

	public void setAzCalcData(AzCalcData azCalcData) {
		this.azCalcData = azCalcData;
		taskDescriptor.setName(AzTaskNames.AZ_CALC_TASK+"_"+taskDescriptor.getId());
		taskDescriptor.setAction(toString());
		taskDescriptor.setType(TaskType.REGULAR);
		taskDescriptor.setLabel(AzTaskNames.AZ_CALC_TASK);
	}
	
	@Override
	public void start()  throws OperationNotAllowed {
		throw new OperationNotAllowed("Операция не поддерживается данным заданием");
	}
	
	@Override
	public void stop()  throws OperationNotAllowed {
		throw new OperationNotAllowed("Операция не поддерживается данным заданием");
	}	

}
