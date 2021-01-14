package bcu.cmp5332.bookingsystem.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class MainWindow extends JFrame implements ActionListener, MouseListener {

	private JMenuBar menuBar;
	private JMenu adminMenu;
	private JMenu flightsMenu;
	private JMenu bookingsMenu;
	private JMenu customersMenu;

	private JMenuItem adminExit;

	private JMenuItem flightsView;
	private JMenuItem flightsAdd;
	private JMenuItem flightsDel;

	private JMenuItem bookingsIssue;
	private JMenuItem bookingsUpdate;
	private JMenuItem bookingsCancel;

	private JMenuItem custView;
	private JMenuItem custAdd;
	private JMenuItem custDel;

	private JTable flightTable;
	private JTable customerTable;

	private FlightBookingSystem fbs;

	public MainWindow(FlightBookingSystem fbs) {

		initialize();
		this.fbs = fbs;
	}

	public FlightBookingSystem getFlightBookingSystem() {
		return fbs;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {

		}

		setTitle("Flight Booking Management System");

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		//adding adminMenu menu and menu items
		adminMenu = new JMenu("Admin");
		menuBar.add(adminMenu);

		adminExit = new JMenuItem("Exit");
		adminMenu.add(adminExit);
		adminExit.addActionListener(this);

		// adding Flights menu and menu items
		flightsMenu = new JMenu("Flights");
		menuBar.add(flightsMenu);

		flightsView = new JMenuItem("View");
		flightsAdd = new JMenuItem("Add");
		flightsDel = new JMenuItem("Delete");
		flightsMenu.add(flightsView);
		flightsMenu.add(flightsAdd);
		flightsMenu.add(flightsDel);
		// adding action listener for Flights menu items
		for (int i = 0; i < flightsMenu.getItemCount(); i++) {
			flightsMenu.getItem(i).addActionListener(this);
		}

		// adding Bookings menu and menu items
		bookingsMenu = new JMenu("Bookings");

		bookingsIssue = new JMenuItem("Issue");
		bookingsUpdate = new JMenuItem("Update");
		bookingsCancel = new JMenuItem("Cancel");
		bookingsMenu.add(bookingsIssue);
		bookingsMenu.add(bookingsUpdate);
		bookingsMenu.add(bookingsCancel);
		// adding action listener for Bookings menu items
		for (int i = 0; i < bookingsMenu.getItemCount(); i++) {
			bookingsMenu.getItem(i).addActionListener(this);
		}

		// adding Customers menu and menu items
		customersMenu = new JMenu("Customers");
		menuBar.add(customersMenu);

		custView = new JMenuItem("View");
		custAdd = new JMenuItem("Add");
		custDel = new JMenuItem("Delete");

		customersMenu.add(custView);
		customersMenu.add(custAdd);
		customersMenu.add(custDel);
		// adding action listener for Customers menu items
		custView.addActionListener(this);
		custAdd.addActionListener(this);
		custDel.addActionListener(this);

		setSize(800, 500);

		setVisible(true);
		setAutoRequestFocus(true);
		toFront();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		/* Uncomment the following line to not terminate the console app when the window is closed */
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);        

	}	

	public static void main(String[] args) throws IOException, FlightBookingSystemException {
		FlightBookingSystem fbs = FlightBookingSystemData.load();
		new MainWindow(fbs);			
	}



	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == adminExit) {
			try {
				FlightBookingSystemData.store(fbs);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
			}
			System.exit(0);
		} else if (ae.getSource() == flightsView) {
			displayFlights();

		} else if (ae.getSource() == flightsAdd) {
			new AddFlightWindow(this);

		} else if (ae.getSource() == flightsDel) {
			if(flightTable != null) {
				int flightRow = flightTable.getSelectedRow();
				if(flightRow != -1) {
					String flightNumber = (String)flightTable.getValueAt(flightRow, 0);
					try {
						deleteFlight(flightNumber);
					} catch (IOException | FlightBookingSystemException e) {
						System.out.println("[ERROR]File is not accessible");
					}
				}else {
					JOptionPane.showMessageDialog(this, "No flight selected","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (ae.getSource() == bookingsIssue) {


		} else if (ae.getSource() == bookingsCancel) {


		} else if (ae.getSource() == custView) {
			displayCustomers();

		} else if (ae.getSource() == custAdd) {
			new AddCustomerWindow(this);

		} else if (ae.getSource() == custDel) {
			if(customerTable != null) {
				int customerRow = customerTable.getSelectedRow();
				if(customerRow != -1) {
					String customerNum = (String)customerTable.getValueAt(customerRow, 1);
					try {
						deleteCustomer(customerNum);
					} catch (IOException | FlightBookingSystemException e) {
						System.out.println("[ERROR]File is not accessible");
					}
				}else {
					JOptionPane.showMessageDialog(this, "No customer selected","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	/**
	 * Removes the customer from the flight booking system. By setting its isDeleted attribute to true, then storing this change to the system.
	 * 
	 * @param customerNum -  String which allows the method to uniquely identify and retrieve the customer.
	 * @throws   IOException - If the file is not accessible.
	 * @throws FlightBookingSystemException - if the passenger does not exist in the flight.
	 */
	public void deleteCustomer(String customerNum) throws IOException, FlightBookingSystemException {
		if(FlightBookingSystemData.isCustomerAccessible()) {
			Customer selectedCustomer = null;
			for(Customer c : fbs.getCustomers()) {
				if(c.getPhone() == customerNum) {
					selectedCustomer = c;
				}
			}
			selectedCustomer.setDeleted(true);
			for (Flight f: getFlightBookingSystem().getFlights()) {
				for(Customer c:f.getPassengers()) {
					if (c == selectedCustomer) {
						f.removePassenger(c);
					}
				}
			}
			selectedCustomer.deleteBookings();
			FlightBookingSystemData.store(fbs);
			displayCustomers();
		}else {
			JOptionPane.showMessageDialog(this, "File is not accessible","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Removes the flight from the flight booking system. By setting its isDeleted attribute to true, And cancelling all of the bookings linked to the flight then storing this change to the system.
	 * 
	 * @param flightNumber -  String which allows the method to uniquely identify and retrieve the customer.
	 * @throws   IOException - If the file is not accessible.
	 * @throws FlightBookingSystemException  If the booking or flight does not exist.
	 */
	public void deleteFlight(String flightNumber) throws IOException, FlightBookingSystemException  {
		if(FlightBookingSystemData.isFlightAccessible()) {
			Flight selectedFlight = null;
			for(Flight f : fbs.getFlights()) {
				if(f.getFlightNumber() == flightNumber) {
					selectedFlight = f;
				}
			}
			Booking temp_b = null;
			for (Customer c: getFlightBookingSystem().getCustomers()) {
				for(Booking b:c.getBookings()) {
					if (b.getFlight() == selectedFlight) {
						temp_b = b;
					}
				}
				if(temp_b != null) {
					Command cancelBooking = new CancelBooking(temp_b.getCustomer().getId(),selectedFlight.getId());
					cancelBooking.execute(getFlightBookingSystem());
				}
			}
			selectedFlight.setDeleted(true);
			FlightBookingSystemData.store(fbs);
			displayFlights();
		}else {
			JOptionPane.showMessageDialog(this, "File is not accessible","Error",JOptionPane.ERROR_MESSAGE);
		}
	}

	public void displayFlights() {
		List<Flight> t_flightsList = new ArrayList<>(fbs.getFlights());
		List<Flight> flightsList = new ArrayList<>();
		// headers for the table
		String[] columns = new String[]{"Flight No", "Origin", "Destination", "Departure Date","Seats","Price","Passengers"};
		Iterator<Flight> flightIterator = t_flightsList.iterator();
		while(flightIterator.hasNext()) {
			Flight f = flightIterator.next();
			if(!f.isDeleted()) {
				LocalDate flightDate = f.getDepartureDate();
				LocalDate systemDate = fbs.getSystemDate(); 
				if(flightDate.compareTo(systemDate) == 1 || flightDate.isAfter(systemDate) ) {
					flightsList.add(f);
				}
			}
		}
		Object[][] data = new Object[flightsList.size()][7];
		for (int i = 0; i < flightsList.size(); i++) {
			Flight flight = flightsList.get(i);
			data[i][0] = flight.getFlightNumber();
			data[i][1] = flight.getOrigin();
			data[i][2] = flight.getDestination();
			data[i][3] = flight.getDepartureDate();
			data[i][4] = flight.getSeats();
			data[i][5] = "£" + flight.getPrice();
			data[i][6] = "View passengers";
		}
		flightTable = new JTable(data, columns);
		flightTable.addMouseListener(this);
		
		
		this.getContentPane().removeAll();
		this.getContentPane().add(new JScrollPane(flightTable));
		this.revalidate();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == flightTable) {
			int row = flightTable.rowAtPoint(e.getPoint());
			int col = flightTable.columnAtPoint(e.getPoint());
			if (col == 6) {
				final String flightNum = (String)flightTable.getValueAt(row, 0);
				if(flightNum != null) {
					new ListFlightCustomers(this,flightNum);
				}
			}
		}else if(e.getSource() == customerTable) {
			int row = customerTable.rowAtPoint(e.getPoint());
			int col = customerTable.columnAtPoint(e.getPoint());
			if (col == 4) {	
				final String cName = (String)customerTable.getValueAt(row, 0);
				new ListBookingCustomer(this,cName);
			}
		}


	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	public void displayCustomers() {
		List<Customer> t_cList = new ArrayList<>(fbs.getCustomers());
		List<Customer> cList = new ArrayList<>();
		// headers for the table
		String[] columns = new String[]{"Name", "Phone Number", "Email","Num of Bookings","View Bookings"};

		Iterator<Customer> custIterator = t_cList.iterator();
		while(custIterator.hasNext()) {
			Customer c = custIterator.next();
			if(!c.isDeleted()) {
				cList.add(c);
			}
		}

		Object[][] data = new Object[cList.size()][6];
		for (int i = 0; i < cList.size(); i++) {
			Customer c = cList.get(i);
			data[i][0] = c.getName();
			data[i][1] = c.getPhone();
			data[i][2] = c.getEmail();
			data[i][3] = c.getBookings().size();
			data[i][4] = "View Bookings";
		}

		customerTable = new JTable(data, columns);
		customerTable.addMouseListener(this);
		this.getContentPane().removeAll();
		this.getContentPane().add(new JScrollPane(customerTable));
		this.revalidate();
	}



}
