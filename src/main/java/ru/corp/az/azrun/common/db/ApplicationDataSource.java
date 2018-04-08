package ru.corp.az.azrun.common.db;

import javax.sql.DataSource;

public class ApplicationDataSource {

	private static DataSource loggingDataSource;

	public static DataSource getLoggingDataSource() {
		return loggingDataSource;
	}

	public static void setLoggingDataSource(DataSource loggingDataSource) {
		ApplicationDataSource.loggingDataSource = loggingDataSource;
	}



	
}
