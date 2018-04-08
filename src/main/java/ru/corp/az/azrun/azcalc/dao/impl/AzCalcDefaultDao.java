package ru.corp.az.azrun.azcalc.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.corp.az.azrun.azcalc.dao.AzCalcData;
import ru.corp.az.azrun.azcalc.dao.AzParallelDegree;
import ru.corp.az.azrun.azcalc.dao.api.AzCalcDao;
import ru.corp.az.azrun.azcalc.dao.api.AzCalcExtDao;
import ru.corp.az.azrun.common.GlobalConfig;

@Repository("azCalcDao")
@Transactional
public class AzCalcDefaultDao implements AzCalcDao {

	@Autowired
	@Qualifier("azCalcExtDao")	
	private AzCalcExtDao azCalcExtDao;	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@SuppressWarnings ("unchecked")
	public List<AzCalcData> getTask(String node) {
		Session session = sessionFactory.getCurrentSession();
		Query   q = session.getNamedQuery("sql-getTask");
		q.setParameter("p_node", GlobalConfig.HOST_HAME);
		return q.list();
	}

	@Override
	@SuppressWarnings ( "unchecked" )
	public List<AzParallelDegree> getParallelDegree(String node) {		
		Session session = sessionFactory.getCurrentSession();
		Query   q = session.getNamedQuery("sql-getParallelDegree");
		q.setParameter("p_node", GlobalConfig.HOST_HAME);
		return q.list();
	}

	@Override
	public void nodeMon(String node) {
		azCalcExtDao.nodeMon(node);
	}

	@Override
	public void runAzCalc(AzCalcData azCalcData) {		
		azCalcExtDao.runAzCalc(azCalcData);		
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void resetTaskStatus(Long idTask, String node) {
		azCalcExtDao.resetTaskStatus(idTask, node);		
	}

	@Override
	public void setTaskStatusJob(boolean enabled) {
		azCalcExtDao.setTaskStatusJob(enabled);		
	}	

}
