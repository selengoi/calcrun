package ru.corp.az.azrun.common.db;

import oracle.ucp.UniversalConnectionPoolException;
import oracle.ucp.admin.UniversalConnectionPoolManager;
import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("connPoolUtils")
public class OracleUCPUtils implements ConnPoolUtils {
	
	private Logger logger = LoggerFactory.getLogger(OracleUCPUtils.class);
	
	@Override
	public void destroyConnPool() throws UniversalConnectionPoolException {
		UniversalConnectionPoolManager ucpManager = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
		for(String s : ucpManager.getConnectionPoolNames()) {
			ucpManager.destroyConnectionPool(s);
			logger.info("destroyConnectionPool: "+s);
		}		
	}
	
	@Override
	public void startConnPool() throws UniversalConnectionPoolException {
		UniversalConnectionPoolManager ucpManager = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
		try {
			for(String s : ucpManager.getConnectionPoolNames()) {
				ucpManager.startConnectionPool(s);
				logger.info("startConnectionPool: "+s);
			}
		}
		catch(UniversalConnectionPoolException e) {
			//повторный старт - Недопустимое состояние жизненного цикла. Проверьте состояние универсального пула соединений:45060
			if(e.getErrorCode() != 45060) 
				throw e;
		}
	}
	
	@Override
	public void stopConnPool() throws UniversalConnectionPoolException {
		UniversalConnectionPoolManager ucpManager = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
		for(String s : ucpManager.getConnectionPoolNames()) {
			ucpManager.stopConnectionPool(s);
			logger.info("stopConnectionPool: "+s);
		}
	}	
}
