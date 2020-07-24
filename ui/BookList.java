/**
 * Project: a00957354_ass2
 * File: BookList.java
 * Date: Jun. 23, 2020
 * Time: 1:16:28 p.m.
 */
package a00.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Book;
import a00.data.util.Logging;
import a00.io.BookReport;

/**
 * @author Clara Fok, A00957354
 *
 */
@SuppressWarnings("serial")
public class BookList extends JFrame {

	
	private static Logger LOG;
	
	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(BookReport.class);
	}
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					BookList frame = new BookList();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public BookList(ArrayList<Book> books) {
				
		super("List of Books in Bookstore");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTextArea textArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        textArea.setFont(textArea.getFont().deriveFont(12f));
        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setSize(1000, 500);
        setLocationByPlatform(true);
        setVisible(true);
        
        printBookDetail(books, textArea);
		
		
	}
	
	
	//prints book details on jtextarea
	public void printBookDetail(ArrayList<Book> books, JTextArea textArea) {
		LOG.debug("Printing List of Books");
		String heading = String.format(
				"%-80s %s %n ==================================================================================================================%n",
				"Authors", "Book Title");
		textArea.append(heading);
		for(Book b : books) {
			String s = String.format("%-80s %s%n", b.getAuthors(), b.getOriginal_title() );
			textArea.append(s);
		}				
	}

}
