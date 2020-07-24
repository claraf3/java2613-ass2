/**
 * Project: a00957354_lab09
 * File: CustomerDialog.java
 * Date: Jun. 20, 2020
 * Time: 11:40:14 a.m.
 */
package a00.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.Customer;
import a00.data.util.ApplicationException;
import a00.data.util.Logging;
import a00.database.dao.CustomerDao;
import net.miginfocom.swing.MigLayout;

/**
 * @author Clara Fok, A00957354
 *
 */
@SuppressWarnings("serial")
public class CustomerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField id;
	private JTextField lastName;
	private JTextField street;
	private JTextField city;
	private JTextField postalCode;
	private JTextField phone;
	private JTextField email;
	private JTextField joinedDate;

	private boolean inputChanged = false;
	private static final Logger LOG;
	private JTextField firstName;

	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(CustomerDialog.class);
	}

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			CustomerDialog dialog = new CustomerDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public CustomerDialog(Customer c, CustomerDao customerDao, DefaultListModel<String> model, int selectedIndex) {
		setBounds(100, 100, 511, 468);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		setLocationRelativeTo(null);
		contentPanel.setLayout(new MigLayout("", "[][80.00][26.00][245.00,grow][45.00]", "[25.00][][][][][][][][][]"));
		
		//Display customer's info
		displayCustomer(c);
		
		
		//Dialog JButtons
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			
			//OK Button 
			//Calls method to update customer data if input fields have been changed
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(inputChanged == true) {
							int confirm = (JOptionPane.showConfirmDialog(CustomerDialog.this, "Are you sure you want to update customer?", "Warning", 0, 2));
							if(confirm == 0) {
								updateCustomer(c, customerDao, model, selectedIndex);
							}
							inputChanged = false;
						}
						dispose();
					}	
				}
				);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			
			//Cancel Button
			//Closes JDialog
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");buttonPane.add(cancelButton);
			}

		}
	}

	/*
	 * method to display customer Data
	 * 
	 * @param c for customer to be displayed
	 */
	public void displayCustomer(Customer c) {
		
		LOG.debug("Displaying customer: " + c);
		//id field
				{
					JLabel lblNewLabel = new JLabel("ID");
					contentPanel.add(lblNewLabel, "cell 1 1");
				}
				{
					id = new JTextField();		
					id.setEditable(false);
					id.setText(Long.toString(c.getCustomerID()));
					
					contentPanel.add(id, "cell 3 1,growx");
					id.setColumns(10);
				}
				
				//first name field
				{
					JLabel lblNewLabel_1 = new JLabel("First Name");
					contentPanel.add(lblNewLabel_1, "cell 1 2");
				}
				{
					firstName = new JTextField();
					firstName.getDocument().addDocumentListener(getInputListener());
					firstName.setText(c.getFirstName());
					contentPanel.add(firstName, "cell 3 2,growx");
					firstName.setColumns(10);
				}

					
				
				//last name field
				{
					JLabel lblNewLabel_2 = new JLabel("Last Name");
					contentPanel.add(lblNewLabel_2, "cell 1 3");
				}
				{
					lastName = new JTextField();
					lastName.getDocument().addDocumentListener(getInputListener());
					lastName.setText(c.getLastName());
					contentPanel.add(lastName, "cell 3 3,growx");
					lastName.setColumns(10);
				}
				
				//street field
				{
					JLabel lblNewLabel_3 = new JLabel("Street");
					contentPanel.add(lblNewLabel_3, "cell 1 4");
				}
				{
					street = new JTextField();
					street.getDocument().addDocumentListener(getInputListener());
					street.setText(c.getStreetName());
					contentPanel.add(street, "cell 3 4,growx");
					street.setColumns(10);
				}
				
				//city field
				{
					JLabel lblNewLabel_4 = new JLabel("City");
					contentPanel.add(lblNewLabel_4, "cell 1 5");
				}
				{
					city = new JTextField();
					city.getDocument().addDocumentListener(getInputListener());
					city.setText(c.getCity());
					contentPanel.add(city, "cell 3 5,growx");
					city.setColumns(10);
				}
				
				//postal code field
				{
					JLabel lblNewLabel_5 = new JLabel("Postal Code");
					contentPanel.add(lblNewLabel_5, "cell 1 6");
				}
				{
					postalCode = new JTextField();
					postalCode.getDocument().addDocumentListener(getInputListener());
					postalCode.setText(c.getPostalCode());
					contentPanel.add(postalCode, "cell 3 6,growx");
					postalCode.setColumns(10);
				}
				
				//phone field
				{
					JLabel lblNewLabel_6 = new JLabel("Phone");
					contentPanel.add(lblNewLabel_6, "cell 1 7");
				}
				{
					phone = new JTextField();
					phone.getDocument().addDocumentListener(getInputListener());
					phone.setText(c.getPhoneNumber());
					contentPanel.add(phone, "cell 3 7,growx");
					phone.setColumns(10);
				}
				
				//email field
				{
					JLabel lblNewLabel_7 = new JLabel("Email");
					contentPanel.add(lblNewLabel_7, "cell 1 8");
				}
				{
					email = new JTextField();
					email.getDocument().addDocumentListener(getInputListener());
					email.setText(c.getEmail());
					contentPanel.add(email, "cell 3 8,growx");
					email.setColumns(10);
				}
				
				//joined date field
				{
					JLabel lblNewLabel_8 = new JLabel("Joined Date");
					contentPanel.add(lblNewLabel_8, "cell 1 9");
				}
				{
					joinedDate = new JTextField();
					joinedDate.getDocument().addDocumentListener(getInputListener());
					joinedDate.setText(c.getJoinDate().toString());
					contentPanel.add(joinedDate, "cell 3 9,growx");
					joinedDate.setColumns(10);
				}
	}

	/*
	 * method to update customer data based on field inputs
	 * 
	 * @param c for customer to be updated
	 * 
	 * @param customerDao for table to be updated
	 */
	public void updateCustomer(Customer c, CustomerDao customerDao, DefaultListModel<String> model, int selectedIndex) {
		LOG.debug("Updating Customer" + c);
		// get updated customer information
		c.setFirstName(firstName.getText());
		c.setLastName(lastName.getText());
		c.setStreetName(street.getText());
		c.setCity(city.getText());
		c.setPostalCode(postalCode.getText());
		c.setPhoneNumber(phone.getText());
		c.setEmail(email.getText());
		
		//update customer name on list 
		String s = String.format("%-10s %-32s %s", c.getCustomerID(), c.getFirstName(), c.getJoinDate().toString());
		model.set(selectedIndex, s);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate newDate = LocalDate.parse(joinedDate.getText(), formatter);
		c.setJoinDate(newDate);

		//update customer table with new data
		try {
			customerDao.updateCustomer(c);
			LOG.debug("Updated Customer: " + c);
		} catch (ApplicationException e) {
			LOG.error("Fail to update customer info", e);
		}
	}

	/*
	 * method to create listener whether customer data has been changed
	 */
	public DocumentListener getInputListener() {

		DocumentListener listener = new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {}

			@Override
			public void removeUpdate(DocumentEvent e) {
				inputChanged = true;
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				inputChanged = true;
			}
		};
		return listener;

	}

}
