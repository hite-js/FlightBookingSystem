package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

public class Booking {
    
    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private int id;
    private float fee;
    private float bookingPrice;

    public Booking(int id,Customer customer, Flight flight, LocalDate bookingDate,float bookingPrice) {
    	this.id = id;
    	this.customer = customer;
    	this.flight = flight;
    	this.bookingDate = bookingDate;
        this.fee = getFlight().getPrice() * 0.07f; //calculates the re-book fee by multiplying the price to book the flight initially, by 7%.
        this.bookingPrice = bookingPrice; //stores the price of when the booking was made aka the price of the flight at that specific date/time.
    }
    
    public Customer getCustomer() {  //getters and setters for customer attributes.
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    
    public LocalDate getBookingdate() {
        return bookingDate;
    }

    public void setBookingdate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isCompleted(FlightBookingSystem fbs) { //checks if the booking has been completed.
		 LocalDate flightDate = getFlight().getDepartureDate();
    	 LocalDate systemDate = fbs.getSystemDate(); 
    	if(flightDate.compareTo(systemDate) == 1 || flightDate.isAfter(systemDate) ) { //specifically getting the flight for the booking and checking if its date is after or the same day 
     		   return false;	                                                       //as the system date.
    	}
    	return true;
	}
	
	public float getFee() {
		return Float.parseFloat(String.format("%.2f", fee)); //returning booking fee in 2dp.
	}

	public float getBookingPrice() {
		return bookingPrice;
	}

	public void setBookingPrice(float bookingPrice) {
		this.bookingPrice = bookingPrice;
	}
}
