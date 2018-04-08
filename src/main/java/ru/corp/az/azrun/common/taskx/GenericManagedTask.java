package ru.corp.az.azrun.common.taskx;

import java.util.Date;

/*
 * run запускает preRun, doTask, postRun
 * предполагается, что перечисленный методы могут запускаться только в одном потоке
 */
public abstract class GenericManagedTask implements ManagedTask, Runnable {
	
	protected volatile TaskDescriptor taskDescriptor;
	protected volatile TaskPool       taskPool = null;	
	private static   int            lastId = 0;	
	protected Object taskStatusLock = this;
			
	public GenericManagedTask() {
		taskDescriptor = new TaskDescriptor();
		taskDescriptor.setId(getId());		
		taskDescriptor.setStatus(TaskStatus.QUEUE);
	}	
	
	public abstract void           preRun();
	public abstract void           postRun();
	public abstract void           doTask();	
	
	@Override 
    public boolean equals(Object obj) {
    	return taskDescriptor.equals(obj);
    }
    
    @Override
    public int hashCode() {
    	return taskDescriptor.hashCode();
    }	
	
    @Override
    public int compareTo(ManagedTask o) {
    	return this.getTaskDescriptor().getId() - o.getTaskDescriptor().getId();
    }
    
    private static synchronized int getId() {
    	return ++lastId;
    }
    
	@Override
	public TaskPool       getTaskPool() {
		return taskPool;
	}	
	
	@Override
	public void           setTaskPool(TaskPool taskPool) {
		this.taskPool = taskPool;
		this.taskPool.add(this);		
	}
	
	@Override
	public TaskDescriptor getTaskDescriptor() {
		return taskDescriptor;
	}
	
	@Override
	public void start() throws OperationNotAllowed {
		synchronized(taskStatusLock) {
			if(taskDescriptor.getStatus() == TaskStatus.FINISHED) 
				throw new OperationNotAllowed("Задание уже завершено");
			else if(taskDescriptor.getStatus() == TaskStatus.STOPPED) {
				
				taskDescriptor.setStatus(TaskStatus.QUEUE);
			}
			else
				throw new OperationNotAllowed("Задание уже запущено либо находитс¤ в очереди на обработку");
		}
	}

	@Override
	public void stop() throws OperationNotAllowed {
		synchronized(taskStatusLock) {
			if(taskDescriptor.getStatus() == TaskStatus.FINISHED) 
				throw new OperationNotAllowed("Задание уже завершено");			
			else if(taskDescriptor.getStatus() != TaskStatus.STOPPED) 
				taskDescriptor.setStatus(TaskStatus.STOPPED);
			else
				throw new OperationNotAllowed("«адание уже остановлено");
		}
	}	
	
	@Override
	public void           run() {
		synchronized(taskStatusLock) {			
			try {
				if(taskDescriptor.getStatus() != TaskStatus.STOPPED) {
					taskDescriptor.setStartTime(new Date());				
					taskDescriptor.setStatus(TaskStatus.RUNNING);
					preRun();
					doTask();
				}
			}
			catch(Throwable caught) {				
				onError(caught);
			}
			finally {
				try {					
					taskDescriptor.setLastStartTime(taskDescriptor.getStartTime());
					taskDescriptor.setStartTime(null);
					if(taskDescriptor.getStatus() != TaskStatus.STOPPED) {
						postRun();		
						if(taskDescriptor.getType() == TaskType.REGULAR) {
							taskDescriptor.setStatus(TaskStatus.FINISHED);
							taskPool.remove(this);
						}
						else if(taskDescriptor.getType() == TaskType.SCHEDULED)
							taskDescriptor.setStatus(TaskStatus.SCHEDULED);
						taskDescriptor.setEndTime(new Date());
					}
				}
				catch(Throwable caught) {
					onError(caught);
				}
			}
		}
	}

}
