/**
 * Project: A00_ass2
 * File: TablesManager.java
 * Date: Jun. 22, 2020
 * Time: 5:41:36 p.m.
 */
package a00.database.dao;

import java.util.Collection;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.AllData;
import a00.data.Book;
import a00.data.Customer;
import a00.data.Purchase;
import a00.data.util.ApplicationException;
import a00.data.util.Logging;
import a00.database.Database;

/**
 * @author Clara Fok, A00
 * Manages the creation, insertion, and dropping for all three tables by calling DAO methods
 */
public class TablesManager {

	private static Database db = null;
	
	//dao objects
	private static CustomerDao customerDao = null;
	private static BookDao bookDao = null;
	private static PurchaseDao purchaseDao = null;
	
	//table names
	private static final String CUSTOMER_TABLE_NAME = "A00_Customers";
	private static final String BOOK_TABLE_NAME = "A00_Books";
	private static final String PURCHASE_TABLE_NAME = "A00_Purchases";
	
	private static final Logger LOG;
	
	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(TablesManager.class);
	}
	
	
	
	//Method to call loadData and create customer, book, and purchase table in database
	public static void createAllTables() throws ApplicationException, NullPointerException{
		LOG.debug("Creating all three tables");
		
		db = new Database();
		customerDao = new CustomerDao(db);
		bookDao = new BookDao(db);
		purchaseDao = new PurchaseDao(db);
		
		customerDao.create();;
		bookDao.create();
		purchaseDao.create();		
	}
	
	
	//method to add data to all three tables
	public static void addAllDataToTable() throws ApplicationException, NullPointerException {
		
		LOG.debug("Add data to all three tables");
		
		AllData.loadData();  //parse data from csv files
		
		db = new Database();
		customerDao = new CustomerDao(db);
		bookDao = new BookDao(db);
		purchaseDao = new PurchaseDao(db);
		
		addCustomerData();
		addBookData();
		addPurchaseData();
	}
		
	
	//adds customer data to customers table
	private static void addCustomerData() throws ApplicationException, NullPointerException {
		
		Map<Integer, Customer> map = AllData.getCustomers();
		Collection<Customer> customers = map.values();
		for(Customer c : customers) {
			customerDao.addCustomer(c);
		}						
	}
	
	//adds book data to book table
	private static void addBookData() throws ApplicationException , NullPointerException{
		
		Map<Integer, Book> map = AllData.getBooks();
		Collection<Book> books = map.values();
		for(Book b : books) {
			bookDao.addBook(b);
		}						
	}
	
	//add purchase data to purchase tables
	private static void addPurchaseData() throws ApplicationException , NullPointerException{
		
		Map<Integer, Purchase> map = AllData.getPurchases();
		Collection<Purchase> purchases = map.values();
		for(Purchase p : purchases) {
			purchaseDao.addPurchase(p);
		}
	}
	
	//method to drop all tables
	public static void dropAllTables() throws ApplicationException {
		LOG.debug("Dropping Tables");
		
		db = new Database();
		db.getConnection();
		
	try {
			if(db.tableExists(PURCHASE_TABLE_NAME)) {
				purchaseDao = new PurchaseDao(db);
				purchaseDao.drop();
				LOG.debug(String.format("Table dropped: %s", PURCHASE_TABLE_NAME));
			}
			if(db.tableExists(CUSTOMER_TABLE_NAME)) {
				customerDao = new CustomerDao(db);
				customerDao.drop();
				LOG.debug(String.format("Table dropped: %s", CUSTOMER_TABLE_NAME));
			} 
			
			if(db.tableExists(BOOK_TABLE_NAME)) {
				bookDao = new BookDao(db);
				bookDao.drop();
				LOG.debug(String.format("Table dropped: %s", BOOK_TABLE_NAME));
			}

		} catch (ApplicationException e) {
			LOG.error("Eror while dropping table", e);
			throw new ApplicationException(e);
		}

	} 
	
	
}
