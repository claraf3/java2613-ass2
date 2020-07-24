/**
 * Project: A00_ass2
 * File: PurchaseDetailedData.java
 * Date: Jun. 24, 2020
 * Time: 11:10:23 a.m.
 */
package a00.data;

/**
 * @author Clara Fok, A00
 *
 */
public class PurchaseDetailedData {

	private int customer_id;
	private String firstName;
	private String lastName;
	private String bookTitle;
	private double price;
	/**
	 * @param firstName
	 * @param lastName
	 * @param bookTitle
	 * @param price
	 */
	public PurchaseDetailedData(int customerID, String firstName, String lastName, String bookTitle, double price) {
		super();
		this.customer_id = customerID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.bookTitle = bookTitle;
		this.price = price;
	}
	/**
	 * @return the customer_id
	 */
	public int getCustomerID() {
		return customer_id;
	}
	/**
	 * @param customer_id the customer_id to set
	 */
	public void setCustomerID(int customerID) {
		this.customer_id = customerID;
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
		this.firstName = firstName;
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
		this.lastName = lastName;
	}
	/**
	 * @return the bookTitle
	 */
	public String getBookTitle() {
		return bookTitle;
	}
	/**
	 * @param bookTitle the bookTitle to set
	 */
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	
	/*
	 * Override toString method from Object Class
	 */
	@Override
	public String toString() {
		return String.format("%d %-5s %-24s %-80s $%.2f", customer_id, firstName, lastName, bookTitle, price);
	}
	
	
	
}
