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
	
	//deprecated
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
	 * Writes user data to data file. If data file doesn't exist, then it is created
	 * @throws IOException throws if error with writing
	 */
	
	/*
	public static void writeUsers() throws IOException {
		
		
		
		//File data=new File("src/data/users.dat");
		
		File data=new File("users.dat");
		
		
				
		ObjectOutputStream os = null;

		try {
			
			//creates file if it doesn't exist
			if (!data.exists()) {
				data.createNewFile();
			} else {
				//then we have to clear the data before writing
				data.delete();
				data.createNewFile();
			}
			 
			 os = new ObjectOutputStream(new FileOutputStream(data));
			 
			 for (User user:users) {
				 os.writeObject(user);
			 }
			 
			 os.close();
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}

		
		
	}
	
	*/
	
	/**
	 * @throws IOException throws if file does not exist
	 * @throws ParseException throws if data cannot be parsed
	 */
	
	/*
	public static void readUsers() throws IOException, ParseException {
		
		
		//File data = new File("src/data/users.dat");
		
		File data=new File("users.dat");
		
		ObjectInputStream os= null;
		
		try {
			//creates dat file if it doesnt exist
			if (!data.exists()) {
				data.createNewFile();
			} else {
				os= new ObjectInputStream(new FileInputStream(data));
				ArrayList<User> read = new ArrayList<User>();
				
				//reading the data
				while(true) {
					Object usr = os.readObject();
					if (usr==null) {
						break;
					} else {
						read.add((User)usr);
					}
				}
				users=read;
				os.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
}
	
	
	*/
	//deprecated
	public static void readUser() throws IOException, ParseException {
		
		String fileName = "data/users.dat";
		
		File file = new File(fileName);
		
		if (!file.exists()) {
			//if the file does not exist, we should create it and add the stock user,album, and photo
			User stock = new User("stock");
			Album stockAlbum = new Album("stock album");
			stock.addAlbum(stockAlbum);
			Photo stockPhoto= new Photo("stock photo", new File("data/stockPhotos/test.png"));
			stockAlbum.addPhoto(stockPhoto);
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
		/*
		
		String[] names = {""};
		
		String[] fileLoc = {"data/stockPhotos/test.jpg"};
		
		Album stockAlbum = new Album("stock album");
		
		if(file.length() == 0) {
			
			User stock = new User("stock");
			
			users.add(stock);
			
			for(int i = 0; i < fileLoc.length; i++) {
		
				Photo image = new Photo(names[i],new File(fileLoc[i]));
				
				/*
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				
				
				String s = sdf.format(file.lastModified());
				
				Date date = sdf.parse(s);
				
				
				image.setPhotoDate(date); 
				*/
				/*
				stockAlbum.setBeginDate(image.getPhotoDate());
				stockAlbum.setEndDate(image.getPhotoDate());
				
				if(image.getPhotoDate().before(stockAlbum.getBeginDate())) {
					
					stockAlbum.setBeginDate(image.getPhotoDate());
				}
				
				if(image.getPhotoDate().after(stockAlbum.getEndDate())) {
										
					stockAlbum.setEndDate(image.getPhotoDate());
				}
				
				
				stockAlbum.addPhoto(image);	
			
			}
			
			//users.add(stock);
			
			stock.addAlbum(stockAlbum);
			
			//users.add(stock);
			
			
			
			return;
		}
				
		try {
			
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
			 
	        users = (List<User>) in.readObject(); 	
	        
	        in.close();
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
		*/
	}
		
		
}