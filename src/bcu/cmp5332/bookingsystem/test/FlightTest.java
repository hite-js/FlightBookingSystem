package bcu.cmp5332.bookingsystem.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

import org.junit.Test;

import bcu.cmp5332.bookingsystem.data.CustomerDataManager;
import bcu.cmp5332.bookingsystem.data.FlightDataManager;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class FlightTest {//WARNING: PERFORMING THESE TESTS WILL OVERIDE/REMOVING THE EXISTING FLIGHTS AND CUSTOMERS FROM THE FILES, PLEASE AFTER PERFORMING TESTS GO TO THE BACK UP FILES TO
	//RESTORE FLIGHTS AND CUSTOMER DATA
	
	Flight flight = new Flight(5, "a111", "Birmingham", "London", LocalDate.parse("2020-11-25"), 40, 23.9f);
	
	@Test
	public void getSeatsTest() {
		assertEquals(40, flight.getSeats());
	}
	
	@Test
	public void setSeatsTest() {
		flight.setSeats(400);
		assertEquals(400, flight.getSeats());
	}
	
	@Test
	public void getPriceTest() {
		assertEquals( 23.9f, flight.getPrice(),0);
	}
	
	@Test
	public void setPriceTest() {
		flight.setPrice(9999.9f);
		assertEquals(9999.9f, flight.getPrice(),0);
	}
	
	@Test
	public void storeSeatsTest() throws FlightBookingSystemException, IOException   {
		String RESOURCE = "./resources/data/flights.txt";
		 
		FlightBookingSystem fbs = new FlightBookingSystem();
		fbs.addFlight(flight);
        FlightDataManager data = new FlightDataManager();
		data.storeData(fbs);
		Scanner sc = new Scanner(new File(RESOURCE));
		while (sc.hasNextLine()) {
			   final String lineFromFile = sc.nextLine();
				  String[] properties = lineFromFile.split(data.SEPARATOR, -1);
				  int seats = Integer.parseInt(properties[5]);
				  assertEquals(40, seats);
			}
		
	}
	
	@Test
	public void storePriceTest() throws FlightBookingSystemException, IOException   {
		String RESOURCE = "./resources/data/flights.txt";
		 
		FlightBookingSystem fbs = new FlightBookingSystem();
		fbs.addFlight(flight);
        FlightDataManager data = new FlightDataManager();
		data.storeData(fbs);
		Scanner sc = new Scanner(new File(RESOURCE));
		while (sc.hasNextLine()) {
			   final String lineFromFile = sc.nextLine();
				  String[] properties = lineFromFile.split(data.SEPARATOR, -1);
				  float price = Float.parseFloat(properties[6]);
				  assertEquals(23.9f, price,0);
			}
		
	}
	
	@Test
	public void loadSeatsTest() throws FlightBookingSystemException, IOException   {
		String RESOURCE = "./resources/data/flights.txt";
		FlightBookingSystem fbs = new FlightBookingSystem();
		   FlightDataManager data = new FlightDataManager();
		PrintWriter out = new PrintWriter(new FileWriter(RESOURCE)); //writing the custoemr info in the text fike
        out.print(flight.getId() + data.SEPARATOR);
        out.print(flight.getFlightNumber() + data.SEPARATOR);
        out.print(flight.getOrigin() + data.SEPARATOR);
        out.print(flight.getDestination() + data.SEPARATOR);
        out.print(flight.getDepartureDate() + data.SEPARATOR);
        out.print(flight.getSeats() + data.SEPARATOR);
        out.print(flight.getPrice() + data.SEPARATOR);
        if(flight.isDeleted()) {
        	out.print(1 + data.SEPARATOR);
        }else {
        	out.print(0 + data.SEPARATOR);
        }
        out.println();
         out.close();
		data.loadData(fbs); // loads data from text file
		 Flight f = fbs.getFlightByID(5);
		 assertEquals(40, f.getSeats());
	            
	    
	}

	@Test
	public void loadPriceTest() throws FlightBookingSystemException, IOException   {
		String RESOURCE = "./resources/data/flights.txt";
		FlightBookingSystem fbs = new FlightBookingSystem();
		   FlightDataManager data = new FlightDataManager();
		PrintWriter out = new PrintWriter(new FileWriter(RESOURCE)); //writing the custoemr info in the text fike
        out.print(flight.getId() + data.SEPARATOR);
        out.print(flight.getFlightNumber() + data.SEPARATOR);
        out.print(flight.getOrigin() + data.SEPARATOR);
        out.print(flight.getDestination() + data.SEPARATOR);
        out.print(flight.getDepartureDate() + data.SEPARATOR);
        out.print(flight.getSeats() + data.SEPARATOR);
        out.print(flight.getPrice() + data.SEPARATOR);
        if(flight.isDeleted()) {
        	out.print(1 + data.SEPARATOR);
        }else {
        	out.print(0 + data.SEPARATOR);
        }
        out.println();
         out.close();
		data.loadData(fbs); // loads data from text file

		 Flight f = fbs.getFlightByID(5);
		 assertEquals(23.9f, f.getPrice(),0);
	            
	    
	}
}

