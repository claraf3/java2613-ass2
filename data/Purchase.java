/**
 * Project: A00_ass1
 * File: Purchase.java
 * Date: May 27, 2020
 * Time: 2:14:57 p.m.
 */
package a00.data;

/**
 * @author Clara Fok, A00
 *
 */
public class Purchase {

	private int id;
	private int customer_id;
	private int book_id;
	private double price;

	/**
	 * Constructor for purchase
	 * 
	 * Initializes Purchase data members with builder's data members
	 * 
	 * @param builder builder object used to initialize Purchase data
	 */
	private Purchase(Builder builder) {
		id = builder.id;
		customer_id = builder.customer_id;
		book_id = builder.book_id;
		price = builder.price;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		if (id >= 0) {
			this.id = id;
		}
	}

	/**
	 * @return the customer_id
	 */
	public int getCustomer_id() {
		return customer_id;
	}

	/**
	 * @param customer_id the customer_id to set
	 */
	public void setCustomer_id(int customer_id) {
		if (customer_id >= 0) {
			this.customer_id = customer_id;
		}
	}

	/**
	 * @return the book_id
	 */
	public int getBook_id() {
		return book_id;
	}

	/**
	 * @param book_id the book_id to set
	 */
	public void setBook_id(int book_id) {
		if (book_id >= 0) {
			this.book_id = book_id;
		}
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
		if (price >= 0.0) {
			this.price = price;
		}
	}

	/**
	 * Override toString() from Objects
	 */
	@Override
	public String toString() {
		return String.format("id=%d, customer id=%d, book_id=%d, price=%.2f", id, customer_id, book_id, price);
	}

	/**
	 * Inner class Builder to build new Purchase objects
	 */
	public static class Builder {

		// required fields
		private int customer_id;
		private int book_id;

		// optional fields
		private int id;
		private double price;
		
		/**
		 * Constructor for builder class
		 * 
		 * @param customer_id for required field customer id
		 * 
		 * @param book_id for required book id
		 */
		public Builder(int customer_id, int book_id) {
			setCustomer_id(customer_id);
			setBook_id(book_id);
		}

		/**
		 * @param customer_id the customer_id to set
		 */
		public Builder setCustomer_id(int customer_id) {
			this.customer_id = customer_id;
			return this;
		}

		/**
		 * @param book_id the book_id to set
		 */
		public Builder setBook_id(int book_id) {
			this.book_id = book_id;
			return this;
		}

		/**
		 * @param id the id to set
		 */
		public Builder setId(int id) {
			this.id = id;
			return this;
		}

		/**
		 * @param price the price to set
		 */
		public Builder setPrice(double price) {
			this.price = price;
			return this;
		}
		
		/**
		 * method to build new Purchase object
		 */
		public Purchase build() {
			return new Purchase(this);
		}

	}

}
