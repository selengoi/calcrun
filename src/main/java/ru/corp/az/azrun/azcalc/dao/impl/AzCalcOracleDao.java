package ru.corp.az.azrun.azcalc.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.corp.az.azrun.azcalc.dao.AzCalcData;
import ru.corp.az.azrun.azcalc.dao.api.AzCalcExtDao;
import ru.corp.az.azrun.common.GlobalConfig;

@Component("azCalcExtDao")
public class AzCalcOracleDao implements AzCalcExtDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void nodeMon(String node) {
		Session session = sessionFactory.getCurrentSession();
		final String sql = session.getNamedQuery("sql-nodeMon").getQueryString();
		
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				CallableStatement call = conn.prepareCall(sql);
				call.setString(1, GlobalConfig.HOST_HAME);				
				call.execute();
				call.close();
			}			
		});
	}

	@Override
	public void runAzCalc(final AzCalcData azCalcData) {
		Session session = sessionFactory.getCurrentSession();
		final String sql = session.getNamedQuery("sql-runAzCalc").getQueryString();
		
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				CallableStatement call = conn.prepareCall(sql);
				call.setLong(1, azCalcData.getIdTask());
				if(azCalcData.getIdTrace() == null)
					call.setNull(2,  java.sql.Types.NULL);
				else					
					call.setLong(2, azCalcData.getIdTrace());
				call.execute();
				call.close();
			}			
		});		
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void resetTaskStatus(final Long idTask, final String node) {
		Session session = sessionFactory.getCurrentSession();
		final String sql = session.getNamedQuery("sql-resetTaskStatus").getQueryString();
		
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				CallableStatement call = conn.prepareCall(sql);
				call.registerOutParameter(1, java.sql.Types.NUMERIC);
				call.setLong(2, idTask);
				call.setString(3, node);
				call.execute();
				call.close();
			}			
		});	
	}

	@Override
	public void setTaskStatusJob(boolean enabled) {
		Session session = sessionFactory.getCurrentSession();
		final String sql = enabled ? session.getNamedQuery("sql-startTaskStatusJob").getQueryString() :
			                         session.getNamedQuery("sql-stopTaskStatusJob").getQueryString();
		
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				Statement call = conn.createStatement();
				call.execute(sql);
				call.close();
			}			
		});	
	}
}
