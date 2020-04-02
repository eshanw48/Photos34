package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



public class Persistance {
	
	/**
	 * Holds all the user objects in the application
	 */
	private static List<User> users = new ArrayList<User>();  // Array which holds all the User objects that were created
	
	
	/**
	 * @param user takes in user object
	 */
	public static void addUser(User user) {
		
		users.add(user);
	}
	
	/**
	 * @param index location of the user to be deleted
	 */
	public static void delUser(int index) {
		
		users.remove(index);
	}
	
	/**
	 * @param index location of where to get the User
	 * @return returns the User at that location
	 */
	public static User getUser(int index) {
		
		return users.get(index);
	}
	
	/**
	 * @return returns iterator to iterate through each user
	 */
	public static Iterator<User> userIterator() {
		
		return users.iterator();
	}
	
	/**
	 * @throws IOException throws if file does not exist
	 */
	public static void writeUser() throws IOException {
		
		String fileName = "users.dat";
				
		ObjectOutputStream os = null;

		try {
			
			 os = new ObjectOutputStream(new FileOutputStream(fileName));
			 
			 os.writeObject(users);
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}

		
		os.close();
		
	}
	
	/**
	 * @throws IOException throws if file does not exist
	 * @throws ParseException throws if date cannot be parsed
	 */
	public static void readUser() throws IOException, ParseException {
		
		String fileName = "users.dat";
		
		File file = new File("users.dat");
		
		String[] names = {"TEST"};
		
		String[] fileLoc = {"src/stock/TEST.jpg"};
}
}