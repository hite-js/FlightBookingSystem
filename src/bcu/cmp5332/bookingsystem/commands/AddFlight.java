package bcu.cmp5332.bookingsystem.commands;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.data.FlightDataManager;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class AddFlight implements  Command {

    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int seats;
    private final float price;

    public AddFlight(String flightNumber, String origin, String destination, LocalDate departureDate, int seats, float price) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.seats = seats;
        this.price = price;
    }
    
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException, IOException {
    	if(FlightBookingSystemData.isFlightAccessible()) { //checks if the file is accessible.
    		int maxId = 0;
	        if (flightBookingSystem.getFlights().size() > 0) {  //will try to find the last id 
	            int lastIndex = flightBookingSystem.getFlights().size() - 1;
	            maxId = flightBookingSystem.getFlights().get(lastIndex).getId();
	        }
	        // overall the execute method for the class is just an extra step before it actually adds the flight to the system where it 
	        // gets the user input and creates the new flight instance whilst finding and incrementing the most recent id and 
	        //sends the flight object to the system 
	        Flight flight = new Flight(++maxId, flightNumber, origin, destination, departureDate,seats,price);
	        flightBookingSystem.addFlight(flight); //adds flight to the system.
	        System.out.println("Flight #" + flight.getId() + " added.");
	        FlightDataManager fdm = new FlightDataManager();
	        fdm.storeData(flightBookingSystem); //stores data after command has been executed
    	}else {
    		System.out.println("[ERROR]Command cannot be executed. File is not accessible");
    	}
    }
}
