/**
 * Project: A00_ass1
 * File: PurchaseReader.java
 * Date: May 27, 2020
 * Time: 2:27:45 p.m.
 */
package a00.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Purchase;
import a00.data.util.ApplicationException;
import a00.data.util.Logging;

/**
 * @author Clara Fok, A00
 *
 */
public class PurchaseReader {

	private static Logger LOG;
	
	private static final String PURCAHSE_DATA_FILENAME = "purchases.csv";

	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger();

	}

	/*
	 * Reads purchase data from csv file
	 * 
	 * Parses data and build Purchase objects
	 * 
	 * @return Map of Purchase objects
	 */

	public static Map<Integer, Purchase> readPurchaseData() throws ApplicationException {
		LOG.info("Enter PurchaseReader.readPurchaseData()");

		// read purchase raw data from CSV file and store to records
		File file = new File(PURCAHSE_DATA_FILENAME);
		FileReader in = null;
		Iterable<CSVRecord> records;

		try {
			in = new FileReader(file);
			LOG.debug("Reading" + file.getAbsolutePath());
			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Error - can't find data file: " + PURCAHSE_DATA_FILENAME);
		} catch (IOException e) {
			throw new ApplicationException("IOE exception while retrieving purchase data file");
		} catch (Exception e) {
			throw new ApplicationException("Error while reading purchase data file");
		} 
		
			
		

		/*
		 * create Map of purchases
		 * 
		 * Key = int for purchase id
		 * 
		 * Value = Purchase obj
		 */
		Map<Integer, Purchase> purchases = new HashMap<>();


		// create book objects from records and add to Map of books
		for (CSVRecord record : records) {

			int id = 0;
			int customer_id = 0;
			int book_id = 0;
			double price = 0.0;

			// validate and initialize purchase data members
			if (Integer.parseInt(record.get("id")) >= 0) {
				id = Integer.parseInt(record.get("id"));
			}

			if (Integer.parseInt(record.get("customer_id")) >= 0) {
				customer_id = Integer.parseInt(record.get("customer_id"));
			} else {
				LOG.error(String.format("Invalid customer id(%s) for purchase id(%s)",
						record.get("customer_id"), record.get("id")));
			}

			if (Integer.parseInt(record.get("book_id")) >= 0) {
				book_id = Integer.parseInt(record.get("book_id"));
			} else {
				LOG.error(String.format("Invalid book id(%s) for purchase id(%s)",
						record.get("book_id"), record.get("id")));
			}

			if (Double.parseDouble(record.get("price")) >= 0) {
				price = Double.parseDouble(record.get("price"));
			}

			// build new purchase obj
			Purchase p = new Purchase.Builder(customer_id, book_id).setId(id).setPrice(price).build();

			// add newly created Purchase obj to Map
			purchases.put(id, p);
			LOG.debug("Added purchase:" + p);
		}
		LOG.info("Exit PurchaseReader.readPurchaseData()");
		
		if(in != null) {
		try {
				LOG.debug("Closing customer data scanner");
				in.close();
			} catch (Exception e){
				throw new ApplicationException("Error while closing customer data scanner");
			}
		}
		return purchases;

	}
}
