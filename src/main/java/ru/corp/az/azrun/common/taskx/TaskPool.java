package ru.corp.az.azrun.common.taskx;

import java.util.List;

/*
 * все методы должны быть потокобезопасными
 */
public interface TaskPool {
	
	void              add(ManagedTask task);
	void              remove(ManagedTask task);
	List<ManagedTask> getAll();
	ManagedTask       get(Integer id);
	
}
