package ru.corp.az.azrun.common.dispose;

import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import ru.corp.az.azrun.common.CommonUtils;

@Component("disposeManager")
public class DisposeManager implements ApplicationListener<ContextClosedEvent>{

	private ArrayList<Disposable> disposableList = new ArrayList<>();
	private Logger logger = LoggerFactory.getLogger(DisposeManager.class);
	
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		try {
			Collections.sort(disposableList);
			for(Disposable d : disposableList)
				try {
					d.dispose();
				}
				catch(Throwable e) {
					logger.error(CommonUtils.getStackTrace(e));
				}				
		}
		catch(Throwable e) {
			logger.error(CommonUtils.getStackTrace(e));
		}
	}

	public void addDisposable(Disposable d) {
		disposableList.add(d);
	}

}
