package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.temporal.ChronoUnit;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

/**
 * 
 *Represents an individual flight for the Flight Booking System. 
 * 
 * @author Aminul Choudhury &amp; Hitesh joshi
 * 
 * @see Customer 
 *
 */
public class Flight {

	private int id;
	private String flightNumber;
	private String origin;
	private String destination;
	private LocalDate departureDate;
	private int seats;
	private float price;
	private float fee;
	private boolean isDeleted;

	private final Set<Customer> passengers;

	//WHEN NEW FLIGHT IS CREATED THIS CONTRUCTOR WILL BE USED
	public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate,int seats, float price) {
		this.id = id;
		this.flightNumber = flightNumber;
		this.origin = origin;
		this.destination = destination;
		this.departureDate = departureDate;
		this.seats = seats;
		this.price = price;
		this.isDeleted = false;

		passengers = new HashSet<>();
	}

	//constructor if isDeleted is specified. Used in data manager
	/**
	 * Second Constructor, identical to the first, but has the extra boolean property isDeleted - having 2 constructors so when loading the data from the file, 
	 * it saves/keeps track of its state regarding if it has been deleted or not and to do this, the second constructor will be used to initialise the already existing object instead of the first. 
	 * As the first constructor will be used to create a new flight.
	 * @param isDeleted   boolean representing if the flight has been deleted/removed from the FBS.
	 * @param id  integer representing the flights unique ID.
	 * @param flightNumber   String representing the flight number of the flight.
	 * @param origin   String representing where the flight will take off from.
	 * @param destination   String representing the destination of the flight.
	 * @param departureDate   LocalDate representing when the flight will take place.
	 * @param seats   integer representing amount of seats the flight has.
	 * @param price   float representing the cost to book this flight.
	 */
	public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate,int seats, float price,boolean isDeleted) {
		this.id = id;
		this.flightNumber = flightNumber;
		this.origin = origin;
		this.destination = destination;
		this.departureDate = departureDate;
		this.seats = seats;
		this.price = price;
		this.isDeleted = isDeleted;

		passengers = new HashSet<>();
	}

	public int getId() { //getters and setters for the flight attributes.
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	public List<Customer> getPassengers() {
		return new ArrayList<>(passengers);
	}

	public String getDetailsShort() { // returns the basic flight details without the passenger info.
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		return "Flight #" + id + " - " + flightNumber + " - " + origin + " to " 
		+ destination + " on " + departureDate.format(dtf) + " Seats: " + seats + " Price: £" + price;
	}

	public String getDetailsLong(FlightBookingSystem fbs) { // returns the customer details with booking info.
		String updatedPrice = Float.toString(getUpdatedPrice(fbs));
		String passengers_string = "";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		for(Customer c : passengers) { //loops through the list of passengers to output each customer information along with the flight information.
			passengers_string = passengers_string + "* Id: " + c.getId()+ " - " + c.getName() + " - " + c.getPhone() +  " - " + c.getEmail()+"\n";
		}
		return "Flight #" + id + "\nFlight No:" + flightNumber + "\nOrigin: " + origin + "\nDestination: " 
		+ destination + "\nDeparture Date: " + departureDate.format(dtf) + "\nSeats: " + seats + "\nSeats Left: " + (seats - passengers.size())+
		"\nPrice: £" + updatedPrice + "\n---------------------------\n Passengers:\n" + passengers_string
		+"\n"+passengers.size()+" passenger(s)";
	}

	public void addPassenger(Customer passenger) throws FlightBookingSystemException {
		for (Customer existing : passengers) {  // for loop as long as the amount of bookings that have been booked.
			if (existing.getId() == passenger.getId()){
				throw new FlightBookingSystemException("Passenger Already Exists"); //checks if the same flight has been booked already
			}
		}
		passengers.add(passenger);
	}

	public void removePassenger(Customer passenger) throws FlightBookingSystemException {
		List<Customer> c = this.getPassengers();
		for (Customer existing : c) {  // for loop as long as the amount of bookings that have been booked.
			if (existing.getId() == passenger.getId()){
				passengers.remove(passenger); //removes customer from the list of passengers
				break;
			}
		}
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	/**
	 * Returns Whether the flight has been deleted from the system or not
	 * 
	 * @return IsDeleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}
	/**
	 * Sets/updates the state of the flight, specifically removing or add the flight back into the system.
	 * @param isDeleted -  new boolean that is to become the current state of the flight.
	 * 
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public float getUpdatedPrice(FlightBookingSystem fbs) { //checking the days in between the system date and flight date  and the amount of seats available for the flight it returns the updated price.
		/*
		 * DAYS (db = days in between the system date and the flight date)
			>= 60db  Original price
			<= 45db +2% added Each day
			<= 15db +5% added Each Day
			<=  7db +7% added Each Day
		 	<=  4db +9% added Each Day

			SEATS
			>= 60% FULL +5% For Each Seat
			>= 80% FULL +8% For Each Seat
			>= 90% FULL +10% For Each Seat
		 * */

		LocalDate flightDate = getDepartureDate();
		LocalDate systemDate = fbs.getSystemDate();
		long daysBetween = ChronoUnit.DAYS.between(systemDate, flightDate);
		float updatedPrice = getPrice();
		if(systemDate.isBefore(flightDate)) {
			if(daysBetween <= 4) {
				updatedPrice = updatedPrice + (updatedPrice * 0.09f);
			}else if(daysBetween <= 7) {
				updatedPrice = updatedPrice + (updatedPrice * 0.07f);
			}else if(daysBetween <= 15) {
				updatedPrice = updatedPrice + (updatedPrice * 0.05f);
			}else if(daysBetween <= 45) {
				updatedPrice = updatedPrice + (updatedPrice * 0.02f);
			}

			int seats = getSeats();
			int passengersNum = getPassengers().size();
			int seatsLeft = seats - passengersNum;
			float percentageFull = 100 - Float.parseFloat(String.format("%.2f", (float) seatsLeft / seats * 100));

			if(percentageFull>=90) {
				updatedPrice = updatedPrice + (updatedPrice * 0.10f);
			}else if(percentageFull >= 80){
				updatedPrice = updatedPrice + (updatedPrice * 0.08f);
			}else if(percentageFull >= 60){
				updatedPrice = updatedPrice + (updatedPrice * 0.05f);
			}
		}return Float.parseFloat(String.format("%.2f", updatedPrice));
	}
}
