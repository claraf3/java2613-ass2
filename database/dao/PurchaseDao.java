/**
 * Project: A00_ass2
 * File: PurchaseDao.java
 * Date: Jun. 22, 2020
 * Time: 1:26:26 p.m.
 */
package a00.database.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Purchase;
import a00.data.util.ApplicationException;
import a00.data.util.Logging;
import a00.database.Database;
import a00.io.BookReport;

/**
 * @author Clara Fok, A00
 *
 */
public class PurchaseDao extends Dao {

	private static final Logger LOG;

	private static final String PURCHASE_TABLE_NAME = "A00_Purchases";
	private static final String CUSTOMER_TABLE_NAME = "A00_Customers";
	private static final String BOOK_TABLE_NAME = "A00_Books";

	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(BookReport.class);
	}

	// constructor for PurchaseDao
	// @param database for database to connect to
	public PurchaseDao(Database database) {
		super(database, PURCHASE_TABLE_NAME);
	}

	// @Override create() from Dao class to create new purchase table
	@Override
	public void create() throws ApplicationException {

		LOG.info("Creating " + PURCHASE_TABLE_NAME);
		String sql = String.format(
				"CREATE TABLE %s (" //0
				+ "%s INT NOT NULL, "//1
				+ "%s INT not null, " //2
				+ "%s INT not null, " //3
				+ "%s FLOAT, " //4
				+ "primary key(%s), "
				+ "CONSTRAINT FK_PurchaseCustomer FOREIGN KEY(%s) REFERENCES %s(%s), "
				+ "CONSTRAINT FK_PurchaseBook FOREIGN KEY(%s) REFERENCES %s(%s))", //5
				PURCHASE_TABLE_NAME, //0
				Fields.PURCHASE_ID.getName(), //1 
				Fields.CUSOMTER_ID.getName(), //2
				Fields.BOOK_ID.getName(), //3
				Fields.PRICE.getName(), //4
				Fields.PURCHASE_ID.getName(),
				Fields.CUSOMTER_ID.getName(), CUSTOMER_TABLE_NAME, CustomerDao.Fields.CUSTOMER_ID.getName(),
				Fields.BOOK_ID.getName(), BOOK_TABLE_NAME, BookDao.Fields.ID.getName()); //5

		try {
			super.create(sql);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}

	}

	// method to add purchase records to table
	public void addPurchase(Purchase p) throws ApplicationException {
		LOG.info("Adding data to " + PURCHASE_TABLE_NAME);
		String sql = String.format("INSERT INTO %s VALUES (%d, %d, %d, %.2f)", PURCHASE_TABLE_NAME, p.getId(),
				p.getCustomer_id(), p.getBook_id(), p.getPrice());

		try {
			super.add(sql);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	// method to update purchase record
	public void updateBook(Purchase p) throws ApplicationException{
		
		Statement stmt = null;

		String sql = String.format("UPDATE %s SET %s = %d, %s = %d, %s = %d, %s = %.2f", PURCHASE_TABLE_NAME,
				Fields.PURCHASE_ID.getName(), p.getId(), Fields.CUSOMTER_ID.getName(), p.getCustomer_id(),
				Fields.BOOK_ID.getName(), p.getBook_id(), Fields.PRICE.getName(), p.getPrice());
		
		try {
			Connection conn = database.getConnection();
			stmt = conn.createStatement();
			int rowsUpdated = stmt.executeUpdate(sql);
			LOG.debug("Purchase rows updated: " + rowsUpdated);
		} catch (SQLException e) {
			throw new ApplicationException("Error - fail to update Purchase table");
		} catch (NullPointerException e) {
			throw new ApplicationException(e);
		} finally {
			close(stmt);
		}	

	}
	
	//method to delete purchase record
	public void deletePurchase(Purchase p) throws ApplicationException{
		
		Statement stmt = null;
		
		String sql = String.format("DELETE FROM %s WHERE %s = %d", PURCHASE_TABLE_NAME, Fields.PURCHASE_ID.getName(), p.getId());
		
		try {
			Connection conn = database.getConnection();
			stmt = conn.createStatement();
			int rowsDeleted = stmt.executeUpdate(sql);
			LOG.debug("Purchase rows deleted: " + rowsDeleted);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} catch (NullPointerException e) {
			throw new ApplicationException(e);
		} finally {
			close(stmt);
		}
	}
	
	//method to count purchase records in table
	public double purchaseTotal(int customer_id) throws ApplicationException {
		
		LOG.debug("Counting total purchases");
		
		Connection conn = database.getConnection();
		ResultSet rs = null;
		Statement stmt = null;
		double total = 0.00;
		String sql;
		
		try {
			stmt = conn.createStatement();	
			if(customer_id > 0) {
				sql = String.format("SELECT price FROM %s WHERE %s = %d",PURCHASE_TABLE_NAME, Fields.CUSOMTER_ID.getName(), customer_id);
			} else {
				sql = String.format("SELECT price FROM %s", PURCHASE_TABLE_NAME);
			}
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				Double p = rs.getDouble(1);
				total = total + p;				
			}
			return total;

		} catch (SQLException e) {
			throw new ApplicationException("Fail to query purchase table");
		} finally {
			close(stmt);
		}
		
	}
	
	//select purchase record by customer ID
	public ResultSet getPurchaseByCustomerID(int id) throws ApplicationException{
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs;
		
		try {
			conn = database.getConnection();
			stmt = conn.createStatement();
			
			String sql = String.format("SELECT * FROM %s WHERE %s = %d", PURCHASE_TABLE_NAME, Fields.CUSOMTER_ID.getName(), id);
			rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			throw new ApplicationException("Fail to retrieve Purchase record by customer ID: " + id);
		}			
	}
	
	
	
	//method to make queries on purchase database
	public ResultSet makeQuery(String sql) throws ApplicationException {
		
		LOG.debug("Querying Purchase table");
		
		Connection conn = database.getConnection();
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
			LOG.debug(sql);
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			throw new ApplicationException("Fail to query purchase table");
		} 
		
	}
			
	

	public enum Fields {

		PURCHASE_ID("id", "int", 10, 0), CUSOMTER_ID("customer_id", "int", 10, 0), BOOK_ID("book_id", "int", 10, 0),
		PRICE("price", "decimal", 10, 0);

		private final String name;
		private final String dataType;
		private final int size;
		private final int column;

		/**
		 * @param name
		 * @param dataType
		 * @param size
		 * @param column
		 */
		private Fields(String name, String dataType, int size, int column) {
			this.name = name;
			this.dataType = dataType;
			this.size = size;
			this.column = column;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the dataType
		 */
		public String getDataType() {
			return dataType;
		}

		/**
		 * @return the size
		 */
		public int getSize() {
			return size;
		}

		/**
		 * @return the column
		 */
		public int getColumn() {
			return column;
		}

	}

}
