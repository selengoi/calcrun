package ru.corp.az.azrun.azcalc.task.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.corp.az.azrun.common.dispose.Disposable;
import ru.corp.az.azrun.common.dispose.DisposeManager;
import ru.corp.az.azrun.common.dispose.DisposePriority;

@Component
public class AzTaskDispose extends Disposable  {

	@Autowired
	@Qualifier("azTaskUtils")
	private AzTaskUtils azTaskUtils;
	
	private DisposeManager disposeManager;
	
	public AzTaskDispose() {
		setPriority(DisposePriority.MEDIUM);
	}
	
	@Override
	public void dispose() throws Exception {
		azTaskUtils.stopAzCalc();	
		azTaskUtils.shutdownSchedulers();
		azTaskUtils.shutdownThreadPools();
	}

	public DisposeManager getDisposeManager() {
		return disposeManager;
	}

	@Autowired
	@Qualifier("disposeManager")
	public void setDisposeManager(DisposeManager disposeManager) { 
		this.disposeManager = disposeManager;
		this.disposeManager.addDisposable(this);
	}

}
