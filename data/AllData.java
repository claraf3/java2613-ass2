/**
 * Project: A00_ass2
 * File: AllData.java
 * Date: Jun. 22, 2020
 * Time: 5:24:05 p.m.
 */
package a00.data;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.util.ApplicationException;
import a00.data.util.Logging;
import a00.io.BookReader;
import a00.io.BookReport;
import a00.io.CustomerReader;
import a00.io.PurchaseReader;

/**
 * @author Clara Fok, A00
 *
 */
public class AllData {
	
	private static Map<Integer, Book> books;
	private static Map<Integer, Customer> customers;
	private static Map<Integer, Purchase> purchases;
	
	private static final Logger LOG;

	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(BookReport.class);
	}
	
	//method to read all data files
	public static void loadData() throws ApplicationException{
		
		LOG.debug("reading data files");
		books = BookReader.readBookData();
		customers = CustomerReader.readCustomerData();
		purchases = PurchaseReader.readPurchaseData();	
		LOG.debug("data files loaded");
		
	}

	/**
	 * @return the books
	 */
	public static Map<Integer, Book> getBooks() {
		return books;
	}

	/**
	 * @return the customers
	 */
	public static Map<Integer, Customer> getCustomers() {
		return customers;
	}

	/**
	 * @return the purchases
	 */
	public static Map<Integer, Purchase> getPurchases() {
		return purchases;
	}
	
	
	

}
