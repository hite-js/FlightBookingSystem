package bcu.cmp5332.bookingsystem.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

public class AddCustomerWindow extends JFrame implements ActionListener{

	private MainWindow mw;
	private JTextField nameText = new JTextField();
	private JTextField phoneNumberText = new JTextField();
	private JTextField emailText = new JTextField();

	private JButton addBtn = new JButton("Add");
	private JButton cancelBtn = new JButton("Cancel");

	public AddCustomerWindow(MainWindow mw) {
		this.mw = mw;
		initialize();
	}

	private void initialize() { //sets the layout of the frame.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {

		}

		setTitle("Add a New Book");

		setSize(350, 220);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(4, 2));
		topPanel.add(new JLabel("Customer Name: "));
		topPanel.add(nameText);
		topPanel.add(new JLabel("Customer Phone Number: "));
		topPanel.add(phoneNumberText);
		topPanel.add(new JLabel("Customer Email: "));
		topPanel.add(emailText);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 3));
		bottomPanel.add(new JLabel("     "));
		bottomPanel.add(addBtn);
		bottomPanel.add(cancelBtn);

		addBtn.addActionListener(this);
		cancelBtn.addActionListener(this);

		this.getContentPane().add(topPanel, BorderLayout.CENTER);
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(mw);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addBtn) {
			addCust();
		} else if (e.getSource() == cancelBtn) {
			this.setVisible(false);
		}

	}

	private void addCust() {
		try {
			String name = nameText.getText();
			if (!name.matches("^[a-zA-Z-\\s]+")) { //checks if string input does contain letters, dashes or spaces aka numbers or other punctuation  input and outputs an exception.
				throw new FlightBookingSystemException("Invalid name entered. Name Can Only Contain Letters.");
			}
			String phoneNumber = phoneNumberText.getText();
			try {
	            Double num = Double.parseDouble(phoneNumber); //tries to convert phonenumber string into a double.
	        } catch (NumberFormatException e) {
	        	throw new FlightBookingSystemException("Phonenumber Can Only Contain Numbers");
	        }
			String email = emailText.getText();
			String validEmailformat = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
			if(!email.matches(validEmailformat)) { // checks if the email entered is a valid email format.
				throw new FlightBookingSystemException("Invalid Email Entered");
			}
			Command addCustomer = new AddCustomer(name,phoneNumber,email);
			addCustomer.execute(mw.getFlightBookingSystem());
			mw.displayCustomers(); // refreshes the customer list.
			this.setVisible(false);
		} 
		catch (FlightBookingSystemException | IOException ex) {
			JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
