/**
 * Project: A00_ass1
 * File: BookReader.java
 * Date: May 27, 2020
 * Time: 12:18:27 p.m.
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

import a00.data.Book;
import a00.data.util.ApplicationException;
import a00.data.util.EscapeSpecialCharacters;
import a00.data.util.Logging;
import a00.data.util.Validator;

/**
 * @author Clara Fok, A00
 *
 */
public class BookReader {

	private static Logger LOG;
	
	private static final String BOOK_DATA_FILENAME = "books500.csv";

	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(BookReader.class);

	}

	/*
	 * Reads book data from csv file
	 * 
	 * Parses data and build Book objects
	 * 
	 * @return Map of Book objects
	 */

	public static Map<Integer, Book> readBookData() throws ApplicationException {
		LOG.info("Enter BookReader.readBookData()");
		
		int count = 0;

		// read book raw data from csv file and store to records
		File file = new File(BOOK_DATA_FILENAME);
		FileReader in = null;
		Iterable<CSVRecord> records;

		try {
			in = new FileReader(file);
			LOG.debug("Reading" + file.getAbsolutePath());
			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Error - can't find data file: " + BOOK_DATA_FILENAME);
		} catch (IOException e) {
			throw new ApplicationException("IOE exception while retrieving book data file");
		} catch (Exception e) {
			throw new ApplicationException("Error while reading book data file");
		} 

		/*
		 * create Map of books
		 * 
		 * key = Integer of book_id
		 * 
		 * value = Book object
		 */
		Map<Integer, Book> books = new HashMap<>();

		LOG.debug("Reading" + file.getAbsolutePath());

		// create book objects from records and add to Map of books
		for (CSVRecord record : records) {

			int book_id = 0;
			String isbn = "";
			String authors = "n/a";
			int publication_year = 0;
			String title = "n/a";
			double average_rating = 0.0;
			long ratings_count = 0;
			String image_url = "n/a";

			// validate and initialize book data variables
			if (Integer.parseInt(record.get("book_id")) >= 0) {
				book_id = Integer.parseInt(record.get("book_id"));
			} else {
				book_id = 0;
				LOG.error("Invalid Book ID: " + record.get("book_id") + "for title: \"" + record.get("original_title")
						+ "\" by " + record.get("authors") + ". Book_id set to 0.");
			}

			if (Validator.validateString(record.get("isbn"))) {
				isbn = record.get("isbn");
			} else {
				isbn = "n/a";
				LOG.error("Invalid ISBN: " + record.get("isbn") + "for title: \"" + record.get("original_title")
						+ "\" by " + record.get("authors") + ". ISBN set to n/a.");
			}

			if (Validator.validateString(record.get("authors"))) {
				String s = record.get("authors");
				
				if (s.length() > 40) {
		            s = s.substring(0, 40 - 3) + "...";
		        }
				if(s.contains("\'")) {
					s = EscapeSpecialCharacters.escapeSingleQuote(s);

				}
				authors = s;
			}

			if (Integer.parseInt(record.get("original_publication_year")) >= -2000) {
				publication_year = Integer.parseInt(record.get("original_publication_year"));
			}

			if (Validator.validateString(record.get("original_title"))) {
				String s = record.get("original_title");
				
				if (s.length() > 40) {
		            s = s.substring(0, 40 - 3) + "...";
		        }
				if(s.contains("\'")) {
					s = EscapeSpecialCharacters.escapeSingleQuote(s);
				}
				title = s;
				LOG.debug(title);
			}

			if (Double.parseDouble(record.get("average_rating")) >= 0) {
				average_rating = Double.parseDouble(record.get("average_rating"));
			}

			if (Long.parseLong(record.get("ratings_count")) >= 0) {
				ratings_count = Long.parseLong(record.get("ratings_count"));
			}

			if (Validator.validateString(record.get("image_url"))) {
				image_url = record.get("image_url");
			}

			// create new book with the variables
			Book b = new Book.Builder(book_id, isbn).setAuthors(authors).setOriginal_publication_year(publication_year)
					.setOriginal_title(title).setAverage_rating(average_rating).setRatings_count(ratings_count)
					.setImage_url(image_url).build();
			
			//Check for repeated book_id (keys) 
			if(books.containsKey(b.getBook_id())) {
				LOG.error("REPEAT BOOK ID: " + b.getBook_id());
			}

			// Add newly created Book to Map<String, Book>
			books.put(book_id, b);
			count++;
			LOG.debug("Added book: " + b);
		}
		
		
		LOG.debug("BOOKS ADDED: " + count);
		LOG.info("Exit BookReader.readBookData()");
		return books;
	}

}
