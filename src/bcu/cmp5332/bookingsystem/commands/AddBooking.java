
package bcu.cmp5332.bookingsystem.commands;

import java.io.IOException;
import java.time.LocalDate;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class AddBooking implements Command { //command class i created

	private final int cusid;
	private final int flightid;

	public AddBooking(int cusid, int flightid) {
		this.cusid = cusid;
		this.flightid = flightid;
	}

	@Override
	public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException, IOException {
		if(FlightBookingSystemData.isBookingAccessible()) { //checks if the file is accessible.
			Customer customer = flightBookingSystem.getCustomerByID(cusid);
			Flight flight = flightBookingSystem.getFlightByID(flightid);
			LocalDate flightDate = flight.getDepartureDate();
			LocalDate systemDate = flightBookingSystem.getSystemDate(); 
			if(flightDate.compareTo(systemDate) == 1 || flightDate.isAfter(systemDate) ) { //checks if flight date is after or the same as the system date.
				LocalDate bookDate = systemDate;//gets system date and makes it the date when the booking was created.
				Booking b_temp = null;
				if(!flight.isDeleted()) { //if flight has not been removed from the system.
					if(!customer.isDeleted()) { //if customer has not been removed from the system.
						if (flight.getPassengers().size() < flight.getSeats()) { //checks if the flight is not full.
							int maxId = 0;
							if (flightBookingSystem.getCustomers().size() > 0) {  //will try to find the last id for bookings.
								for(Customer c : flightBookingSystem.getCustomers()) {
									for(Booking b : c.getBookings()) {
										if (b.getId() > maxId) {
											maxId = b.getId();
										}
										if(b.getFlight() == flight) {
											b_temp = b;
										}
									}
								}
							}
							if(b_temp != null && !b_temp.getBookingdate().equals(flightBookingSystem.getSystemDate())) { //TODO
								float updatedPrice = flight.getUpdatedPrice(flightBookingSystem);
								flight.setPrice(updatedPrice);
							}
							Booking booking = new Booking(++maxId,customer,flight,bookDate,flight.getPrice()); //Creates and adds the booking to the system.
							customer.addBooking(booking); //adds booking to the customer object
							flight.addPassenger(customer);//adds customer to the passengers list for the flight object.
							System.out.println("Booking added.");
							FlightBookingSystemData.store(flightBookingSystem); //saves the booking that was created into the file after it was created.
						} else throw new FlightBookingSystemException("Maximum number of Passengers has been reached for flight "+flight.getFlightNumber());
					}else throw new FlightBookingSystemException("[ERROR]Customer has been removed from the system. Booking cannot be made");
				}else throw new FlightBookingSystemException("[ERROR]Flight has been cancelled. Booking cannot be made");
			}else throw new FlightBookingSystemException("[ERROR]Flight has expired. Booking cannot be made");
		}else throw new FlightBookingSystemException("[ERROR]Command cannot be executed. File is not accessible");

	}
}
