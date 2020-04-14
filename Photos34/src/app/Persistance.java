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


import java.net.URL;


/**
 * Class to help with persistence between sessions and between users/admin.
 * @author Eshan Wadhwa and Vishal Patel
 *
 */
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
	 * Writes users to "data/users.dat" through serialization.
	 * @throws IOException Throws Exception if Serialization error.
	 */
	public static void writeUser() throws IOException {
		
		String fileName = "data/users.dat";
		
		//new code
		File data = new File(fileName);
		
		//creates the file if it does not exist
		data.createNewFile();
		
		
		ObjectOutputStream os = null;

		try {
			 //we set false below to OVERWRITE the data file when we write it
			 os = new ObjectOutputStream(new FileOutputStream(data,false));
			 
			 os.writeObject(users);
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}

		
		os.close();
		
	}
	
	
	
	
	
	
	
	/**
	 * Reads users from "data/users.dat". If no such file exists, then it is created, and the stock user is initialized.
	 * @throws IOException Throws exception if serialization error.
	 * @throws ParseException Throws exception if users.dat file cannot be parsed into an array of users for some reason.
	 */
	public static void readUser() throws IOException, ParseException {
		
		String fileName = "data/users.dat";
		
		File file = new File(fileName);
		
		if (!file.exists()) {
			//if the file does not exist, we should create it and add the stock user,album, and photo
			User stock = new User("stock");
			Album stockAlbum = new Album("stock");
			stock.addAlbum(stockAlbum);
			//hardcoding stock photos
			Photo stockPhoto= new Photo("stock photo", new File("data/stockPhotos/test.png"));
			stockAlbum.addPhoto(stockPhoto);
			stockAlbum.addPhoto(new Photo("stock photo 2", new File("data/stockPhotos/stockPhoto.jpg")));
			stockAlbum.addPhoto(new Photo("stock photo 3", new File("data/stockPhotos/stockPhoto2.png")));
			stockAlbum.addPhoto(new Photo("stock photo 4", new File("data/stockPhotos/stockPhoto3.jpg")));
			stockAlbum.addPhoto(new Photo("stock photo 5", new File("data/stockPhotos/stockPhoto4.jpg")));
			stockAlbum.addPhoto(new Photo("stock photo 6", new File("data/stockPhotos/stockPhoto5.jpg")));
			//resetting users
			users=new ArrayList<User>();
			users.add(stock);
		} else {
			//then we should just read the array
			try {
				
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
				 
		        users = (List<User>) in.readObject(); 	
		        
		        in.close();
		        
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
		
		
}