package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ShowCustomer implements Command {

	private final int id;
	
	public ShowCustomer(int id) {
		this.id = id;
	}
	
	@Override
	public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
		Customer c = flightBookingSystem.getCustomerByID(id);
		System.out.println(c.getDetailsLong(flightBookingSystem)); //finds the customer by id and outputs their detailed info.
	}
}
