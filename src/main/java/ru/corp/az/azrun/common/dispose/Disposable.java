package ru.corp.az.azrun.common.dispose;

public abstract class Disposable implements Comparable<Disposable> {

	private DisposePriority  priority = DisposePriority.MEDIUM;
		
	public abstract void dispose() throws Exception;
	
	@Override
	public int compareTo(Disposable d) {		
		return this.priority.ordinal() - d.priority.ordinal();
	}

	public DisposePriority getPriority() {
		return priority;
	}

	public void setPriority(DisposePriority priority) {
		this.priority = priority;
	}
}
