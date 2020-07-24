/**
 * Project: a00957354_ass2
 * File: PurchaseList.java
 * Date: Jun. 23, 2020
 * Time: 1:55:15 p.m.
 */
package a00.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00.data.PurchaseDetailedData;
import a00.data.util.Logging;
import a00.io.BookReport;

/**
 * @author Clara Fok, A00957354
 *
 */
@SuppressWarnings("serial")
public class PurchaseList extends JFrame {
	
	private static Logger LOG;
	
	static {
		Logging.configureLogging();
		LOG = LogManager.getLogger(BookReport.class);
	}
	
	public PurchaseList(List<PurchaseDetailedData> purchaseList, int idToFilter) {
		
		super("List of Purchases");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTextArea textArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        textArea.setFont(textArea.getFont().deriveFont(12f));
        textArea.setLineWrap(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);


        setSize(1000, 500);
        setLocationByPlatform(true);
        setVisible(true);
        
        printPurchaseDetail(purchaseList, idToFilter, textArea);
 
        
		
		{
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}

		}
		
	}
	
	public void printPurchaseDetail(List<PurchaseDetailedData> purchaseList, int idToFilter, JTextArea textArea) {
		
		
		LOG.debug("Printing purchase detail");
		textArea.append(String.format(
				"%-30s %-100s %s%n ==================================================================================================================%n",
				"Customer Name", "Book Title", "Price"));
		if(idToFilter > 0) {
			boolean customerFound = false;
			
			for (PurchaseDetailedData p : purchaseList) {
				if (p.getCustomerID() == idToFilter) {
					customerFound = true;
					textArea.append(String.format("%s %-30s %-80s $%.2f%n", p.getFirstName(), p.getLastName(), p.getBookTitle(), p.getPrice()));
				}
			}
			
			if(customerFound != true) {
				JOptionPane.showMessageDialog(PurchaseList.this, "Customer hasn't purchased from us");
			}
		} else {			
	
			for (PurchaseDetailedData p : purchaseList) {
				textArea.append(String.format("%s %-30s %-80s $%.2f%n", p.getFirstName(), p.getLastName(), p.getBookTitle(), p.getPrice()));		
			}
		}
		
	}
	




}
