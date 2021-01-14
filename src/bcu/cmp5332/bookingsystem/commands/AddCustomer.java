package bcu.cmp5332.bookingsystem.commands;

import java.io.IOException;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class AddCustomer implements Command {

    private final String name;
    private final String phone;
    private final String email;

    public AddCustomer(String name, String phone,String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException, IOException {
    	if(FlightBookingSystemData.isCustomerAccessible()) { //checks if the file is accessible.
			int maxId = 0;
	          if (flightBookingSystem.getCustomers().size() > 0) {  //will try to find the last id 
	              int lastIndex = flightBookingSystem.getCustomers().size() - 1;
	              maxId = flightBookingSystem.getCustomers().get(lastIndex).getId();
	          }
	        
	          Customer customer = new Customer(++maxId, name, phone,email);
	          flightBookingSystem.addCustomer(customer); //adds customer to the system.
	          System.out.println("Customer #" + customer.getId() + " added.");
	          FlightBookingSystemData.store(flightBookingSystem); //stores data after the command was executed.
		}else {
			System.out.println("[ERROR]Command cannot be executed. File is not accessible.");
		}
    }
}
