package ru.corp.az.azrun.common.log4j12;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;

import ru.corp.az.azrun.common.CommonUtils;
import ru.corp.az.azrun.common.db.ApplicationDataSource;

public class CustomJDBCAppender extends JDBCAppender {

	private Logger logger = Logger.getLogger(CustomJDBCAppender.class);
	
	@Override
	protected void closeConnection(Connection conn) {
		try {
			if(conn == null)
				return;
			conn.close();
		} catch (SQLException e) {
			logger.error(CommonUtils.getStackTrace(e));
		}
	}
	
	@Override
	protected Connection getConnection() {
		Connection conn = null;
		try {
			conn = ApplicationDataSource.getLoggingDataSource().getConnection();
		} catch (SQLException e) {
			logger.error(CommonUtils.getStackTrace(e));
		}
		return conn;
	}	
	
    @Override
    public void append(LoggingEvent event) {
        LoggingEvent modifiedEvent = new LoggingEvent(event.getFQNOfLoggerClass(), event.getLogger(), 
        		                                      event.getTimeStamp(), event.getLevel(), 
        		                                      event.getMessage().toString().replaceAll("'", "''"),
                                                      event.getThreadName(), event.getThrowableInformation(), 
                                                      event.getNDC(), event.getLocationInformation(),
                                                      event.getProperties());

        super.append(modifiedEvent);
    }	
		
}
