package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Album implements Serializable{
	
	private List<Photo> photos = new ArrayList<Photo>();  // holds all the photos within the album
	
	private String albumName;
	
	private Date beginDate;

	private Date endDate;
	
	private String dateRange; 
	
	private int numOfPhotos;
	
	public Album(String albumName) {
		
		this.albumName = albumName;
		
		numOfPhotos = 0;
	}
	
	public void setAlbumName(String albumName) {
		
		this.albumName = albumName;
	}
	
	public String getAlbumName() {
		
		return albumName;
	}
	
	public String getNumOfPhotos() {
		
		return Integer.toString(numOfPhotos);
	}
}
