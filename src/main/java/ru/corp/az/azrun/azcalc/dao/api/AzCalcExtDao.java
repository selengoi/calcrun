package ru.corp.az.azrun.azcalc.dao.api;

import ru.corp.az.azrun.azcalc.dao.AzCalcData;

public interface AzCalcExtDao {

	void                   nodeMon(String node);
	void runAzCalc(AzCalcData azCalcData);
	void resetTaskStatus(Long idTask, String node);
	void setTaskStatusJob(boolean enabled);
		
}
