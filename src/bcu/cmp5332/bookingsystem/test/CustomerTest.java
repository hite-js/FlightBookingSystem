package bcu.cmp5332.bookingsystem.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import bcu.cmp5332.bookingsystem.data.CustomerDataManager;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class CustomerTest { //WARNING: PERFORMING THESE TESTS WILL OVERIDE/REMOVING THE EXISTING FLIGHTS AND CUSTOMERS FROM THE FILES, PLEASE AFTER PERFORMING TESTS GO TO THE BACK UP FILES TO
	//RESTORE FLIGHTS AND CUSTOMER DATA
	
	Customer hitesh = new Customer(100,"hitesh","19910","bob@mail.com");
	
	@Test
	public void getEmailTest() {
		assertEquals("bob@mail.com", hitesh.getEmail());
	}
	
	@Test
	public void setEmailTest() {
		hitesh.setEmail("warriar@mail.com");
		assertEquals("warriar@mail.com", hitesh.getEmail());
	}
	
	@Test
	public void storeEmailTest() throws FlightBookingSystemException, IOException   {
		String RESOURCE = "./resources/data/customers.txt";
		 
		FlightBookingSystem fbs = new FlightBookingSystem();
		fbs.addCustomer(hitesh);
        CustomerDataManager data = new CustomerDataManager();
		data.storeData(fbs);
		Scanner sc = new Scanner(new File(RESOURCE));
		while (sc.hasNextLine()) {
			   final String lineFromFile = sc.nextLine();
				  String[] properties = lineFromFile.split(data.SEPARATOR, -1);
				  String email = properties[3];
				  assertEquals("bob@mail.com", email);
			}
		
	}
	
	@Test
	public void loadEmailTest() throws FlightBookingSystemException, IOException   {
		String RESOURCE = "./resources/data/customers.txt";
		FlightBookingSystem fbs = new FlightBookingSystem();
		CustomerDataManager data = new CustomerDataManager();
		PrintWriter out = new PrintWriter(new FileWriter(RESOURCE)); //writing the customer info in the text file
	     out.print(hitesh.getId() + data.SEPARATOR);
         out.print(hitesh.getName() + data.SEPARATOR);
         out.print(hitesh.getPhone() + data.SEPARATOR);
         out.print(hitesh.getEmail() + data.SEPARATOR);
         if(hitesh.isDeleted()) {
         	out.print(1 + data.SEPARATOR);
         }else {
         	out.print(0 + data.SEPARATOR);
         }
         out.println();
         out.close();
		data.loadData(fbs); // loads data from text file
		 Customer c = fbs.getCustomerByID(100);
		 assertEquals(hitesh.getEmail(), c.getEmail());
	            
	    
	}


}
