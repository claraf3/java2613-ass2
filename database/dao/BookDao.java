/**
 * Project: A00_ass2
 * File: BookDao.java
 * Date: Jun. 22, 2020
 * Time: 1:26:15 p.m.
 */
package a00.database.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Book;
import a00.data.util.ApplicationException;
import a00.data.util.Logging;
import a00.database.Database;

/**
 * @author Clara Fok, A00
 *
 */
public class BookDao extends Dao{



	private static Logger LOG;
	
	private static final String BOOK_TABLE_NAME = "A00_Books";
	
	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(CustomerDao.class);
	}
	
	
	/**
	 * @param database
	 */

	public BookDao(Database database) {
		super(database, BOOK_TABLE_NAME);
	}
	
	//create table
	//@Override create() from Dao class
	@Override
	public void create() throws ApplicationException {
		LOG.info("Creating " + BOOK_TABLE_NAME);
		String sql = String.format(
				"CREATE TABLE %s(" //0
				+ "%s INT not null, "	//1
				+ "%s VARCHAR(15) not null, "//2
				+ "%s VARCHAR(50), "//3
				+ "%s INT, "//4
				+ "%s VARCHAR(45), "//5
				+ "%s FLOAT, "//6
				+ "%s INT, "//7
				+ "%s VARCHAR(200), "//8
				+ "primary key(%s) )", //9
				BOOK_TABLE_NAME, //0
				Fields.ID.getName(), //1 
				Fields.ISBN.getName(), //2
				Fields.AUTHORS.getName(), //3
				Fields.PUB_YEAR.getName(), //4
				Fields.TITLE.getName(), //5
				Fields.AVG_RATING.getName(), //6 
				Fields.RATINGS_COUNT.getName(), //7
				Fields.URL.getName(), //8
				Fields.ID.getName()); //9
		try {
			super.create(sql);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}

	}
	
	//add Book data to table
	public void addBook(Book b) throws ApplicationException {
		LOG.info("Adding data to " + BOOK_TABLE_NAME);
		String sql = String.format("INSERT INTO %s VALUES("
				+ "%d, " //id 0
				+ "'%s', " //isbn 1
				+ "'%s', " //authors 2
				+ "%d, " //publication year 3
				+ "'%s', " //title 4
				+ "%.3f, " //avg rating 5
				+ "%d, " //ratings count 6
				+ "'%s')", //img url 7
				BOOK_TABLE_NAME,
				b.getBook_id(), //0
				b.getIsbn(), //1
				b.getAuthors(), //2
				b.getOriginal_publication_year(), //3
				b.getOriginal_title(), //4
				b.getAverage_rating(), //5
				b.getRatings_count(), //6
				b.getImage_url()); //7		
		try {
			super.add(sql);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} 							
	}
	
	//method to update an existing Book record
	public void updateBook(Book b) throws ApplicationException {
		
		Statement stmt = null;
		
		String sql = String.format("UPDATE %s SET "
				+ "%s = %d, " //id 0
				+ "%s = '%s, " //isbn 1
				+ "%s = '%s', " //authors 2
				+ "%s = %d, " //publication year 3
				+ "%s = '%s', " //title 4
				+ "%s = %.3f, " //avg rating 5
				+ "%s = %d, " //ratings count 6
				+ "%s = '%s')" //image url 7
				+ "WHERE %s = %d", //primary key id 8
				BOOK_TABLE_NAME,
				Fields.ID.getName(),b.getBook_id(), //0
				Fields.ISBN.getName(),b.getIsbn(), //1
				Fields.AUTHORS.getName(), b.getAuthors(), //2
				Fields.PUB_YEAR.getName(), b.getOriginal_publication_year(), //3
				Fields.TITLE.getName(), b.getOriginal_title(), //4
				Fields.AVG_RATING.getName(), b.getAverage_rating(), //5
				Fields.RATINGS_COUNT.getName(), b.getRatings_count(), //6
				Fields.URL.getName(), b.getImage_url(), //7
				Fields.ID.getName(), b.getBook_id()); //8
		
		try {
			
			Connection conn = database.getConnection();
			stmt = conn.createStatement();
			int rowsUpdated = stmt.executeUpdate(sql);
			LOG.debug("Book rows updated: ", rowsUpdated);
		} catch (SQLException e) {
			throw new ApplicationException("Error - fail to update Book table");
		} catch (NullPointerException e) {
			throw new ApplicationException(e);
		} finally {
			close(stmt);
		}	
	}
	
	
	//method to delete 
	public void deleteBook(Book b) throws ApplicationException{
		
		Statement stmt = null;
		
		String sql = String.format("DELETE %s WHERE %s = %d", BOOK_TABLE_NAME, Fields.ID.getName(), b.getBook_id());
		
		try {
			Connection conn = database.getConnection();
			stmt = conn.createStatement();
			int rowsDeleted = stmt.executeUpdate(sql);
			LOG.info("Book rows deleted: ", rowsDeleted);
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} catch (NullPointerException e) {
			throw new ApplicationException(e);
		} finally {
			close(stmt);
		}				
	}
	
	//method to make queries on book database
		public ResultSet makeQuery(String sql) throws ApplicationException {
			
			LOG.debug("Querying Book table");
			
			Connection conn = database.getConnection();
			Statement stmt = null;
			
			try {
				stmt = conn.createStatement();
				LOG.debug(sql);
				ResultSet rs = stmt.executeQuery(sql);
				return rs;
			} catch (SQLException e) {
				throw new ApplicationException("Fail to query book table");
			} 			
		}
		
		public Book getBookByID(int book_id) throws ApplicationException{
			LOG.debug("Getting book by Book ID");
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			int count = 0;
			Book b = null;;
			
			try {
				conn = database.getConnection();
				stmt = conn.createStatement();
				
				String sql = String.format("SELECT * FROM %s WHERE %s=%d", BOOK_TABLE_NAME, Fields.ID.getName(), book_id);
				LOG.debug(sql);
				rs = stmt.executeQuery(sql);	
				
				while(rs.next()) {
					count++;
					if(count > 1) {
						throw new ApplicationException("Expected 1 book but got more than 1");
					}
					
					int id = rs.getInt(1);
					String isbn = rs.getString(2);
					String authors = rs.getString(3);
					int pubYear = rs.getInt(4);
					String title = rs.getString(5);
					double rating = rs.getDouble(6);
					int ratingCount = rs.getInt(7);
					String url = rs.getString(8);
					
					b = new Book.Builder(id,  isbn).setAuthors(authors).setOriginal_publication_year(pubYear).setOriginal_title(title).setAverage_rating(rating).setRatings_count(ratingCount).setImage_url(url).build();
					
				}
				
				return b;
				
			} catch (SQLException e) {
				throw new ApplicationException(e);
			} finally {
				close(stmt);
			}
		}
	
	//method to count book records in table
	public int count() throws ApplicationException {
		
		LOG.debug("Counting customer records");
		
		Connection conn = database.getConnection();
		Statement stmt = null;
		int count = 0;
		
		try {
			stmt = conn.createStatement();	
			String sql = String.format("SELECT COUNT(*) FROM %s AS count", BOOK_TABLE_NAME);
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				count = rs.getInt(1);
			}
			return count;

		} catch (SQLException e) {
			throw new ApplicationException("Fail to query book table");
		} finally {
			close(stmt);
		}
		
	}
	
	
	public enum Fields {
		
		ID("book_id", "int", 10, 1),
		ISBN("isbn", "String", 15, 2),
		AUTHORS("authors", "String",40, 3),
		PUB_YEAR("original_publication_year", "int", 4, 4),
		TITLE("original_title", "String", 40, 5),
		AVG_RATING("average_rating", "double", 10, 6),
		RATINGS_COUNT("ratings_count", "int", 12, 7),
		URL("image_url", "int", 60, 8);
		
		private final String name;
		private final String dataType;
		private final int size;
		private final int column;
		

		private Fields(String name, String dataType, int size, int column) {
			this.name = name;
			this.dataType = dataType;
			this.size = size;
			this.column = column;
		}


		public String getName() {
			return name;
		}


		public String getDataType() {
			return dataType;
		}


		public int getSize() {
			return size;
		}

		public int getColumn() {
			return column;
		}
		
		
	}
	
	
	
	
	
}
