package ru.corp.az.azrun.common.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.corp.az.azrun.common.dispose.Disposable;
import ru.corp.az.azrun.common.dispose.DisposeManager;
import ru.corp.az.azrun.common.dispose.DisposePriority;

@Component
public class ConnPoolDispose extends Disposable {
	
	private DisposeManager disposeManager;
	
	@Autowired
	@Qualifier("connPoolUtils")	
	private ConnPoolUtils  connPoolUtils;
	
	public ConnPoolDispose() {
		setPriority(DisposePriority.LOW);
	}

	@Override
	public void dispose() throws Exception {
		connPoolUtils.destroyConnPool();	
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

	public ConnPoolUtils getConnPoolUtils() {
		return connPoolUtils;
	}

	public void setConnPoolUtils(ConnPoolUtils connPoolUtils) {
		this.connPoolUtils = connPoolUtils;
	}	
}
