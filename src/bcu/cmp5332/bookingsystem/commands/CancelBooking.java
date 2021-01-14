package bcu.cmp5332.bookingsystem.commands;

import java.io.IOException;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class CancelBooking implements Command {

	private final int customerID;
	private final int flightID;
	
	
	public CancelBooking(int customerID,int flightID) {
		this.customerID = customerID;
		this.flightID = flightID;
	}
	
	@Override
	public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException, IOException {
		if(FlightBookingSystemData.isBookingAccessible()) { //checks if the file is accessible.
			Customer customer = flightBookingSystem.getCustomerByID(customerID);
	        Flight flight = flightBookingSystem.getFlightByID(flightID);
	        if(customer.cancelBookingForFlight(flight)) { //if the cancelBooking method returns true, meaning it has worked it will remove the customer from the flight.
	        	System.out.println(customer.getName() + " has been removed from flight no: " + flight.getFlightNumber()+".");
	        	flight.removePassenger(customer); //removes customer from the list of passengers.
	        	FlightBookingSystemData.store(flightBookingSystem); //Updates file to reflect change made ot the system.
	        }else {
	        	System.out.println(customer.getName() + " does not have the booking");
	        }
		}else {
			System.out.println("[ERROR]Command cannot be executed. File is not accessible");
		}
	}

}
