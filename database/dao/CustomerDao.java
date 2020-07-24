/**
 * Project: A00_lab07
 * File: CustomerDao.java
 * Date: Jun. 8, 2020
 * Time: 1:09:21 p.m.
 */
package a00.database.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Customer;
import a00.data.util.ApplicationException;
import a00.data.util.Logging;
import a00.database.Database;

/**
 * @author Clara Fok, A00
 *
 */
public class CustomerDao extends Dao {
	
	private static Logger LOG;
	
	private static final String CUSTOMER_TABLE_NAME = "A00_Customers";
	
	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(CustomerDao.class);
	}


	/**
	 * Constructor for CustomerDao
	 * Calls super constructor
	 */
	public CustomerDao(Database database) {
		super(database, CUSTOMER_TABLE_NAME);

	}

	/*
	 * @Override create() method from Dao
	 */
	@Override
	public void create() throws ApplicationException {
		LOG.info("Creating " + CUSTOMER_TABLE_NAME);
		String sql = String.format(
				"CREATE TABLE %s(" //0
				+ "%s int, "	//1
				+ "%s VARCHAR(20), "//2
				+ "%s VARCHAR(20), "//3
				+ "%s VARCHAR(40), "//4
				+ "%s VARCHAR(40), "//5
				+ "%s VARCHAR(10), "//6
				+ "%s VARCHAR(15) not null, "//7
				+ "%s VARCHAR(40), "//8
				+ "%s DATE, "//9
				+ "primary key(%s) )", //10
				CUSTOMER_TABLE_NAME, //0
				Fields.CUSTOMER_ID.getName(), //1 
				Fields.FIRST_NAME.getName(), //2
				Fields.LAST_NAME.getName(), //3
				Fields.STREET.getName(), //4
				Fields.CITY.getName(), //5
				Fields.POSTAL_CODE.getName(), //6 
				Fields.PHONE.getName(), //7
				Fields.EMAIL.getName(), //8
				Fields.JOIN_DATE.getName(), //9 
				Fields.CUSTOMER_ID.getName()); //10
		
		try {
			super.create(sql);
		} catch (SQLException e) {			
			throw new ApplicationException(e);
		}
	}
	

	
	
	/*
	 * method to insert customer info into table
	 * 
	 * @param c customer to be added
	 */
	public void addCustomer(Customer c) throws ApplicationException {
		LOG.info("Adding data to " + CUSTOMER_TABLE_NAME);
		String sql = String.format("INSERT INTO %s VALUES ("
				+ "'%d', "
				+ "'%s', "
				+ "'%s', "
				+ "'%s', "
				+ "'%s', "
				+ "'%s', "
				+ "'%s', "
				+ "'%s', "
				+ "'%s') ",
				CUSTOMER_TABLE_NAME, c.getCustomerID(), c.getFirstName(), c.getLastName(), c.getStreetName(), c.getCity(), c.getPostalCode(), c.getPhoneNumber(), c.getEmail(), c.getJoinDate());

		try {
			super.add(sql);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} 			
	}
	

	/*
	 * method to update Customer info
	 * 
	 * @param customer_id id of customer to be updated
	 */
	public void updateCustomer(Customer customer) throws ApplicationException {
		
		Statement stmt = null;
		
		try {
			Connection conn = database.getConnection();
			stmt = conn.createStatement();
			
			String sql = String.format("UPDATE %s SET "
					+ "%s = '%s', " //1
					+ "%s = '%s', " //2
					+ "%s = '%s', " //3
					+ "%s = '%s', " //4
					+ "%s = '%s', " //5
					+ "%s = '%s', " //6
					+ "%s = '%s', " //7
					+ "%s = '%s', " //8
					+ "%s = '%s' " //9
					+ "WHERE %s = '%s'", //10,
					CUSTOMER_TABLE_NAME,
					Fields.CUSTOMER_ID.getName(), customer.getCustomerID(),//1 
					Fields.FIRST_NAME.getName(), customer.getFirstName(),//2
					Fields.LAST_NAME.getName(), customer.getLastName(),//3
					Fields.STREET.getName(), customer.getStreetName(),//4
					Fields.CITY.getName(), customer.getCity(),//5
					Fields.POSTAL_CODE.getName(), customer.getPostalCode(),//6 
					Fields.PHONE.getName(), customer.getPhoneNumber(),//7
					Fields.EMAIL.getName(), customer.getEmail(),//8
					Fields.JOIN_DATE.getName(), customer.getJoinDate(),//9 
					Fields.CUSTOMER_ID.getName(), customer.getCustomerID() //10
					);
			LOG.debug(sql);
			int rowsUpdated = stmt.executeUpdate(sql);
			LOG.info("Customer Rows updated: " + rowsUpdated);
		} catch (NullPointerException e) {
			throw new ApplicationException(e);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			close(stmt);
		}	
	}
	
	/*
	 * method to delete customer 
	 * 
	 * @param customer the customer to be deleted
	 */
	public void deleteCustomer(Customer customer) throws ApplicationException{
		Statement stmt = null;
		
		try {
			Connection conn = database.getConnection();
			stmt = conn.createStatement();
			
			String sql = String.format("DELETE FROM %s WHERE %s='%s'", CUSTOMER_TABLE_NAME, Fields.CUSTOMER_ID.getName(), customer.getCustomerID());
			LOG.debug(sql);
			
			int rowsDeleted = stmt.executeUpdate(sql);
			LOG.debug("Customer Rows deleted: " + rowsDeleted);
			
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} catch (NullPointerException e) {
			throw new ApplicationException(e);
		} finally {
			close(stmt);
		}
	}
	
	
	/*
	 * method to retrieve a list of customer IDs from table
	 */
	public List<String> getIds() throws ApplicationException{
		
		LOG.debug("Getting customer IDs");
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<String> idList = new ArrayList<String>();
		
		try {
			connection = database.getConnection();
			stmt = connection.createStatement();
			
			String sql = String.format("SELECT %s FROM %s", Fields.CUSTOMER_ID.getName(), CUSTOMER_TABLE_NAME);
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				idList.add(rs.getString(Fields.CUSTOMER_ID.getName()));
			}
			
			return idList;
			
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			close(stmt);
		}
		
	}
	
	/*
	 * method to select data based on student id
	 * 
	 * @param customer_id the id of customer to search for
	 */
	public Customer getCustomerByID(int customer_id) throws ApplicationException{
		LOG.debug("Reading customer by ID");
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		Customer c = null;
		
		try {
			conn = database.getConnection();
			stmt = conn.createStatement();
			
			String sql = String.format("SELECT * FROM %s WHERE %s=%d", CUSTOMER_TABLE_NAME, Fields.CUSTOMER_ID.getName(), customer_id);
			LOG.debug(sql);
			resultSet = stmt.executeQuery(sql);			
			
			int count = 0;
			while(resultSet.next()) {
				count++;
				if(count > 1) {
					throw new ApplicationException("Expected 1 customer but got more than 1");
				}
				
				String fName = resultSet.getString(Fields.FIRST_NAME.getName());
				String LName = resultSet.getString(Fields.LAST_NAME.getName());
				String street = resultSet.getString(Fields.STREET.getName());
				String city = resultSet.getString(Fields.CITY.getName());
				String pc = resultSet.getString(Fields.POSTAL_CODE.getName());
				String phone = resultSet.getString(Fields.PHONE.getName());
				String email = resultSet.getString(Fields.EMAIL.getName());
				int year = Integer.parseInt(resultSet.getString(Fields.JOIN_DATE.getName()).substring(0,4));
				int month = Integer.parseInt(resultSet.getString(Fields.JOIN_DATE.getName()).substring(5,7));
				int day = Integer.parseInt(resultSet.getString(Fields.JOIN_DATE.getName()).substring(8));
				LocalDate joinDate = LocalDate.of(year, month, day);
						
				c = new Customer.Builder(customer_id, phone).setFirstName(fName).setLastName(LName).setStreetName(street).setCity(city).setPostalCode(pc).setEmail(email).setJoinDate(joinDate).build();
			}
			
			return c;
			
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			close(stmt);
		}				
	}
	
