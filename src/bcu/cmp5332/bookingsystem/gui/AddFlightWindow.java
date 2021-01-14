package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
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

public class AddFlightWindow extends JFrame implements ActionListener {

	private MainWindow mw;
	private JTextField flightNoText = new JTextField();
	private JTextField originText = new JTextField();
	private JTextField destinationText = new JTextField();
	private JTextField depDateText = new JTextField();
	private JTextField priceText = new JTextField();
	private JTextField seatsText = new JTextField();

	private JButton addBtn = new JButton("Add");
	private JButton cancelBtn = new JButton("Cancel");

	public AddFlightWindow(MainWindow mw) {
		this.mw = mw;
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

		setTitle("Add a New Book");

		setSize(350, 220);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(7, 2));
		topPanel.add(new JLabel("Flight No : "));
		topPanel.add(flightNoText);
		topPanel.add(new JLabel("Origin : "));
		topPanel.add(originText);
		topPanel.add(new JLabel("Destination : "));
		topPanel.add(destinationText);
		topPanel.add(new JLabel("Departure Date (YYYY-MM-DD) : "));
		topPanel.add(depDateText);
		topPanel.add(new JLabel("Seats "));
		topPanel.add(seatsText);
		topPanel.add(new JLabel("Price(£): "));
		topPanel.add(priceText);

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
			String flightNumber = flightNoText.getText();
			String origin = originText.getText();
			String destination = destinationText.getText();
			int seats = Integer.parseInt(seatsText.getText());
			float price = Float.parseFloat(priceText.getText());
			LocalDate departureDate = null;
			if (!origin.matches("^[a-zA-Z-\s]+")) {
				throw new FlightBookingSystemException("Origin can only contain letters. No numbers");
			}
			if (!destination.matches("^[a-zA-Z-\s]+")) {
				throw new FlightBookingSystemException("Destination can only contain letters.No numbers");
			}

			try {
				departureDate = LocalDate.parse(depDateText.getText());
			}
			catch (DateTimeParseException dtpe) {
				throw new FlightBookingSystemException("Date must be in YYYY-DD-MM format");
			}
			// create and execute the AddFlight Command
			Command addFlight = new AddFlight(flightNumber, origin, destination, departureDate,seats,price);
			addFlight.execute(mw.getFlightBookingSystem());
			// refresh the view with the list of flights
			mw.displayFlights();
			// hide (close) the AddFlightWindow
			this.setVisible(false); 
		} catch (FlightBookingSystemException | IOException ex ) {
			JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
		} catch( NumberFormatException e ) {
			seatsText.setText("");
			priceText.setText("");
			JOptionPane.showMessageDialog(this, "Invalid characters detected for Price or Seats", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
