/*
 * @author Clara Fok A00
 * @version May 26, 2020
 */

package a00.data;

/*
 * @author Clara Fok A00
 * @version May 27, 2020
 */
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.util.Logging;
import a00.data.util.Validator;

public class Customer {

	private int customer_id;
	private String firstName;
	private String lastName;
	private String streetName;
	private String city;
	private String postalCode;
	private String phoneNumber;
	private String email;
	private LocalDate joinDate;
	
	private static Logger LOG;

	/*
	 * Static
	 * 
	 * Starts up the logger for Lab5
	 */
	static {

		Logging.configureLogging();

		LOG = LogManager.getLogger(Customer.class);
	}

	/*
	 * Customer parametized constructor populates member data with builder member
	 * data
	 */
	private Customer(Builder builder) {
		customer_id = builder.customer_id;
		firstName = builder.firstName;
		lastName = builder.lastName;
		streetName = builder.streetName;
		city = builder.city;
		postalCode = builder.postalCode;
		phoneNumber = builder.phoneNumber;
		email = builder.email;
		joinDate = builder.joinDate;
	}

	/**
	 * @return the customer_id
	 */
	public int getCustomerID() {
		return customer_id;
	}

	/**
	 * @param customer_id the customer_id to set
	 * 
	 * @deprecated customer ID should not be changed after customer object creation
	 */
	@Deprecated
	public void setCustomerID(int customer_id) {
		if (customer_id > 0) {
			this.customer_id = customer_id;
		} else {
			LOG.error("fail to set customer_id: " + customer_id);
		}
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		if (firstName != null && !firstName.isEmpty()) {
			this.firstName = firstName;
		} else {
			LOG.error("fail to set first name: " + firstName);
		}
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		if (lastName != null && !lastName.isEmpty()) {
			this.lastName = lastName;
		} else {
			LOG.error("fail to set lastName: " + lastName);
		}
	}

	/**
	 * @return the streetName
	 */
	public String getStreetName() {
		return streetName;
	}

	/**
	 * @param streetName the streetName to set
	 */
	public void setStreetName(String streetName) {
		if (streetName != null && !streetName.isEmpty()) {
			this.streetName = streetName;
		} else {
			LOG.error("fail to set street name: " + streetName);
		}
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		if (city != null && !city.isEmpty()) {
			this.city = city;
		} else {
			LOG.error("fail to set city: " + city);
		}
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set Checks for null, empty, and format
	 */
	public void setPostalCode(String postalCode) {
		if (Validator.validateString(postalCode)) {
			this.postalCode = postalCode;
		} else {
			LOG.error("fail to set postalCode: " + postalCode);
		}
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set Checks for null, empty, and format
	 */
	public void setPhoneNumber(String phoneNumber) {
		if (Validator.validateString(phoneNumber) && Validator.validatePhone(phoneNumber)) {
			this.phoneNumber = phoneNumber;
		} else {
			LOG.error("fail to set phone number: " + phoneNumber);
		}
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set Checks for null, empty, and format
	 */
	public void setEmail(String email) {
		if (Validator.validateString(email) && Validator.validateEmail(email)) {
			this.email = email;
		} else {			
			LOG.error("fail to set email: " + email);
			this.email = "N/A";
		}
	}

	/**
	 * @return the joinDate
	 */
	public LocalDate getJoinDate() {
		return joinDate;
	}

	/**
	 * @param joinDate the joinDate to set
	 */
	public void setJoinDate(LocalDate joinDate) {
		if (joinDate != null) {
			this.joinDate = joinDate;
		} else {
			LOG.error("fail to set joinDate: " + joinDate);
		}
	}

	/**
	 * Override toString() method from Objects
	 */
	@Override
	public String toString() {
		return "Customer [customer_id=" + customer_id + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", streetName=" + streetName + ", city=" + city + ", postalCode=" + postalCode + ", phoneNumber="
				+ phoneNumber + ", email=" + email + ", joinDate=" + joinDate + "]";
	}

	/**
	 * Inner builder class to validate and pass arguments to outer Customer class
	 * ------------------------------------------------------------------------
	 */
	@SuppressWarnings(value = { "serial" })
	public static class Builder extends Exception {

		// Required fields
		private int customer_id;
		private String phoneNumber;

		// Optional fields - if empty, initialized to default values
		private String firstName;
		private String lastName;
		private String streetName;
		private String city;
		private String postalCode;
		private String email;
		private LocalDate joinDate;

		public Builder(int customerNumber, String phoneNumber) {
			setCustomerID(customerNumber);
			setPhoneNumber(phoneNumber);
		}

		/**
		 * @param customer_id the customer_id to set
		 */
		public Builder setCustomerID(int customerNumber) {
			this.customer_id = customerNumber;
			return this;
		}

		/**
		 * @param firstName the firstName to set
		 */
		public Builder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		/**
		 * @param lastName the lastName to set
		 */
		public Builder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		/**
		 * @param streetName the streetName to set
		 */
		public Builder setStreetName(String streetName) {
			this.streetName = streetName;
			return this;
		}

		/**
		 * @param city the city to set
		 */
		public Builder setCity(String city) {
			this.city = city;
			return this;
		}

		/**
		 * @param postalCode the postalCode to set Checks for null, empty, and format
		 */
		public Builder setPostalCode(String postalCode) {
			this.postalCode = postalCode;
			return this;
		}

		/**
		 * @param phoneNumber the phoneNumber to set Checks for null, empty, and format
		 */
		public Builder setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		/**
		 * @param email the email to set Checks for null, empty, and format
		 */
		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}

		/**
		 * @param joinDate the joinDate to set
		 */
		public Builder setJoinDate(LocalDate joinDate) {
			this.joinDate = joinDate;
			return this;
		}

		/**
		 * method to build and return a Customer object
		 */
		public Customer build() {
			return new Customer(this);
		}

	}

}
