package ru.corp.az.azrun.common.taskx;

/*
 * все методы должны быть потокобезопасными
 */
public interface ManagedTask extends Comparable<ManagedTask> {

	void           start() throws OperationNotAllowed;
	void           stop()  throws OperationNotAllowed;
	void           onError(Throwable caught);
	TaskPool       getTaskPool();
	void           setTaskPool(TaskPool taskPool);
	TaskDescriptor getTaskDescriptor();
	
}
