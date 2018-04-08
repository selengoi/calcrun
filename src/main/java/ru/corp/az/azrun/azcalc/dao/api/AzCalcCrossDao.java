package ru.corp.az.azrun.azcalc.dao.api;

import java.util.List;

import ru.corp.az.azrun.azcalc.dao.AzCalcData;
import ru.corp.az.azrun.azcalc.dao.AzParallelDegree;

public interface AzCalcCrossDao {
	List<AzCalcData>       getTask(String node);
	List<AzParallelDegree> getParallelDegree(String node);
}
