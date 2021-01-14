package bcu.cmp5332.bookingsystem.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class BookingDataManager implements DataManager {
    
    public final String RESOURCE = "./resources/data/bookings.txt";

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
                        	int bId = Integer.parseInt(properties[0]); // records booking info from file.
                            int cId = Integer.parseInt(properties[1]);
                            int fId = Integer.parseInt(properties[2]);
                            LocalDate bookingDate = LocalDate.parse(properties[3]);
                            float bookingPrice = Float.parseFloat(properties[5]);
                            Customer c = fbs.getCustomerByID(cId);
                            if(!fbs.getFlights().isEmpty()) { //TODO WHAT THIS MEAN
                            	Flight f = fbs.getFlightByID(fId);
                                Booking b = new Booking(bId,c, f, bookingDate,bookingPrice);
                                c.addBooking(b); //Adds booking to system.
                                f.addPassenger(c);
                            }else {
                            	PrintWriter writer = new PrintWriter(new FileWriter(RESOURCE));
                            	writer.print("");
                            	writer.close();
                            	System.out.println("[ERROR]No flights to book. The current booking file has been wiped");
                            }
                        } catch (NumberFormatException ex) {
                            throw new FlightBookingSystemException("Unable to parse booking id " + properties[0] + " on line " + line_idx
                                + "\nError: " + ex);
                        }
                        line_idx++;
                    }
                }return true;
            }
            else {
            	System.out.println("[ERROR]No bookings could be retrieved because file is empty");
            	return false;
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
    	if(FlightBookingSystemData.isBookingAccessible()) { // checks if the file can be opened, and written on.
	    	try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
	            for (Customer c : fbs.getCustomers()) { //stores data in file.
	            	for(Booking b : c.getBookings()) {
	            		out.print(b.getId() + SEPARATOR);
	            		out.print(b.getCustomer().getId() + SEPARATOR);
	                    out.print(b.getFlight().getId() + SEPARATOR);
	                    out.print(b.getBookingdate() + SEPARATOR);
	                    out.print(b.getFee() + SEPARATOR);
	                    out.print(b.getBookingPrice() + SEPARATOR);
	                    out.println();
	            	}
	            }
	        }
    	}
    }  
}
