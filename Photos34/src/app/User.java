package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class User implements Serializable{
	
	String userName;  
	
	private List<Album> albums = new ArrayList<Album>();  // holds all the albums that a User has
	
	private List<Photo> userPhotos = new ArrayList<Photo>();
	

	public User(String userName) { 
		
		this.userName = userName;
	}
	
	public String toString() {
		
		return userName;
	}
	
	public void setUserName(String userName) {
		
		this.userName = userName;
	}
	
	public void addAlbum(Album album) {
		
		albums.add(album);
	}
	
	public void delAlbum(int index) {
		
		albums.remove(index);
	}
	
	public Iterator<Album> albumIterator() {
		
		return albums.iterator();
	}
	
}