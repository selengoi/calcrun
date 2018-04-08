package ru.corp.az.azrun.jmx;

import java.util.List;

import javax.management.MBeanException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import ru.corp.az.azrun.azcalc.task.utils.AzTaskUtils;
import ru.corp.az.azrun.common.db.ConnPoolUtils;


@ManagedResource(
        objectName="ru.corp.az.azrun:name=azRunJmx",
        description="")
public class AzRunJmxSpring implements AzRunJmx {
	
	@Autowired
	@Qualifier("connPoolUtils")
	private ConnPoolUtils    connPoolUtils;

	@Autowired
	@Qualifier("azTaskUtils")
	private AzTaskUtils azTaskUtils;	
	
	private Logger           rootLogger   = Logger.getRootLogger();
	private Logger           azCalcLogger = Logger.getLogger("ru.corp.az.azrun.azcalc");	
			
	@Override
	@ManagedAttribute(description="Список задач")
	public List<String> getTaskList() {
		return azTaskUtils.getTaskList();
	}		
		
	@Override
	@ManagedOperation(description="Остановить пул соединений")
	public void stopConnPool() throws MBeanException {
		try {
			connPoolUtils.stopConnPool();
		}
		catch(Exception e) {
			throw new MBeanException(new Exception(e.getMessage()), 
					                 e.getMessage());
		}		
	}

	@Override
	@ManagedOperation(description="Запустить пул соединений")
	public void startConnPool()  throws MBeanException {
		try {
			connPoolUtils.startConnPool();
		}
		catch(Exception e) {
			throw new MBeanException(new Exception(e.getMessage()), 
					                 e.getMessage());
		}		
	}	

	@Override
	@ManagedAttribute(description="Уровень логирования root")
	public String getRootLoggingLevel() {
		if(rootLogger.getLevel() == null)
			return "default";
        else					
        	return rootLogger.getLevel().toString();
	}

	@Override
	@ManagedAttribute
	public void setRootLoggingLevel(String level) {
		rootLogger.setLevel( Level.toLevel(level, Level.WARN) );
	}

	@Override
	@ManagedAttribute(description="Уровень логирования расчета")
	public String getAzCalcLoggingLevel() {
		if(azCalcLogger.getLevel() == null)
			return "default";
        else					
        	return azCalcLogger.getLevel().toString();
	}

	@Override
	@ManagedAttribute
	public void setAzCalcLoggingLevel(String level) {
		azCalcLogger.setLevel( Level.toLevel(level, Level.WARN) );		
	}

//	@Override
//	@ManagedOperation(description="Остановка очереди заданий")
//	public void stopAzQueue() throws MBeanException {
//		try {
//			azTaskUtils.stopAzQueue();
//		}
//		catch(Exception e) {
//			throw new MBeanException(new Exception(e.getMessage()), 
//					                 e.getMessage());
//		}		
//	}

	@Override
	@ManagedOperation(description="Запуск очереди заданий")
	public void startAzCalc() throws MBeanException {
		try {
			azTaskUtils.startAzCalc();
		}
		catch(Exception e) {
			throw new MBeanException(new Exception(e.getMessage()), 
					                 e.getMessage());
		}	
	}
	
	@Override
	@ManagedOperation(description="Остановка расчета")
	public void stopAzCalc() throws MBeanException {
		try {
			azTaskUtils.stopAzCalc();
		}
		catch(Exception e) {
			throw new MBeanException(new Exception(e.getMessage()), 
					                 e.getMessage());
		}	
	}	

}
