/**
 * Project: A00_ass2
 * File: Database.java
 * Date: Jun. 22, 2020
 * Time: 12:15:45 p.m.
 */
package a00.database;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.util.ApplicationException;
import a00.data.util.Logging;

/**
 * @author Clara Fok, A00
 *
 */
public class Database {
	
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	
	private static final Logger LOG;
	
	private Properties properties;
	private static final String DB_PROPERTIES_FILENAME = "db.properties";
	private static Connection connection;
	
	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(Database.class);
	}
	
	public Database() throws ApplicationException{
		loadDBProperties();
		driver = properties.getProperty("db.driver");
		
	}
	
	public void loadDBProperties() throws ApplicationException {
		
		LOG.debug("loading a00.database properties");
		properties = new Properties();
		File file = new File(DB_PROPERTIES_FILENAME);
		FileReader in = null;
		
		try {
			in = new FileReader(file);
			properties.load(in);
		} catch (IOException e) {
			throw new ApplicationException("Error reading db properties file");	
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new ApplicationException("Error closing db properties reader");
				}
			}
		}
	}
	
	//method to get connection to a00.database
	public Connection getConnection() throws ApplicationException {
		
		if(connection != null) {
			return connection;
		}
		
		try {
			connect();
		} catch (ClassNotFoundException e) {
			throw new ApplicationException("Error - cannot find error");
		} catch (SQLException e) {
			throw new ApplicationException("Error - cannot connect to a00.database");
		} 
		
		LOG.debug("Database connected");
		return connection;
	}
	
	//method to connect to a00.database
	private void connect() throws ClassNotFoundException, SQLException {
		
		LOG.debug("connecting to a00.database");
		
		//get db properties
		url = properties.getProperty("db.url");
		user = properties.getProperty("db.user");
		password = properties.getProperty("db.password");
		
		Class.forName(driver);
		connection = DriverManager.getConnection(url, user, password);
	
	}
	
	//method to shut down connection and reset connection to null
	public void shutDown() throws ApplicationException {
		
		if(connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				throw new ApplicationException("Error - cannot close connection");
			}
		}
	}
	
	//method to check if table exists
	public boolean tableExists(String tableName) throws ApplicationException {
		LOG.debug("Checking if table exists");
		
		DatabaseMetaData dbData = null;
		ResultSet rs = null;
		String name = null;
		
		try {
			dbData = connection.getMetaData();
			rs = dbData.getTables(connection.getCatalog(), "%", "%", null);
			
			while(rs.next()) {
				name = rs.getString("TABLE_NAME");
				if(name.equalsIgnoreCase(tableName)) {
					return true;
				}
			}
		} catch (SQLException e) {
			throw new ApplicationException("Error - fail to check if table exists");
		} catch (NullPointerException e) {
			throw new ApplicationException(e);
		} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					throw new ApplicationException("Error while closng result set");
				}
		}
		return false;
	}
	

}
