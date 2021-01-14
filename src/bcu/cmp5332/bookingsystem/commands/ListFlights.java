package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.time.LocalDate;
import java.util.List;

public class ListFlights implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        List<Flight> flights = flightBookingSystem.getFlights(); //gets the flight list aka all the flights in the system and loops through each flight and outputs its details.
        for (Flight flight : flights) {
        	if(!flight.isDeleted()) { //after getting the flight it checks if it has been deleted, if so it will not be output.
        		 LocalDate flightDate = flight.getDepartureDate();
            	 LocalDate systemDate = flightBookingSystem.getSystemDate(); 
            	if(flightDate.compareTo(systemDate) == 1 || flightDate.isAfter(systemDate) ) { //checks if flight is in the future or the same day as today/system date.
            		   System.out.println(flight.getDetailsShort());		
            	}
        	}
        }
        System.out.println(flights.size() + " flight(s)");
    }
}
