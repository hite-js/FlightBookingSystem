package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

public class FlightDataManager implements DataManager {
    
    private final String RESOURCE = "./resources/data/flights.txt";
    
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
    	                    int id = Integer.parseInt(properties[0]); // records flight info from file.
    	                    String flightNumber = properties[1];
    	                    String origin = properties[2];
    	                    String destination = properties[3];
    	                    LocalDate departureDate = LocalDate.parse(properties[4]);
    	                    int seats = Integer.parseInt(properties[5]);
    	                    float price = Float.parseFloat(properties[6]);
    	                    int isDeletedNum = Integer.parseInt(properties[7]);
    	                    Flight flight;
    	                    if(isDeletedNum != 0) { //checks if the flight is deleted. if so it will use another constructor which will specify the flight has already been deleted.
    	                    	flight = new Flight(id, flightNumber, origin, destination, departureDate,seats,price,true);
    	                    	
    	                    }else {
    	                    	flight = new Flight(id, flightNumber, origin, destination, departureDate,seats,price);
    	                    }
    	                    
    	                    fbs.addFlight(flight); //adds flight to the system
    	                } catch (NumberFormatException ex) {
    	                    throw new FlightBookingSystemException("Unable to parse flight id " + properties[0] + " on line " + line_idx
    	                        + "\nError: " + ex);
    	                }
    	                line_idx++;
    	            }
                }return true;
            }else {
            	System.out.println("[ERROR]The file is empty. No flights recorded");
            	return false;
            }
        }
    }
    
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
    	if(FlightBookingSystemData.isFlightAccessible()) { // checks if the file can be opened, and written on.
    		try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
                for (Flight flight : fbs.getFlights()) { //stores data in file.
                    out.print(flight.getId() + SEPARATOR);
                    out.print(flight.getFlightNumber() + SEPARATOR);
                    out.print(flight.getOrigin() + SEPARATOR);
                    out.print(flight.getDestination() + SEPARATOR);
                    out.print(flight.getDepartureDate() + SEPARATOR);
                    out.print(flight.getSeats() + SEPARATOR);
                    out.print(flight.getPrice() + SEPARATOR);
                    if(flight.isDeleted()) {
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
