package bcu.cmp5332.bookingsystem.data;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class FlightBookingSystemData {
    
    private static final List<DataManager> dataManagers = new ArrayList<>();
    private final static String flightsFile = "./resources/data/flights.txt"; //storing the files in an attribute.
    private final static String customersFile = "./resources/data/customers.txt";
    private final static String bookingsFile = "./resources/data/bookings.txt";
    private final static File fFile = new File(flightsFile);
    private final static File cFile = new File(customersFile);
    private final static File bFile = new File(bookingsFile);
    
    // runs only once when the object gets loaded to memory
    static {
        dataManagers.add(new FlightDataManager());
        dataManagers.add(new CustomerDataManager());
        dataManagers.add(new BookingDataManager());
    }
    
    public static FlightBookingSystem load() throws FlightBookingSystemException, IOException {

        FlightBookingSystem fbs = new FlightBookingSystem();
        for (DataManager dm : dataManagers) {
            dm.loadData(fbs);
        }return fbs;
        
    }

    public static void store(FlightBookingSystem fbs) throws IOException {
        for (DataManager dm : dataManagers) {
            dm.storeData(fbs);
        }
    }
                                               //each method checks if each file can be wrote on or not, returning a boolean.
    public static boolean isFlightAccessible() { 
    	if(fFile.canWrite()) { 
    		return true;
    	}return false;
    }
    public static boolean isBookingAccessible() {
    	if(bFile.canWrite()) {
    		return true;
    	}return false;
    }
    public static boolean isCustomerAccessible() {
    	if(cFile.canWrite()) {
    		return true;
    	}return false;
    }
    
}
