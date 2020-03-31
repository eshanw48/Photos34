package app;

import java.util.ArrayList;
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
	
	
}