package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.LoadGUI;
import bcu.cmp5332.bookingsystem.commands.ShowCustomer;
import bcu.cmp5332.bookingsystem.commands.ShowFlight;
import bcu.cmp5332.bookingsystem.commands.UpdateBooking;
import bcu.cmp5332.bookingsystem.commands.ListFlights;
import bcu.cmp5332.bookingsystem.commands.ListCustomers;
import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.AddCustomer; // have to import the addcustomer class manually
import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.Help;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CommandParser {

	public static Command parse(String line) throws IOException, FlightBookingSystemException {
		try {
			String[] parts = line.split(" ", 3); // line being the command user inputs.
			String cmd = parts[0]; // checks the first word typed in

			if (cmd.equals("addflight")) { //if user inputs a specific word aka command, in this case addflight
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Flight Number: ");
				String flighNumber = br.readLine();
				System.out.print("Origin: ");
				String origin = br.readLine();
				if (!origin.matches("^[a-zA-Z-\s]+")) { //checks if string input does contain letters, dashes or spaces aka numbers or other punctuation  input and outputs an exception.
					throw new FlightBookingSystemException("[ERROR]Origin can only contain letters.");
				}
				System.out.print("Destination: ");
				String destination = br.readLine();
				if (!destination.matches("^[a-zA-Z-\s]+")) { //checks if string input does contain letters, dashes or spaces aka numbers or other punctuation  input and outputs an exception.
					throw new FlightBookingSystemException("[ERROR]Destination can only contain letters.");
				}
				try {
					System.out.print("Seats: ");
					int seats = Integer.parseInt(br.readLine());
					System.out.print("Price: ");
					float price = Float.parseFloat(br.readLine());
					LocalDate departureDate = parseDateWithAttempts(br);
					return new AddFlight(flighNumber, origin, destination, departureDate,seats,price); // returns the AddFlight object that implements the command interface.	
				}catch(NumberFormatException e) { //if numbers have not be input for price or seats i will output and error.
					throw new FlightBookingSystemException("[ERROR]Invalid characters detected for numerical properties(price/seats).");
				}
			} else if (cmd.equals("addcustomer")) { 
				
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Name: ");
				String cusname = br.readLine();
				if (cusname.matches("^[a-zA-Z-\s]+")) { //checks if string input does contain letters, dashes or spaces aka numbers or other punctuation  input and outputs an exception.
					System.out.print("Phone number: ");
					String phnumber = br.readLine();
					try {
			            Double num = Double.parseDouble(phnumber); //tries to convert phone number string into a double. and outputting an error if this is not possible.
			        } catch (NumberFormatException e) {
			        	throw new FlightBookingSystemException("[ERROR]Phonenumber can only contain numbers");
			        }
					System.out.print("Email: ");
					String email = br.readLine();
				    String validEmailformat = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
				    if(email.matches(validEmailformat)) { // checks if the email entered is a valid email format.
				    	return new AddCustomer(cusname, phnumber,email);
				    }else {
						throw new FlightBookingSystemException("[ERROR]Invalid email entered");
					}
					
				}else {
					throw new FlightBookingSystemException("[ERROR]Name can only contains letters. No numbers or puntuation");
				}

			} else if (cmd.equals("loadgui")) {
				return new LoadGUI();
				
			} else if (parts.length == 1) {
				
				if (line.equals("listflights")) {
					return new ListFlights();
					
				} else if (line.equals("listcustomers")) {
					return new ListCustomers();
				} else if (line.equals("help")) {
					return new Help();
				}
			} else if (parts.length == 2) {
				int id = Integer.parseInt(parts[1]);

				if (cmd.equals("showflight")) {
					return new ShowFlight(id);
				} else if (cmd.equals("showcustomer")) {
					return new ShowCustomer(id);
				}
			} else if (parts.length == 3) {
				int customerID = Integer.parseInt(parts[1]);
				int flightID = Integer.parseInt(parts[2]);

				if (cmd.equals("addbooking")) {
					return new AddBooking(customerID, flightID);
					
				} else if (cmd.equals("editbooking")) {
					int bookingID = customerID;
					return new UpdateBooking(bookingID,flightID);
					
				} else if (cmd.equals("cancelbooking")) {
					return new CancelBooking(customerID,flightID);
				}
			}
		} catch (NumberFormatException ex) {

		}

		throw new FlightBookingSystemException("Invalid command.");
	}

	private static LocalDate parseDateWithAttempts(BufferedReader br, int attempts) throws IOException, FlightBookingSystemException {
		if (attempts < 1) {
			throw new IllegalArgumentException("Number of attempts should be higher that 0");
		}
		while (attempts > 0) {
			attempts--;
			System.out.print("Departure Date (\"YYYY-MM-DD\" format): ");
			try {
				LocalDate departureDate = LocalDate.parse(br.readLine());
				return departureDate;
			} catch (DateTimeParseException dtpe) {
				System.out.println("Date must be in YYYY-MM-DD format. " + attempts + " attempts remaining...");
			}
		}

		throw new FlightBookingSystemException("Incorrect departure date provided. Cannot create flight.");
	}

	private static LocalDate parseDateWithAttempts(BufferedReader br) throws IOException, FlightBookingSystemException {
		return parseDateWithAttempts(br, 3);
	}
}
