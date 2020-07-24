/*
 * @author Clara Fok A00
 * @version May 19, 2020
 */

package a00.io;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Formatter;
import java.io.FileNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Customer;
import a00.data.util.ApplicationException;
import a00.data.util.Logging;

public class CustomerReport {

	private static Logger LOG;
	private static final String OUTPUT_FILE_NAME = "customers_report.txt";

	/*
	 * set up logger
	 */
	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(CustomerReport.class);
	}

	/*
	 * @param customers for array of Customer objects prints customer data into a
	 * report format
	 */
	public static void write(List<Customer> customers) throws ApplicationException {


		Formatter output = null;

		LOG.info("CustomerReport.write()");
		int i = 0;
		LOG.debug("Generating customer report:");

		// generate customer report file
		try {

			output = new Formatter(OUTPUT_FILE_NAME);

			// output report header
			String header1 = "Customers Report\n-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
			String header2 = String.format("%5s %-6s %-12s %-12s %-40s %-25s %-12s %-15s %-40s %-12s", "#.", "ID.",
					"First name", "Last name", "Street", "City", "Postal Code", "Phone", "Email", "Join Date");
			String header3 = "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
			output.format("%s%n%s%n%s%n", header1, header2, header3);

			LOG.info(header1);
			LOG.info(header2);
			LOG.info(header3);

			// output customer info
			for (Customer c : customers) {

				// format date output to "Mmm dd yyyy"
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLL dd yyyy");

				output.format("%4d. %06d %-12s %-12s %-40s %-25s %-12s %-15s %-40s %-12s%n", ++i, c.getCustomerID(),
						c.getFirstName(), c.getLastName(), c.getStreetName(), c.getCity(), c.getPostalCode(),
						c.getPhoneNumber(), c.getEmail(), formatter.format(c.getJoinDate()));

				LOG.info(String.format("%4d. %06d %-12s %-12s %-40s %-25s %-12s %-15s %-40s %-12s", i,
						c.getCustomerID(), c.getFirstName(), c.getLastName(), c.getStreetName(), c.getCity(),
						c.getPostalCode(), c.getPhoneNumber(), c.getEmail(), formatter.format(c.getJoinDate())));
			}

		} catch (FileNotFoundException e) {
			LOG.error("Customer report generation error- file not found: " + OUTPUT_FILE_NAME);
			throw new ApplicationException("Customer report generation error- file not found: \" + OUTPUT_FILE_NAME");
		} catch (Exception e) {
			LOG.error("Error while generating customer report: ", e);
			throw new ApplicationException("Customer report generation error");
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					LOG.error("Error while closing output stream", e);
					System.out.println(e.getMessage());
				}
			}
		}
	}

}
