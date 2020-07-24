/**
 * Project: A00_ass1
 * File: BookReport.java
 * Date: Jun. 2, 2020
 * Time: 1:25:32 p.m.
 */
package a00.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Book;
import a00.data.util.ApplicationException;
import a00.data.util.Logging;

/**
 * @author Clara Fok, A00
 *
 */
public class BookReport {

	private static Logger LOG;
	private static final int MAX_STRING_WIDTH = 40;
	private static final String OUTPUT_FILENAME = "book_report.txt";

	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(BookReport.class);
	}

	public static void write(List<Book> bookList) throws ApplicationException {

		LOG.info("Entered BookRerpot.write()");


		Formatter out = null;

		try {
			out = new Formatter(new File(OUTPUT_FILENAME));

			String heading1 = "Books Report\n--------------------------------------------------------------------------------------------------------------------------------------------------";
			String heading2 = String.format("%-8s %-12s %-40s %-40s %4s %6s %13s %-60s", "ID", "ISBN", "Authors",
					"Title", "Year", "Rating", "Ratings Count", "Image URL");
			String heading3 = "--------------------------------------------------------------------------------------------------------------------------------------------------";
			out.format("%s%n%s%n%s%n", heading1, heading2, heading3);
			LOG.debug(heading1);
			LOG.debug(heading2);
			LOG.debug(heading3);

			for (Book b : bookList) {
				
				String bookTitle = null;
				String authors = null;
				String url = null;
				//System.out.println("length of" + b.getAuthors() + " is " + b.getAuthors().length());
				
				if(b.getAuthors().length() > MAX_STRING_WIDTH) {
					authors = String.format("%s...", b.getAuthors().substring(0, MAX_STRING_WIDTH - 3));
				} else {
					authors = b.getAuthors();
				}
				
				if(b.getOriginal_title().length() > MAX_STRING_WIDTH) {
					bookTitle = String.format("%s...", b.getOriginal_title().substring(0,MAX_STRING_WIDTH - 3));
				} else {
					bookTitle = b.getOriginal_title();
				}
				
				if(b.getImage_url().length() > MAX_STRING_WIDTH) {
					url = String.format("%s...", b.getImage_url().substring(0, MAX_STRING_WIDTH - 3));
				} else {
					url = b.getImage_url();
				}
				
				out.format("%08d %-12s %-40s %-40s %4d %6.3f %13d %-60s%n", b.getBook_id(), b.getIsbn(), authors, bookTitle, b.getOriginal_publication_year(), b.getAverage_rating(),
						b.getRatings_count(), url);
				
				LOG.debug(String.format("%08d %-12s %-40s %-40s %4d %6.3f %13d %-60s", b.getBook_id(), b.getIsbn(), authors,
						bookTitle, b.getOriginal_publication_year(), b.getAverage_rating(),
						b.getRatings_count(), url));
			}
		} catch (FileNotFoundException e) {
			LOG.error("Cannot find file: " + OUTPUT_FILENAME);
			throw new ApplicationException("Cannot find file: " + OUTPUT_FILENAME);
		} 
		catch (Exception e) {
			LOG.error("Erorr creating file: " + OUTPUT_FILENAME);
			throw new ApplicationException("Error creating file: " + OUTPUT_FILENAME);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					LOG.error("Error while closing book report output stream", e);
					System.out.println(e.getMessage());
				}
			}
		}

	}
	

}
