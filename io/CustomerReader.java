/*
 * @author Clara Fok A00
 * @version May 27, 2020
 */

package a00.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Customer;
import a00.data.util.ApplicationException;
import a00.data.util.EscapeSpecialCharacters;
import a00.data.util.Logging;
import a00.data.util.Validator;

public class CustomerReader {

	public static final String DATA_DELIMITER = ":";
	public static final String FIELD_DELIMITER = "\\|";
	private static final String CUSTOMER_DATA_FILENAME = "customers.dat";
	private static Logger LOG;

	/*
	 * set up logger
	 */
	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(CustomerReader.class);

	}

	/*
	 * @param customerData input data
	 * 
	 * @param comparer comparator used to sort customers. Argument passed from
	 * driver class.
	 * 
	 * @return An array of customers
	 * 
	 * @throws ApplicationException throws exception if input contains invalid data
	 */
	public static Map<Integer, Customer> readCustomerData() throws ApplicationException {
		LOG.info("Enter CustomerReader.read()");

		// read customer data from file
		Scanner scanner = null;
		ArrayList<String> customerData = new ArrayList<String>();

		try {
			scanner = new Scanner(new FileReader(CUSTOMER_DATA_FILENAME));
			// skip first line (heading) of file
			scanner.nextLine();

			while (scanner.hasNext()) {
				customerData.add(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			throw new ApplicationException(String.format("Customer data file not found: %s", CUSTOMER_DATA_FILENAME));
		} catch (Exception e) {
			throw new ApplicationException("Error while reading customer data file");
		} finally {
			if(scanner != null) {
				try {
					LOG.debug("Closing customer data scanner");
					scanner.close();
				} catch (Exception e){
					throw new ApplicationException("Error while closing customer data scanner");
				}
			}
		}
		

		/*
		 *  create Map to store customers
		 *  
		 *  Key = customer_id
		 *  
		 *  Value = Customer obj
		 */
		Map<Integer, Customer> customerMap = new HashMap<>();

		// parse customer data
		LOG.debug("Parsing customer data");
		
		String [] data;
		
		for (String customer : customerData) {
			if(customer.contains("\'")) {
				String escapedString  = EscapeSpecialCharacters.escapeSingleQuote(customer);
				data = escapedString.split(FIELD_DELIMITER);
			}
			else {
				data = customer.split(FIELD_DELIMITER);
			}

			// Checks for 9 data elements. If less than 9, throws ApplicationException.
			if (data.length < 9) {
				LOG.error("Parse error - customer data has less than 9 elements: " + Arrays.toString(data));
				throw new ApplicationException(
						String.format("Expected 9 elements but got %d: %s", data.length, Arrays.toString(data)));

			}

			int customer_id;
			String firstName;
			String lastName;
			String streetName;
			String city;
			String postalCode;
			String phoneNumber;
			String email;
			LocalDate joinDate = null;

			// validate and set customerNumber
			if (Validator.validateString(data[0]) && Integer.parseInt(data[0]) >= 0) {
				customer_id = Integer.parseInt(data[0]);
			} else {
				LOG.error("Invalid Customer id:" + data[0]);
				customer_id = 0;
			}

			// validate and set first name
			if (Validator.validateString(data[1])) {
				firstName = data[1];
			} else {
				LOG.error("Invalid first name: " + data[1]);
				firstName = "n/a";
				//throw new ApplicationException(String.format("Invalid first name: %s", data[1]));
			}

			// validate and set last name
			if (Validator.validateString(data[2])) {
				lastName = data[2];
			} else {
				LOG.error("Invalid last name: " + data[2]);
				lastName = "n/a";
				//throw new ApplicationException(String.format("Invalid last name: %s", data[2]));
			}

			// validate and set street name
			if (Validator.validateString(data[3])) {
				streetName = data[3];
			} else {
				LOG.error("Invalid Street name: " + data[3]);
				streetName = "n/a";
				//throw new ApplicationException(String.format("Invalid street name: %s", data[3]));
			}

			// validate and set city
			if (Validator.validateString(data[4])) {
				city = data[4];
			} else {
				LOG.error("Invalid city: " + data[4]);
				city = "n/a";
				//throw new ApplicationException(String.format("Invalid city: %s", data[4]));
			}

			// validate and set postalCode
			if (Validator.validateString(data[5]))  {
				postalCode = data[5];
			} else {
				LOG.error("Invalid postal code: " + data[5]);
				postalCode = "n/a";
				//throw new ApplicationException(String.format("Invalid postal code: %s", data[5]));
			}

			// validate and set phone number
			if (Validator.validateString(data[6]) && Validator.validatePhone(data[6])) {
				phoneNumber = data[6];
			} else {
				LOG.error("Invalid phone number: " + data[6]);
				phoneNumber = "n/a";
				//throw new ApplicationException(String.format("Invalid phone number: %s", data[6]));
			}

			// validate and set email
			if (Validator.validateString(data[7]) && Validator.validateEmail(data[7])) {
				email = data[7];
			} else {
				LOG.error("Invalid email: " + data[7]);
				email = "n/a";
				//throw new ApplicationException(String.format("Invalid email: %s", data[7]));
			}

			/*
			 * Parse input data into int values for year, month and day
			 * 
			 * Sets customer joined date with validated input data
			 * 
			 * @throws ApplicationException if input is invalid
			 */
			if (Validator.validateString(data[8]) && Validator.validateDateString(data[8])) {

				int year = Integer.parseInt(data[8].substring(0, 4));
				int month = Integer.parseInt(data[8].substring(4, 6));
				int day = Integer.parseInt(data[8].substring(6));

				boolean validYear = false;
				boolean validMonth = false;
				boolean validDay = false;

				// validate year
				if (Validator.validateJoinedYear(year)) {
					validYear = true;
				} else {
					LOG.error("Invalid year: " + year);
					//throw new ApplicationException(String.format("Invalid year (valid values 1980 - 2020): %d", year));
				}

				// validate month
				if (Validator.validateJoinedMonth(month)) {
					validMonth = true;
				} else {
					LOG.error("Invalid month: " + month);
					//throw new ApplicationException(String.format("Invalid month (valid values 1 - 12): %d", month));
				}

				// validate day of month
				if (Validator.validateJoinedDay(month, day)) {
					validDay = true;
				} else {
					LOG.error("Invalid day: " + day);
					//throw new ApplicationException(
							//String.format("Invalid day for Day Of Month (valid values 1-28-31): %d", day));
				}

				// set joint date if year, month, and day input is valid
				if (validYear && validMonth && validDay) {
					joinDate = LocalDate.of(year, month, day);
				} 
		
			} else {
				LOG.error("Invalid date string: " + data[8]);
				//throw new ApplicationException(String.format("Invalid date string: %s", data[8]));
			}

			// create customer and store into CustomerMap
			Customer c = new Customer.Builder(customer_id, phoneNumber).setFirstName(firstName).setLastName(lastName)
					.setStreetName(streetName).setCity(city).setPostalCode(postalCode).setEmail(email)
					.setJoinDate(joinDate).build();
			
			//Check for repeated customer_id (keys) 
			if(customerMap.containsKey(c.getCustomerID())) {
				LOG.error("REPEAT CUSTOMER ID: " + c.getCustomerID());
			}
			
			customerMap.put(customer_id, c);
			LOG.debug("Added Customer " + c);

		}
		return customerMap;
	}
}
