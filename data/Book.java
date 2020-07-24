/**
 * Project: A00_ass1
 * File: Book.java
 * Date: May 27, 2020
 * Time: 11:36:31 a.m.
 */
package a00.data;

import a00.data.util.Validator;

/**
 * @author Clara Fok, A00
 *
 */
public class Book {

	private int book_id;
	private String isbn;
	private String authors;
	private int original_publication_year;
	private String original_title;
	private double average_rating;
	private long ratings_count;
	private String image_url;

	/**
	 * Constructor for Book
	 * 
	 * @param builder for inner class builder to initialize book data
	 */
	private Book(Builder builder) {
		book_id = builder.book_id;
		isbn = builder.isbn;
		authors = builder.authors;
		original_publication_year = builder.original_publication_year;
		original_title = builder.original_title;
		average_rating = builder.average_rating;
		ratings_count = builder.ratings_count;
		image_url = builder.image_url;
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
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(String isbn) {
		if (Validator.validateString(isbn)) {
			this.isbn = isbn;
		}
	}

	/**
	 * @return the authors
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(String authors) {
		if (Validator.validateString(authors)) {
			this.authors = authors;
		}
	}

	/**
	 * @return the original_publication_year
	 */
	public int getOriginal_publication_year() {
		return original_publication_year;
	}

	/**
	 * @param original_publication_year the original_publication_year to set
	 */
	public void setOriginal_publication_year(int original_publication_year) {
		if (original_publication_year >= 0 && original_publication_year <= 2050) {
			this.original_publication_year = original_publication_year;
		}
	}

	/**
	 * @return the original_title
	 */
	public String getOriginal_title() {
		return original_title;
	}

	/**
	 * @param original_title the original_title to set
	 */
	public void setOriginal_title(String original_title) {
		if (Validator.validateString(original_title)) {
			this.original_title = original_title;
		}
	}

	/**
	 * @return the average_rating
	 */
	public double getAverage_rating() {
		return average_rating;
	}

	/**
	 * @param average_rating the average_rating to set
	 */
	public void setAverage_rating(double average_rating) {
		if (average_rating >= 0.000 && average_rating <= 5.000) {
			this.average_rating = average_rating;
		}
	}

	/**
	 * @return the ratings_count
	 */
	public long getRatings_count() {
		return ratings_count;
	}

	/**
	 * @param ratings_count the ratings_count to set
	 */
	public void setRatings_count(long ratings_count) {
		if (ratings_count >= 0) {
			this.ratings_count = ratings_count;
		}
	}

	/**
	 * @return the image_url
	 */
	public String getImage_url() {
		return image_url;
	}

	/**
	 * @param image_url the image_url to set
	 */
	public void setImage_url(String image_url) {
		if (Validator.validateString(image_url)) {
			this.image_url = image_url;
		}
	}

	/**
	 * Override toString method to print Book data in string format
	 */
	@Override
	public String toString() {
		return String.format(
				"Book ID= %d, ISBN= %s, Authors= %s, Publication Year= %d, Title = %s, Average Rating= %.3f, Ratings Count =%d, Image Url=%s",
				book_id, isbn, authors, original_publication_year, original_title, average_rating, ratings_count,
				image_url);
	}

	/**
	 * 
	 * Inner class builder for Book Class
	 *
	 */
	public static class Builder {

		// required fields
		private int book_id;
		private String isbn;

		// optional fields
		private String authors;
		private int original_publication_year;
		private String original_title;
		private double average_rating;
		private long ratings_count;
		private String image_url;

		/**
		 * Constructor for Builder.
		 * 
		 * @param book_id required field of book id
		 * @param isbn    required field of book isbn
		 */
		public Builder(int book_id, String isbn) {
			setBook_id(book_id);
			setIsbn(isbn);
		}

		/**
		 * @param book_id the book_id to set
		 */
		public Builder setBook_id(int book_id) {
			this.book_id = book_id;
			return this;
		}

		/**
		 * @param isbn the isbn to set
		 */
		public Builder setIsbn(String isbn) {
			this.isbn = isbn;
			return this;
		}

		/**
		 * @param authors the authors to set
		 */
		public Builder setAuthors(String authors) {
			this.authors = authors;
			return this;
		}

		/**
		 * @param original_publication_year the original_publication_year to set
		 */
		public Builder setOriginal_publication_year(int original_publication_year) {
			this.original_publication_year = original_publication_year;
			return this;
		}

		/**
		 * @param original_title the original_title to set
		 */
		public Builder setOriginal_title(String original_title) {
			this.original_title = original_title;
			return this;
		}

		/**
		 * @param average_rating the average_rating to set
		 */
		public Builder setAverage_rating(double average_rating) {
			this.average_rating = average_rating;
			return this;
		}

		/**
		 * @param ratings_count the ratings_count to set
		 */
		public Builder setRatings_count(long ratings_count) {
			this.ratings_count = ratings_count;
			return this;
		}

		/**
		 * @param image_url the image_url to set
		 */
		public Builder setImage_url(String image_url) {
			this.image_url = image_url;
			return this;
		}

		/**
		 * method to build new Book object
		 * 
		 * @returns new Book object the Book constructor
		 */
		public Book build() {
			return new Book(this);
		}

	}

}
