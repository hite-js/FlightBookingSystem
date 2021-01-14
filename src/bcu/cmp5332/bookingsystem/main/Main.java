package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.*;

public class Main {

	public static void main(String[] args) throws IOException, FlightBookingSystemException {
		FlightBookingSystem fbs = FlightBookingSystemData.load();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Flight Booking System");
		System.out.println("Enter 'help' to see a list of available commands.");
		while (true) {
			System.out.print("> ");
			String line = br.readLine();
			if (line.equals("exit")) {
				break;
			}
			try {
				Command command = CommandParser.parse(line); //it checks the command the user typed in and if they typed in a valid command, 
				//it will return a command class interface eg addflight help list flight etc, and what ever command that gets returned it will run its execute method.help
				command.execute(fbs);
			} catch (FlightBookingSystemException ex) {
				System.out.println(ex.getMessage());
			}
		}
		try {
			FlightBookingSystemData.store(fbs);
		}catch(IOException e) { //catches the error of not being able to write on the files, and outputting an error.
			System.out.println("[ERROR]File could not be accessed. System could not be saved");
		}
		System.exit(0);
	}
}
