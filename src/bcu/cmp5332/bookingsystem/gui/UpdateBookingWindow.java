package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.UpdateBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class UpdateBookingWindow extends JFrame implements ActionListener {

	private MainWindow mw;
	private ListBookingCustomer lbc;
	private int bid;
	private JTextField fligIDText = new JTextField();

	private JButton addBtn = new JButton("Update");
	private JButton cancelBtn = new JButton("Cancel");

	public UpdateBookingWindow(MainWindow mw, ListBookingCustomer lbc, int bid) {
		this.mw = mw;
		this.lbc = lbc;
		this.bid = bid;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {

		}

		setTitle("Update Booking#" + bid);

		setSize(350, 220);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(3, 2));


		topPanel.add(new JLabel("Flight ID : "));
		topPanel.add(fligIDText);

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
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == addBtn) {
			addBook();
		} else if (ae.getSource() == cancelBtn) {
			this.setVisible(false);
		}

	}

	private void addBook() {
		try {
			int fid = Integer.parseInt(fligIDText.getText());
			// create and execute the AddBooking Command
			Command updateBooking = new UpdateBooking(bid,fid);
			updateBooking.execute(mw.getFlightBookingSystem());
			// refresh the view with the list of Booking
			lbc.displayBookings();
			mw.displayCustomers();
			// hide (close) the AddFlightWindow
			this.setVisible(false); 
		} catch (FlightBookingSystemException | IOException ex) {
			JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
