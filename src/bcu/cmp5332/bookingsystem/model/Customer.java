package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 *Represents an individual Customer for the Flight Booking System. 
 * 
 * @author Aminul Choudhury &amp; Hitesh joshi
 * 
 * @see Flight 
 *
 */
public class Customer {
    
    private int id;
    private String name;
    private String phone;
    private String email;
    private boolean isDeleted;
    private final List<Booking> bookings = new ArrayList<>();
    
    // TODO: implement constructor here
    public Customer(int id, String name, String phone,String email) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.isDeleted = false;
    }
    /**
   	 * Second Constructor, identical to the first, but has the extra boolean property isDeleted - having 2 constructors so when loading the data from the file, 
   	 * it saves/keeps track of its state regarding if it has been deleted or not and to do this, the second constructor will be used to initialise the already existing object instead of the first. As the first constructor will be used to create a new fresh customer.
   	 * @param isDeleted   boolean representing if the customer has been deleted/removed from the FBS.
   	 * @param id  integer representing the customers unique ID.
   	 * @param name   String representing the name of the customer.
   	 * @param phone   String representing the customers phone number.
   	 * @param email   String representing customers email.
   	 * 
   	 */
    public Customer(int id, String name, String phone,String email,boolean isDeleted) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.isDeleted = isDeleted;
    }
    
    public int getId() { //getters and setters for customer attributes.
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public List<Booking> getBookings() {
    	return bookings;
    }
    
    public String getDetailsShort() { // returns the basic customer details without booking info.
    	String details = "Customer #"+id+" - "
    	+ "Name: "+name+" - "	
    	+ "Phone: "+phone + " - " + "Email: " + email;
    	return details;
    }
    
    public String getDetailsLong(FlightBookingSystem fbs) { // returns the customer details with booking info.
    	String bookings_string = "";
        for(Booking b : bookings) { //loops through the list of bookings the customer has made to output each booking information along with the customer information.
        	Flight f = b.getFlight();
        	if (!b.isCompleted(fbs)) {// checks if the booking has been completed/expired. if so it will output this information instead of presenting the whole information.
            	bookings_string = bookings_string + "* Booking ID: "+ b.getId() + " - " + "Booking date: " + b.getBookingdate()+ " for Flight #" + f.getId() + " - " + f.getFlightNumber()
            			+ " - " + f.getOrigin() + " to " + f.getDestination() + " on " + f.getDepartureDate() +  " - " +  "Booking Price: £" + b.getBookingPrice()+  " - " 
            			+" Cancel/Rebook Fee: £"+ b.getFee()
                    			  + "\n";
        	}else {
        		bookings_string =  bookings_string + "* The booking for flight "+f.getFlightNumber()+ " has expired. \n";
        	}
        }
        String customer_string = "Customer #" + id + "\nName:" + name + "\nPhone: " + phone + "\nEmail: "+ email + "\n---------------------------\n Bookings:\n";
        String isDeletedCheck = "";
        if (isDeleted == true) { //checks if customer has been removed from the system 
        	isDeletedCheck = "\n\n[Alert]This customer has been removed from the system";
        }
        return  customer_string + bookings_string
        +"\n"+bookings.size()+" booking(s)" + isDeletedCheck;
    }
    
    
    public void addBooking(Booking booking) throws FlightBookingSystemException  {
    	 for (Booking existing : bookings) {  // for loop as long as the amount of bookings that have been booked.
             if (existing.getFlight().equals(booking.getFlight())) {
                 throw new FlightBookingSystemException("Flight Already Exists"); //checks if the same flight has been booked already
             }
    	 }
    	bookings.add(booking);
    }
    public boolean cancelBookingForFlight(Flight flight) throws FlightBookingSystemException {
    	Booking b_temp = null;
    	List<Booking> c_bookings = this.getBookings();
    	for(Booking b : c_bookings) { //check if the booking for the flight exists in the customer booking list
    		if(b.getFlight().getId() == flight.getId()) {
    			b_temp = b;
    		}
    	}
    	return bookings.remove(b_temp); // if it does exist then it it removed from the booking list.
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	   /**
	  	 * Returns whether the Customer has been removed from the flight booking system or not
	  	 * 
	  	 * @return IsDeleted
	  	 */
	public boolean isDeleted() {
		return isDeleted;
	}
	  /**
   	 * Sets/updates the state of the customer, specifically removing or add the flight back into the system.
   	 * @param isDeleted -  new boolean that is to become the current state of the flight.
   	 * 
   	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public void deleteBookings() {
		bookings.clear();
	}
}
