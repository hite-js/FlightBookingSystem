
package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

public class ListCustomers implements Command { //CLASS THAT I ADDED

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        List<Customer> customers = flightBookingSystem.getCustomers(); //gets the customer list aka all the customers in the system and loops through each customer and outputs its details.
        for (Customer customer : customers) {
        	if(!customer.isDeleted()) { //added checks if customer been deleted.
        		 System.out.println(customer.getDetailsShort());	
        	}
        }
        System.out.println(customers.size() + " customer(s)");
    }
}
