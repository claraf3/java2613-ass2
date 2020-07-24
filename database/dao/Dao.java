package a00.database.dao;


/**
 * Abstract Dao
 * 
 * @author scirka
 *
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.util.ApplicationException;
import a00.data.util.Logging;
import a00.database.Database;



public abstract class Dao {

	protected final Database database;
	protected final String tableName;

	private static Logger LOG;
	
	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(Dao.class);
	}

	protected Dao(Database database, String tableName) {
		this.database = database;
		this.tableName = tableName;
	}

	public abstract void create() throws ApplicationException;

	protected void create(String sql) throws ApplicationException, SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			LOG.debug(sql);
			statement.executeUpdate(sql);
			LOG.debug("table created");
		} finally {
			close(statement);
		}
	}

	protected void add(String sql) throws ApplicationException, SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			LOG.debug(sql);
			statement.executeUpdate(sql);
		} finally {
			close(statement);
		}
	}

	public void drop() throws ApplicationException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String sql = "drop table " + tableName;
			LOG.debug(sql);
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}  finally {
			close(statement);
		}
	}

	protected void close(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
