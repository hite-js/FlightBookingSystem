package bcu.cmp5332.bookingsystem.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;

public class ListBookingCustomer extends JFrame implements ActionListener{

	private String cName;
	private MainWindow mw;
	private JMenuBar menuBar;
	private JMenu bookingMenu;
	
	private JMenuItem bookingAdd;
	private JMenuItem bookingUpdate;
	private JMenuItem bookingDelete;
	
	private JTable bookingTable;
	private List<Booking> cBooking;
	private Customer cus;
	
	public ListBookingCustomer(MainWindow mw, String cName) {
		this.mw = mw;
		this.cName = cName;
		
        initialize();
	}

	private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {

        }
        
        List<Customer> customers = mw.getFlightBookingSystem().getCustomers();
       
        for(Customer c : customers) {
        	if(c.getName() == cName) {
        		cBooking = c.getBookings();
        		cus = c;
        	}
        }
        
        setTitle("Bookings for "+ cName);

        setSize(800, 500);
    	menuBar = new JMenuBar();
		setJMenuBar(menuBar);
        
		bookingMenu = new JMenu("Booking");
		menuBar.add(bookingMenu);
		
		bookingAdd = new JMenuItem("Add");
		bookingUpdate = new JMenuItem("Update");
		bookingDelete = new JMenuItem("Delete");
		bookingMenu.add(bookingAdd);
		bookingMenu.add(bookingUpdate);
		bookingMenu.add(bookingDelete);
        
		for (int i = 0; i < bookingMenu.getItemCount(); i++) {
			bookingMenu.getItem(i).addActionListener(this);
		}
		
		displayBookings();
        setLocationRelativeTo(mw);
        this.getContentPane().removeAll();
		this.getContentPane().add(new JScrollPane(bookingTable));
		this.revalidate();
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae){
		// TODO Auto-generated method stub
	if (ae.getSource() == bookingAdd) {
		new AddBookingWindow(mw,this,cus);
		
		
	} else if (ae.getSource() == bookingUpdate) {
		int row = bookingTable.getSelectedRow();
		if (row != -1) {
			int bid = (int)bookingTable.getValueAt(row, 0);
			new UpdateBookingWindow(mw,this,bid);	
		}else {
			JOptionPane.showMessageDialog(this, "No Booking Selected", "Error", JOptionPane.ERROR_MESSAGE);
		}
		

	}else if (ae.getSource() == bookingDelete) {
		int row = bookingTable.getSelectedRow();
		if (row != -1) {
			int bid = (int)bookingTable.getValueAt(row, 0);
			try {
				Booking book = mw.getFlightBookingSystem().getBookingByID(bid);
				Command cancelBook = new CancelBooking(book.getCustomer().getId(),book.getFlight().getId());
				cancelBook.execute(mw.getFlightBookingSystem());
				displayBookings();
				mw.displayCustomers();
			} catch (FlightBookingSystemException | IOException e) {}
		}else {
			JOptionPane.showMessageDialog(this, "No Booking Selected", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
    public void displayBookings() {
    	
    	String[] columns = new String[]{"Booking ID","Booking Date", "Flight ID", "Flight Number","Origin","Destination","Departure Date","Expired"};
		Object[][] data = new Object[cBooking.size()][9];
		for (int i = 0; i < cBooking.size(); i++) {
			Booking b = cBooking.get(i);
			data[i][0] = b.getId();
			data[i][1] = b.getBookingdate();
			data[i][2] = b.getFlight().getId();
			data[i][3] = b.getFlight().getFlightNumber();
			data[i][4] = b.getFlight().getOrigin();
			data[i][5] = b.getFlight().getDestination();
			data[i][6] = b.getFlight().getDepartureDate();
			if(b.isCompleted(mw.getFlightBookingSystem())) {
				data[i][7] = "\u2713"; //Unicode character for check mark.
			}else {
				data[i][7] = "X";
			}
			
		}

		bookingTable = new JTable(data, columns);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        bookingTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		this.getContentPane().removeAll();
		this.getContentPane().add(new JScrollPane(bookingTable));
		this.revalidate();
    } 
}
