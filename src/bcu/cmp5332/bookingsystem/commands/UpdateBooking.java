package bcu.cmp5332.bookingsystem.commands;

import java.io.IOException;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class UpdateBooking implements Command {

	private final int bookingID;
	private final int flightID;
	
	public UpdateBooking(int bookingID,int flightID) {
		this.bookingID = bookingID;
		this.flightID = flightID;
		
	}
	
	
	@Override
	public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException, IOException {
		if(FlightBookingSystemData.isBookingAccessible()) { //checks if file is accessible.
			Booking b = fbs.getBookingByID(bookingID);  //gets the booking, and the flight, and changes the flight for the booking and stores this change into the file.
			String old_flight = b.getFlight().getFlightNumber();
			Flight f = fbs.getFlightByID(flightID);
			b.setFlight(f);
			System.out.println(old_flight + " has been set to " + f.getFlightNumber()+" for booking #" + b.getId());
			FlightBookingSystemData.store(fbs);
		}else {
			System.out.println("[ERROR]Command cannot be executed. File is not accessible");
		}
	}

}
