package bcu.cmp5332.bookingsystem.gui;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;

public class ListFlightCustomers extends JFrame  {
	
	private String flightNum;
	private MainWindow mw;
	
	public ListFlightCustomers(MainWindow mw, String flightNum) {
		this.mw = mw;
		this.flightNum = flightNum;
        initialize();
	}

	private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {

        }
        
        List<Flight> flights = mw.getFlightBookingSystem().getFlights();
        List<Customer> passengers = null;
        for(Flight f : flights) {
        	if(f.getFlightNumber() == flightNum) {
        		passengers = f.getPassengers();
        	}
        }
        setTitle("Passengers for flight number "+ flightNum);

        setSize(500, 220);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        if(!passengers.isEmpty()) {
        	for(Customer c : passengers) {
            	panel.add(new JLabel(c.getDetailsShort()));
            }
        }else {
        	panel.add(new JLabel("This flight does not have any passengers"));
        }
        


        this.getContentPane().add(panel);
        setLocationRelativeTo(mw);
        setVisible(true);
	}
}
