package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.util.*;

public class FlightBookingSystem {
    
    private final LocalDate systemDate = LocalDate.parse("2021-01-03");
    
    private final Map<Integer, Customer> customers = new TreeMap<>(); // the integer will be the id
    private final Map<Integer, Flight> flights = new TreeMap<>(); // map holding an integer that will be the key and representing the flight id and the actual flights 
    //them selves that will be the value of the map and will represent the Flight class.

    public LocalDate getSystemDate() { //getters and setters, for Flight booking system attributes.
        return systemDate;
    }

    public List<Flight> getFlights() {
        List<Flight> out = new ArrayList<>(flights.values());
        return Collections.unmodifiableList(out);
    }
    
    public List<Customer> getCustomers() { // had to do this
        List<Customer> out = new ArrayList<>(customers.values());
        return Collections.unmodifiableList(out);
    }


    public Flight getFlightByID(int id) throws FlightBookingSystemException { //returns the flight based on the id given
        if (!flights.containsKey(id)) {
            throw new FlightBookingSystemException("There is no flight with that ID.");
        }
        return flights.get(id);
    }

    public Customer getCustomerByID(int id) throws FlightBookingSystemException { //returns the customer based on the id given
        // TODO: implementation here
    	 if (!customers.containsKey(id)) {
             throw new FlightBookingSystemException("There is no customer with that ID.");
         }
         return customers.get(id);
    }
    
    public Booking getBookingByID(int id) throws FlightBookingSystemException{ //returns the booking based on the id given
    	for(Customer c : this.getCustomers()) { //goes through list of customers in the system and checks if any of the bookings id matches the id that has been passed over.
    		for(Booking b : c.getBookings()) {
    			if(b.getId() == id) {
    				return b;
    			}
    		}
    	}
    	throw new FlightBookingSystemException("Booking #"+id+" has not been found.");
    }

    public void addFlight(Flight flight) throws FlightBookingSystemException {
        if (flights.containsKey(flight.getId())) {
            throw new IllegalArgumentException("Duplicate flight ID."); //checks if the flight is already in the system.
        }
        for (Flight existing : flights.values()) {  // for loop as long as the amount of flights that have been booked.
            if (existing.getFlightNumber().equals(flight.getFlightNumber()) 
                && existing.getDepartureDate().isEqual(flight.getDepartureDate())) { //checks if there is a flight with the same flight number or departure date
                throw new FlightBookingSystemException("There is a flight with same"
                        + "number and departure date in the system");
            }
        }
        flights.put(flight.getId(), flight);
    }

    public void addCustomer(Customer customer) throws FlightBookingSystemException  {
        // TODO: implementation here
    	
        if (customers.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Duplicate customer ID."); //checks if the customer is already in the system.
        }
        for (Customer existing : customers.values()) {  // for loop as long as the amount of flights that have been booked.
            if (existing.getPhone().equals(customer.getPhone())) { //checks if the customers phone number has already been registered in the system.
                throw new FlightBookingSystemException("There is a customer with same "
                        + " phone number in the system");
            }
        }
        customers.put(customer.getId(), customer);
    }
}
