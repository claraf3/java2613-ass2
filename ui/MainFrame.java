/**
 * Project: a00957354_ass2
 * File: MainFrame.java
 * Date: Jun. 22, 2020
 * Time: 9:19:17 p.m.
 */
package a00.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Book;
import a00.data.PurchaseDetailedData;
import a00.data.util.ApplicationException;
import a00.data.util.Logging;
import a00.database.Database;
import a00.database.dao.BookDao;
import a00.database.dao.CustomerDao;
import a00.database.dao.PurchaseDao;
import a00.database.dao.TablesManager;
import net.miginfocom.swing.MigLayout;

//import a00.data.Book;

/**
 * @author Clara Fok, A00957354
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JMenuBar menuBar;

	private JMenu fileMenu;
	private JMenu bookMenu;
	private JMenu customerMenu;
	private JMenu purchaseMenu;
	private JMenu helpMenu;
	
	private JPanel contentPane;

	private int idToFilter = 0;	

	private static Logger LOG;

	private static final String CUSTOMER_TABLE_NAME = "A00957354_Customers";
	private static final String BOOK_TABLE_NAME = "A00957354_Books";


	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(TablesManager.class);
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(CustomerDao customerDao, BookDao bookDao, PurchaseDao purchaseDao, Database db) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 667, 545);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[]", "[]"));

		buildMenu(customerDao, bookDao, purchaseDao, db);
	}
	
	//builds menubar
	public void buildMenu(CustomerDao customerDao, BookDao bookDao, PurchaseDao purchaseDao, Database db) {

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		addMenus(customerDao, bookDao, purchaseDao);
		addFileSubMenus(db);
		addCustomerSubMenus(customerDao);
		addBookSubMenus(bookDao);
		addPurchaseSubMenus(purchaseDao, bookDao, customerDao);
		addHelpSubMenus();

	}
	
	//add menus to menubar
	public void addMenus(CustomerDao customerDao, BookDao bookDao, PurchaseDao purchaseDao) {
		
		// file menu
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);

		// book menu
		bookMenu = new JMenu("Books");
		bookMenu.setMnemonic('B');
		menuBar.add(bookMenu);

		// customers menu
		customerMenu = new JMenu("Customers");
		customerMenu.setMnemonic('C');
		menuBar.add(customerMenu);
				
		// purchases menu
		purchaseMenu = new JMenu("Purchases");
		purchaseMenu.setBorderPainted(true);
		purchaseMenu.setMnemonic('P');
		menuBar.add(purchaseMenu);

		// help menu
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		menuBar.add(helpMenu);
		
		LOG.debug("added menu to menu bar");
	}

	// add submenus to File menu
	public void addFileSubMenus(Database db) {
		LOG.debug("added submenu to File menu");
		
		// File - add drop
		JMenuItem drop = new JMenuItem("Drop");
		drop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int response = JOptionPane.showConfirmDialog(MainFrame.this,
						"Do you wish to delete all Book2 input data?", "Warning", 0, 2);

				if (response == 0) {
					try {
						TablesManager.dropAllTables();
						JOptionPane.showMessageDialog(MainFrame.this, "Tables dropped. Exiting Application.");					
						System.exit(0);
					} catch (ApplicationException e1) {
						LOG.error("Error while dropping tables", e1);
						JOptionPane.showMessageDialog(MainFrame.this, "Error while dropping tables", "Error", 3);
					}
				}
			}
		});
		drop.setMnemonic('D');
		fileMenu.add(drop);

		// File - add Quit
		JMenuItem quitApp = new JMenuItem("Quit");
		quitApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					db.shutDown();
				} catch (ApplicationException e1) {
					LOG.error("Error while quitting application", e1);
					JOptionPane.showMessageDialog(MainFrame.this, "Failed to shut down database", "Error", 3);
				}
				System.exit(0);
			}
		});
		quitApp.setMnemonic('Q');
		fileMenu.add(quitApp);
	}

	// add submenus to Customer Menu
	// @param customerDao the dao to access customer table
	public void addCustomerSubMenus(CustomerDao customerDao) {
		LOG.debug("added submenu to Customer menu");
		//Counts number of customers in database
		JMenuItem countCustomers = new JMenuItem("Count");
		countCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int count = customerDao.count();
					JOptionPane.showMessageDialog(MainFrame.this, "Customer Count: " + count, "Customer Count",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (ApplicationException e1) {
					LOG.error(e1.getMessage(), e1);
					JOptionPane.showMessageDialog(MainFrame.this, "Error while counting customers", "Error", 3);
				}
			}
		});
		countCustomers.setMnemonic('O');
		customerMenu.add(countCustomers);

		// Customers - add sort by join date
		JCheckBoxMenuItem customerSortByJoinDate = new JCheckBoxMenuItem("By Join Date");
		customerSortByJoinDate.setMnemonic('J');
		customerMenu.add(customerSortByJoinDate);

		// add action listener for user request to see list of customers
		JMenuItem listCustomers = new JMenuItem("List");
		listCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> customerName = new ArrayList<String>();
				ResultSet rs = null;
				String sql = null;
				
				//sort customer by join date if check box for sort by join date is selected
				if (customerSortByJoinDate.isSelected()) {
					sql = String.format("SELECT %s, %s, %s " + "FROM %s " + "ORDER BY joinDate",
							CustomerDao.Fields.CUSTOMER_ID.getName(), CustomerDao.Fields.FIRST_NAME.getName(), CustomerDao.Fields.JOIN_DATE.getName(),
							CUSTOMER_TABLE_NAME);
					LOG.debug("Retrieving customer list by join date");
				} else {
					sql = String.format("SELECT %s, %s, %s " + "FROM %s", CustomerDao.Fields.CUSTOMER_ID.getName(),
							CustomerDao.Fields.FIRST_NAME.getName(), CustomerDao.Fields.JOIN_DATE.getName(), CUSTOMER_TABLE_NAME);
					LOG.debug("Retrieving customer list");
					
				}
				try {
					rs = customerDao.makeQuery(sql);
					while (rs.next()) {
						String s = String.format("%-10s %-30s %s", rs.getInt(1), rs.getString(2), rs.getDate(3).toString());
						customerName.add(s);
					}
					String[] customerArr = new String[customerName.size()];
					customerArr = customerName.toArray(customerArr);
					new CustomerList(customerArr, customerDao);
				} catch (ApplicationException e1) {
					LOG.error(e1.getMessage(), e1);
					JOptionPane.showMessageDialog(MainFrame.this, "Error while retrieving customer list", "Error", 3);
					e1.printStackTrace();
				} catch (SQLException e2) {
					LOG.error(e2.getMessage(), e2);
					JOptionPane.showMessageDialog(MainFrame.this, "Error while reading customer Result Set", "Error",3);
				} catch (Exception e3) {
					LOG.error(e3.getMessage(), e3);
					JOptionPane.showMessageDialog(MainFrame.this, "Error while reading customer Result Set", "Error",3);
				} finally {
					try {
						if(rs!=null) {
							rs.close();
						}
					} catch (SQLException e1) {
						LOG.error("Failed to close resultset", e);
					}
				}
			}
		});
		listCustomers.setMnemonic('L');
		customerMenu.add(listCustomers);

	}

	// adds submenus to Book Menu
	// @param bookDao the dao to access book table
	public void addBookSubMenus(BookDao bookDao) {
		LOG.debug("added submenu to Book menu");
		// Count number of books in database
		JMenuItem bookCount = new JMenuItem("Count");
		bookCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					int count = bookDao.count();
					JOptionPane.showMessageDialog(MainFrame.this, "Book Count: " + count, "Book Count",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (ApplicationException e1) {
					LOG.error(e1.getMessage(), e1);
					JOptionPane.showMessageDialog(MainFrame.this, "Error while counting Books in BookStore", "Error",3);
				}
			}
		});
		bookCount.setMnemonic('O');
		bookMenu.add(bookCount);

		// Books - checkbox to select sort by author
		JCheckBoxMenuItem bookSortByAuthor = new JCheckBoxMenuItem("By Author");

		bookSortByAuthor.setMnemonic('A');
		bookMenu.add(bookSortByAuthor);

		// Books - checkbox to select sort descending
		JCheckBoxMenuItem bookSortByDescending = new JCheckBoxMenuItem("Descending");
		bookSortByDescending.setMnemonic('D');
		bookMenu.add(bookSortByDescending);

		// Books - add action listener for user request to see list of books in database
		JMenuItem listBooks = new JMenuItem("List");
		listBooks.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ResultSet rs;
				String sql;
				ArrayList<Book> books = new ArrayList<Book>();
				//sort books based on user selections on checkboxes
				if (bookSortByAuthor.isSelected()) {
					if (bookSortByDescending.isSelected()) {
						sql = String.format("SELECT * FROM %s ORDER BY %s DESC", BOOK_TABLE_NAME,
								BookDao.Fields.AUTHORS.getName());
					} else {
						sql = String.format("SELECT * FROM %s ORDER BY %s", BOOK_TABLE_NAME,
								BookDao.Fields.AUTHORS.getName());
					}
				} else {
					sql = String.format("SELECT * FROM %s", BOOK_TABLE_NAME);
				}
				try {
					rs = bookDao.makeQuery(sql);
					while (rs.next()) {
						Book b = new Book.Builder(rs.getInt(1), rs.getString(2)).setAuthors(rs.getString(3))
								.setOriginal_publication_year(rs.getInt(4)).setOriginal_title(rs.getString(5))
								.setAverage_rating(rs.getDouble(6)).setRatings_count(rs.getInt(7))
								.setImage_url(rs.getString(8)).build();
						books.add(b);
					}

					new BookList(books);
				} catch (ApplicationException e1) {
					LOG.error(e1.getMessage(), e1);
					JOptionPane.showMessageDialog(MainFrame.this, "Error while retrieving book data", "Error", 3);
				} catch (SQLException e2) {
					LOG.error(e2.getMessage(), e2);
					JOptionPane.showMessageDialog(MainFrame.this, "Error while reading book ResultSet", "Error", 3);
				}
			}
		});
		listBooks.setMnemonic('L');
		bookMenu.add(listBooks);

	}

	// add submenus to Purchase Menu
	public void addPurchaseSubMenus(PurchaseDao purchaseDao, BookDao bookDao, CustomerDao customerDao) {

		// Purchase - add purchase total count
		JMenuItem purchaseTotal = new JMenuItem("Total");
		purchaseTotal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					double count = purchaseDao.purchaseTotal(idToFilter);
					JOptionPane.showMessageDialog(MainFrame.this, String.format("Purchase Total: %.2f", count),
							"Purchase Total", JOptionPane.INFORMATION_MESSAGE);
				} catch (ApplicationException e1) {
					JOptionPane.showMessageDialog(MainFrame.this, "Error while retriving total purchases", "Error", 3);
					LOG.error(e1.getMessage(), e1);
				}
			}
		});
		purchaseTotal.setMnemonic('T');
		purchaseMenu.add(purchaseTotal);

		// Purchase - checkbox to select sort by last name
		JCheckBoxMenuItem purchaseByLastName = new JCheckBoxMenuItem("By Last Name");
		purchaseByLastName.setMnemonic('N');
		purchaseMenu.add(purchaseByLastName);

		// Purchase - checkbox to select sort by title
		JCheckBoxMenuItem purchaseSortByTitle = new JCheckBoxMenuItem("By Title");
		purchaseSortByTitle.setMnemonic('I');
		purchaseMenu.add(purchaseSortByTitle);

		// Purchase - checkbox to select sort by descending
		JCheckBoxMenuItem purchaseSortByDescending = new JCheckBoxMenuItem("Descending");
		purchaseSortByDescending.setMnemonic('D');
		purchaseMenu.add(purchaseSortByDescending);

		// Purchase - filters customer and purchase data by performing SQL query based on customer ID
		// Returns data for indivdual customer
		JMenuItem purchaseByCustomerId = new JMenuItem("Filter By Customer ID");
		purchaseByCustomerId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String s = JOptionPane.showInputDialog(MainFrame.this,
						"Please enter ID of customer you wish to filter by", "Filter Purchase by Customer ID",
						JOptionPane.QUESTION_MESSAGE);
				if (s == null || s.isEmpty()) { //check null
					LOG.info("No filters applied");
					idToFilter = 0;
				} else if (!s.matches("[-+]?\\d*\\.?\\d+") || Integer.parseInt(s) <= 999 || Integer.parseInt(s) >= 10000) { //check non-numeric input or invalid numeric input
					JOptionPane.showMessageDialog(MainFrame.this, "Invalid Customer ID Entered", "Warning", 2);
				} else {
					idToFilter = Integer.parseInt(s);
					LOG.info(String.format("Purchase filtered by customer %d", idToFilter));
				}

			}
		});
		purchaseByCustomerId.setMnemonic('C');
		purchaseMenu.add(purchaseByCustomerId);

		// Purchase - add action listener for user request to get list of purchases
		// Shows all purchase details by default unless filter by customer ID is set
		JMenuItem listPurchase = new JMenuItem("List");
		listPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ResultSet rs = downloadData(purchaseDao);
				ArrayList<PurchaseDetailedData> purchaseList = new ArrayList<PurchaseDetailedData>();
				
				try {
					while(rs.next()) {
						PurchaseDetailedData p = new PurchaseDetailedData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5));
						purchaseList.add(p);
						LOG.debug("added to purchase list: " + p);
					}
									
				} catch (SQLException e1) {
					LOG.error("Error while making query to join three tables", e1);
					JOptionPane.showMessageDialog(MainFrame.this, "Error while downloading purchase data", "Error", 3);;
				}
				
				// sort purchase by last name if checkbox for sort by last name is selected
				if (purchaseByLastName.isSelected()) {
					if (purchaseSortByDescending.isSelected()) {
						LOG.debug("sorting purchase list by last name descending order");
						purchaseList.sort((p1, p2) -> p2.getLastName().compareTo(p1.getLastName()));
					} else {
						LOG.debug("sorting purchase list by last name ascending order");
						purchaseList.sort((p1, p2) -> p1.getLastName().compareTo(p2.getLastName()));
					}
				}
				// Sort purchase by title if checkbox for sort by title is selected
				if (purchaseSortByTitle.isSelected()) {

					if (purchaseSortByDescending.isSelected()) {
						LOG.debug("sorting purchase list by book title in descending order");
						purchaseList.sort((p1, p2) -> p2.getBookTitle().compareTo(p1.getBookTitle()));
					} else {
						LOG.debug("sorting purchase list by book title in ascending order");
						purchaseList.sort((p1, p2) -> p1.getBookTitle().compareTo(p2.getBookTitle()));
					}
				}
				

				new PurchaseList(purchaseList, idToFilter);
			}
		});
		listPurchase.setMnemonic('L');
		purchaseMenu.add(listPurchase);

	}

	// add submenus to Help Menu
	public void addHelpSubMenus() {

		// Help - add About
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, "COMP 2613 Assignment 2\nClara Fok A00957354",
						"Clara Fok Assignment 2", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		about.setMnemonic('A');
		about.setAccelerator(KeyStroke.getKeyStroke("F1"));
		helpMenu.add(about);
	}

	
	//method to download data from the database by joining tables through id's
	public ResultSet downloadData(PurchaseDao purchaseDao) {
		
		ResultSet rs = null;
		
		try {
			
			String sql = "SELECT A00957354_Customers.customer_id, A00957354_Customers.firstName, A00957354_Customers.lastName, A00957354_Books.original_title, price "
						+ "FROM ((A00957354_Purchases "
						+ "INNER JOIN A00957354_Customers ON A00957354_Purchases.customer_id = A00957354_Customers.customer_id)"
						+ "INNER JOIN A00957354_Books ON A00957354_Purchases.book_id = A00957354_Books.book_id)";
			
			rs = purchaseDao.makeQuery(sql);
			
			
		} catch (ApplicationException e2) {
			LOG.error("Error while making query to join three tables", e2);
			JOptionPane.showMessageDialog(MainFrame.this, "Failed to retrieve purchase details", "Warning", 2);
		}
		return rs;
	}
	
}
