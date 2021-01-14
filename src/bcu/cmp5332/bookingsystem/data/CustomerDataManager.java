package bcu.cmp5332.bookingsystem.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class CustomerDataManager implements DataManager {

    private final String RESOURCE = "./resources/data/customers.txt";
    
    @Override
    public boolean loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
    	 try (Scanner sc = new Scanner(new File(RESOURCE))) {
             int line_idx = 1;
             if(sc.hasNext()) { //checks if the file is empty.
            	 while (sc.hasNextLine()) { //checks if the next line is not empty.
                     String line = sc.nextLine();
                     if(!line.isEmpty()) { //if the line is not empty it will start gathering the information from the line.
                    	 String[] properties = line.split(SEPARATOR, -1);
                         try {
                             int id = Integer.parseInt(properties[0]); // records customer info from file.
                             String name = properties[1];
                             String phoneNumber = properties[2];
                             String email = properties[3];
                             int isDeletedNum = Integer.parseInt(properties[4]);
     	                     Customer c;
     	                     if(isDeletedNum != 0) { //checks if the customer is deleted. if so it will use another constructor which will specify the customer has already been deleted.
     	                    	 c = new Customer(id,name,phoneNumber,email,true);
     	                     }else {
     	                    	 c = new Customer(id,name,phoneNumber,email);
     	                     }
                             fbs.addCustomer(c); //adds customer to the system
                         } catch (NumberFormatException ex) {
                             throw new FlightBookingSystemException("Unable to parse customer id " + properties[0] + " on line " + line_idx
                                 + "\nError: " + ex);
                         }
                         line_idx++;
                     }
                 }return true;
             }else {
            	System.out.println("[ERROR]No customers could be retrieved.");
             	return false;
             }
         }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
    	if(FlightBookingSystemData.isCustomerAccessible()) { // checks if the file can be opened, and written on.
	    	try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
	            for (Customer c : fbs.getCustomers()) { //stores data in file.
	                out.print(c.getId() + SEPARATOR);
	                out.print(c.getName() + SEPARATOR);
	                out.print(c.getPhone() + SEPARATOR);
	                out.print(c.getEmail() + SEPARATOR);
	                if(c.isDeleted()) {
                    	out.print(1 + SEPARATOR);
                    }else {
                    	out.print(0 + SEPARATOR);
                    }
	                out.println();
	            }
	        }
    	}
    }

}