//	public String getNameByID(int customer_id) throws ApplicationException{
//		LOG.debug("Getting customer name by Customer ID");
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet resultSet = null;
//		String title;
//		
//		try {
//			conn = database.getConnection();
//			stmt = conn.createStatement();
//			
//			String sql = String.format("SELECT %s, %s FROM %s WHERE %s=%d", Fields.FIRST_NAME.getName(), Fields.LAST_NAME.getName(), CUSTOMER_TABLE_NAME, Fields.CUSTOMER_ID.getName(), customer_id);
//			LOG.debug(sql);
//			resultSet = stmt.executeQuery(sql);	
//			title = resultSet.getString(1);
//
//			return title;
//			
//		} catch (SQLException e) {
//			throw new ApplicationException(e);
//		} 
//	}
	
	
	//method to make queries on customer database
		
	public ResultSet makeQuery(String sql) throws ApplicationException {
		
		LOG.debug("Querying Customer table");
		
		Connection conn = database.getConnection();
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
			LOG.debug(sql);
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} 
	
	} 
		
		
		
		
	
	//method to count customer records in table
	public int count() throws ApplicationException {
		
		LOG.debug("Counting customer records");
		
		Connection conn = database.getConnection();
		Statement stmt = null;
		int count = 0;
		
		try {
			stmt = conn.createStatement();	
			String sql = String.format("SELECT COUNT(*) FROM %s AS count", CUSTOMER_TABLE_NAME);
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				count = rs.getInt(1);
			}
			return count;

		} catch (SQLException e) {
			throw new ApplicationException("Fail to query customer table");
		} finally {
			close(stmt);
		}
		
	}


	public enum Fields {

		CUSTOMER_ID("customer_id", "long", 10, 1), 
		FIRST_NAME("firstName", "VARCHAR", 17, 2),
		LAST_NAME("lastName", "VARCHAR", 17, 3), 
		STREET("streetName", "VARCHAR", 25, 4), 
		CITY("city", "VARCHAR", 17, 5),
		POSTAL_CODE("postalCode", "VARCHAR", 17, 6), 
		PHONE("phoneNumber", "VARCHAR", 17, 7),
		EMAIL("email", "VARCHAR", 32, 8), 
		JOIN_DATE("joinDate", "DATE", 14, 9);

		private final String name;
		private final String dataType;
		private final int size;
		private final int column;

		Fields(String name, String dataType, int size, int column) {
			this.name = name;
			this.dataType = dataType;
			this.size = size;
			this.column = column;
		}

		public String getDataType() {
			return dataType;
		}

		public String getName() {
			return name;
		}

		public int getSize() {
			return size;
		}

		public int getColumn() {
			return column;
		}

	}

}
