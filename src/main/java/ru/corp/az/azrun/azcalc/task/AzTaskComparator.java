package ru.corp.az.azrun.azcalc.task;

import java.util.Comparator;

import ru.corp.az.azrun.common.taskx.GenericManagedTask;

public class AzTaskComparator implements Comparator<GenericManagedTask>{

	@Override
	public int compare(GenericManagedTask o1, GenericManagedTask o2) {
		int result = 0;
		if(o1 instanceof AzCalcTask && o2 instanceof AzCalcTask) {
			result = ((AzCalcTask)o2).getAzCalcData().getPriority()-((AzCalcTask)o1).getAzCalcData().getPriority();
			if(result == 0)
				//PriorityBlockingQueue не гарантирует FIFO при одинаковом приоритете
				//javadoc: Operations on this class make no guarantees about the ordering of elements with equal priority. 
				result = o1.getTaskDescriptor().getId() - o2.getTaskDescriptor().getId();
		}
		
		return result;
	}
	
}
