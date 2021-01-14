package bcu.cmp5332.bookingsystem.commands;

import java.time.LocalDate;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ShowFlight implements Command {

	private final int id;
	
	public ShowFlight(int id) {
		this.id = id;
	}
	
	
	@Override
	public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
		 Flight f = flightBookingSystem.getFlightByID(id);
		 LocalDate flightDate = f.getDepartureDate();
    	 LocalDate systemDate = flightBookingSystem.getSystemDate();
    	 if(!f.isDeleted()) { //checks if the flight has been removed from the system.
    		 if(flightDate.compareTo(systemDate) == 1 || flightDate.isAfter(systemDate) ) { //checks if flight date is after or the same as the system date.
    	    		System.out.println(f.getDetailsLong(flightBookingSystem));
    	    	}else {
    	    		System.out.println(f.getDetailsLong(flightBookingSystem) + "\n\n[ALERT]This Flight Has Already Been Departed");
    	    	}
    	 }else System.out.println(f.getDetailsLong(flightBookingSystem) + "\n\n[ALERT]This Flight Has Been Cancelled");
	}
}
