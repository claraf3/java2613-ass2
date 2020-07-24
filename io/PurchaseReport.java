/**
 * Project: A00_ass1
 * File: PurchaseReport.java
 * Date: Jun. 2, 2020
 * Time: 2:44:51 p.m.
 */
package a00.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Book;
import a00.data.Customer;
import a00.data.Purchase;
import a00.data.util.ApplicationException;
import a00.data.util.Logging;

/**
 * @author Clara Fok, A00
 *
 */
public class PurchaseReport {

	private static Logger LOG;
	private static final String OUTPUT_FILENAME = "purchases_report.txt";

	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(PurchaseReport.class);
	}

	public static void write(List<Purchase> purchaseList, Map<Integer, Customer> customerMap,
			
		Map<Integer, Book> bookMap, Boolean printTotal, String customerID) throws ApplicationException {

		Formatter out = null;
		double priceTotal = 0;

		try {
			out = new Formatter(new File(OUTPUT_FILENAME));

			// print purchase report headings
			String heading1 = "Purchases Report\n-------------------------------------------------------------------------------------------------------------------";
			String heading2 = String.format("%-24s %-80s %s", "Name", "Title", "Price");
			String heading3 = "-------------------------------------------------------------------------------------------------------------------";
			out.format("%s%n%s%n%s%n", heading1, heading2, heading3);
			LOG.debug(heading1);
			LOG.debug(heading2);
			LOG.debug(heading3);

			// print general purchase report if customerID is null
			if (customerID == null) {

				// print purchase details
				for (Purchase p : purchaseList) {
					priceTotal += p.getPrice();
					String name = String.format("%s %s", customerMap.get(p.getCustomer_id()).getFirstName(),
							customerMap.get(p.getCustomer_id()).getLastName());
					String title = bookMap.get(p.getBook_id()).getOriginal_title();

					out.format("%-24s %-80s $%.2f%n", name, title, p.getPrice());

					LOG.debug(String.format("%-24s %-80s $%.2f", name, title, p.getPrice()));
				}

				if (printTotal) {
					out.format("%nValue of Purchases: %.2f", priceTotal);
				}
			} else {
				// print designated customer's purchase report if customerID is not null
				int customer_id = Integer.parseInt(customerID);
				for (Purchase p : purchaseList) {
					if (p.getCustomer_id() == customer_id) {
						priceTotal += p.getPrice();
						String name = String.format("%s %s", customerMap.get(p.getCustomer_id()).getFirstName(),
								customerMap.get(p.getCustomer_id()).getLastName());
						String title = bookMap.get(p.getBook_id()).getOriginal_title();
						out.format("%-24s %-80s $%.2f%n", name, title, p.getPrice());
						
						LOG.debug(String.format("%-24s %-80s $%.2f", name, title, p.getPrice()));
					}
				}

				out.format("%nValue of purchases: %.2f", priceTotal);
			}

		} catch (FileNotFoundException e) {
			LOG.error("Cannot find file: " + OUTPUT_FILENAME);
			throw new ApplicationException("Cannot find file: " + OUTPUT_FILENAME);
		} catch (Exception e) {
			LOG.error("Erorr creating file: " + OUTPUT_FILENAME);
			throw new ApplicationException("Error creating file: " + OUTPUT_FILENAME);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					LOG.error("Error while closing purchases report output stream", e);
					System.out.println(e.getMessage());
				}
			}
		}

	}

}
