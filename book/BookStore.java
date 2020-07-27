package a00.book;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import a00.data.util.ApplicationException;
import a00.data.util.BookOptions;
import a00.database.Database;
import a00.database.dao.BookDao;
import a00.database.dao.CustomerDao;
import a00.database.dao.PurchaseDao;
import a00.database.dao.TablesManager;
import a00.ui.MainFrame;

/**
 * Project: Book
 * File: BookStore.java
 * Date: May, 2020
 */

/**
 * @author Clara Fok, A00
 *
 */
public class BookStore {

	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	
	private static Database db = null;

	
	//table names
	private static final String CUSTOMER_TABLE_NAME = "A00_Customers";
	private static final String BOOK_TABLE_NAME = "A00_Books";
	private static final String PURCHASE_TABLE_NAME = "A00_Purchases";



	static {
		configureLogging();
	}
	private static final Logger LOG = LogManager.getLogger();

	/**
	 * BookStore Constructor. Processes the commandline arguments ex. -c -j -t
	 * 
	 * @throws ApplicationException
	 * @throws ParseException
	 */
	public BookStore(String[] args) throws ApplicationException, ParseException {
		LOG.info("Created BookStore");
		BookOptions.process(args);
	}

	/**
	 * Entry point to BookStore
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Instant startTime = Instant.now();
		LOG.info(startTime);

		try {			
			BookStore book = new BookStore(args);						
			book.run();
			
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
		}

		Instant endTime = Instant.now();
		LOG.info(endTime);
		LOG.info(String.format("Duration: %d ms", Duration.between(startTime, endTime).toMillis()));
		LOG.info("Books is running");
	}



	/**
	 * Connects to database 
	 */
	private void run() {
				
		try {
			
			db = new Database();
			db.getConnection();
			
			CustomerDao cdao = new CustomerDao(db);
			PurchaseDao pdao = new PurchaseDao(db);
			BookDao bdao = new BookDao(db);
			
			createUI(cdao, pdao, bdao, db);
			
			/*
			 * check if tables exist in database. If not, create tables and add data to tables. 
			 * For the purposes of this project, all three tables would either be dropped or created simultaneously
			 */
			if(!db.tableExists(CUSTOMER_TABLE_NAME) && !db.tableExists(BOOK_TABLE_NAME) && !db.tableExists(PURCHASE_TABLE_NAME)) {
				TablesManager.createAllTables();
				TablesManager.addAllDataToTable();
			}
			
		} catch (ApplicationException e) {
			LOG.error(e.getMessage(), e);
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
		}

	}
	

	public static void createUI(CustomerDao customerDao, PurchaseDao purchaseDao, BookDao bookDao, Database db) {
			
			try {
			    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			        if ("Nimbus".equals(info.getName())) {
			            UIManager.setLookAndFeel(info.getClassName());
			            break;
			        }
			    }
			} catch (Exception e) {
			    LOG.error("Nimbus not available", e);
			}
			// create and show the user interface
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						MainFrame mainFrame = new MainFrame(customerDao, bookDao, purchaseDao, db);
						mainFrame.setVisible(true);
					} catch (Exception e) {
						LOG.error(e.getMessage());
					}
				}
			});
	}
	
	
	/**
	 * Configures log4j2 from the external configuration file specified in
	 * LOG4J_CONFIG_FILENAME. If the configuration file isn't found then log4j2's
	 * DefaultConfiguration is used.
	 */
	private static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			System.out.println(String.format(
					"WARNING! Can't find the log4j logging configuration file %s; using DefaultConfiguration for logging.",
					LOG4J_CONFIG_FILENAME));
			Configurator.initialize(new DefaultConfiguration());
		}
	}

}
