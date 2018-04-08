package ru.corp.az.azrun.common.db;

public interface ConnPoolUtils {
	
	void destroyConnPool() throws Exception;	
	void startConnPool()   throws Exception;	
	void stopConnPool()    throws Exception;  

}
