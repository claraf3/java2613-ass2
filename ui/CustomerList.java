/**
 * Project: a00957354_ass2
 * File: CustomerList.java
 * Date: Jun. 23, 2020
 * Time: 10:47:10 a.m.
 */
package a00.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import a00.data.Customer;
import a00.data.util.ApplicationException;
import a00.database.dao.CustomerDao;
import net.miginfocom.swing.MigLayout;

/**
 * @author Clara Fok, A00957354
 *
 */
@SuppressWarnings("serial")
public class CustomerList extends JFrame {


//
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					String[] customers = {"Bobby", "Jane", "Goodall"};
//					CustomerList frame = new CustomerList(customers, customerDao);
////					frame.setTitle("List of Customers");
////					frame.setLocationRelativeTo(null); // Center the frame
////					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public CustomerList(String[] customers, CustomerDao customerDao) {
		
		
		setSize(300, 300);
		setTitle("List of Customers");
		setLocationRelativeTo(null); // Center the frame
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		//create list of customers
		DefaultListModel<String> model = new DefaultListModel<String>();
		model.addElement(String.format("%-10s %-32s %s", "ID", "Customer Name", "Join Date"));
		for(String s : customers) {
			model.addElement(s);
		}
		JList<String> list = new JList<String>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getContentPane().add(list, "cell 0 0,grow");
		setVisible(true);
		
//		//add to scroll panel
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(300, 300));
		add(listScroller, BorderLayout.WEST);
		
		//add list selection listener to Jlist
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
			    if (e.getValueIsAdjusting() == false) {

			        if (list.getSelectedIndex() == -1) {
			        	//do nothing if nothing selected
			        	
			        } else {

			        try {
			        	//retrieve selected customer
			        	String i = list.getSelectedValue().substring(0,4);
			        	int id = Integer.parseInt(i);
			        	
			        	System.out.println(id);
			        	int selectedIndex = list.getSelectedIndex();

						Customer c = customerDao.getCustomerByID(id);
						//display customer details
						CustomerDialog dialog = new CustomerDialog(c, customerDao, model, selectedIndex);
						dialog.setVisible(true);
					} 
			        catch (ApplicationException e2) {
						JOptionPane.showMessageDialog(CustomerList.this, "Error while retrieving customer info", "Error", 3);
					}
			        
			        
			        		            
			        }
			    }
			}
		});

	}
	

}
