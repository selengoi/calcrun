package ru.corp.az.azrun.common.taskx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GenericTaskPool implements TaskPool {

	private ConcurrentHashMap<Integer, ManagedTask> taskMap = new ConcurrentHashMap<>();
	
	@Override
	public void add(ManagedTask task) {
		taskMap.put(task.getTaskDescriptor().getId(), task);		
	}

	@Override
	public void remove(ManagedTask task) {
		taskMap.remove(task.getTaskDescriptor().getId());		
	}

	@Override
	public List<ManagedTask> getAll() {
		return new ArrayList<ManagedTask>(taskMap.values());
	}

	@Override
	public ManagedTask get(Integer id) {
		return taskMap.get(id);
	}

}
