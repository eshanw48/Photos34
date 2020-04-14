package app;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




/**
 * Class to represent our Albums for each user, where every album holds a collection of Photos.
 * @author Eshan Wadhwa and Vishal Patel
 *
 */
public class Album implements Serializable{
	
	/**
	 * List of all the photos held in our album.
	 */
	private List<Photo> photos = new ArrayList<Photo>();  // holds all the photos within the album
	
	/**
	 * Name of the album.
	 */
	private String albumName;
	
	/**
	 * Date-Time of the earliest photo in our album.
	 */
	private LocalDateTime beginDate;

	/**
	 * Date-Time of the most recent photo in our album.
	 */
	private LocalDateTime endDate;
	
	/**
	 * Number of photos in our album
	 */
	private int numOfPhotos;
	
	

	

	/**
	 * Constructor that initializes our album.
	 * @param albumName This is the desired name of our album.
	 */
	public Album(String albumName) {
		this.photos=new ArrayList<Photo>();
		
		this.albumName = albumName.trim();
		
		this.numOfPhotos = 0;
		this.beginDate=null;
		this.endDate=null;
		
	}
	
	
	/**
	 * Overriding object equals for album. Two albums are equal if they have the same exact name.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Album) || o==null) {
			return false;
		} else {
			Album toCompare = (Album) o;
			if (toCompare.getAlbumName().equals(this.getAlbumName())) {
				//then they are considered the same album
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Setter for album name.
	 * @param albumName String representing desired album name.
	 */
	public void setAlbumName(String albumName) {
		
		this.albumName = albumName;
	}
	
	/**
	 * Getter for album name.
	 * @return Returns the string that represents the album name.
	 */
	public String getAlbumName() {
		
		return albumName;
	}
	
	/**
	 * Getter for number of photos in our album.
	 * @return Returns an integer that represents the number of photos in this album.
	 */
	public int getNumOfPhotos() {
		
		return numOfPhotos;
	}
	
	
	
	
	/**
	 * Method to add a photo to our album.
	 * @param p This is the photo to add to our album.
	 * @return Returns false if photo already exists in our album and insertion failed. Returns true otherwise.
	 */
	public boolean addPhoto(Photo p)
	{
		//User currentUser = Persistance.getUser(LoginController.getUserIndex());
		Iterator<Photo> photoIter=this.photoIterator();
		
		
		Photo photoToAdd = null;
		
		while (photoIter.hasNext())
		{
			photoToAdd = photoIter.next();
			if(p.equals(photoToAdd))
			{
				//then we found a repeat photo in our album, we should not allow this
				return false;
				
				
				
				
				
			}
		}
		
		this.photos.add(p);
		//we should check here if the date is greater than earliest date
		if (this.photos.size()==1) {
			//then we added the only photo
			this.beginDate=p.getPhotoDate();
			this.endDate=p.getPhotoDate();
		} else {
			//then there is more than one photo here, so we should compare dates
			this.updateDates();
		}
		numOfPhotos++;
		return true;
		
		
		
		
	}
	
	/**
	 * Getter for a specific photo in our album.
	 * @param index Index of the photo to get.
	 * @return Returns the desired Photo object at the index, or null if the index is invalid.
	 */
	public Photo getPhoto(int index) {
		if (index<0 || index>=this.photos.size()) {
			return null;
		}
		return this.photos.get(index);
	}
	
	/**
	 * Method for deleting a specific photo from our album.
	 * @param p Photo to delete
	 * @return Returns true if the photo was found and deleted successfully. Returns false otherwise.
	 */
	public boolean deletePhoto(Photo p)
	{
		Iterator<Photo> photoiter = this.photoIterator();
		
		while (photoiter.hasNext()) {
			if (photoiter.next().equals(p)) {
				photoiter.remove();
				this.numOfPhotos--;
				//need to update dates of photos here, in case the photo we deleted had the earliest or latest date
				this.updateDates();
				return true;
			}
		}
		//picture not found
		return false;
		
		
	
	}
	
	/**
	 * Method to update the start and end dates of our album. (Goes through all photos and sets the earliest and newest dates)
	 */
	public void updateDates() {
		Iterator<Photo> photoiter = this.photoIterator();
		LocalDateTime beg = null;
		LocalDateTime end = null;
		
		Photo toExamine = null;
		
		while (photoiter.hasNext()) {
			toExamine=photoiter.next();
			if (beg==null) {
				//then both beg and end must be null
				beg=toExamine.getPhotoDate();
				end=toExamine.getPhotoDate();
			} else {
				//then we need to compare
				if (beg.compareTo(toExamine.getPhotoDate())>0) {
					//then our beginning date is newer than the date of the photo we found
					beg=toExamine.getPhotoDate();
				}
				if (end.compareTo(toExamine.getPhotoDate())<0) {
					//then our ending date is older then the date of the photo we found
					end=toExamine.getPhotoDate();
				}
			}
		}
		//now we set the values to our album
		this.beginDate=beg;
		this.endDate=end;
	}
	
	/**
	 * Method to tell whether a specific photo is in this album or not.
	 * @param p Photo to check for .
	 * @return Returns true if the photo is in this album and returns false otherwise.
	 */
	public boolean hasPhoto(Photo p) {
		Iterator<Photo> iter = this.photoIterator();
		while(iter.hasNext()) {
			if (iter.next().equals(p)) {
				//then we found a match
				return true;
			}
		}
		//then no match found
		return false;
	}
	
	/**
	 * Method to move a photo from one album to another
	 * @param one Host album for the photo.
	 * @param two Destination album for the photo.
	 * @param p Photo to be moved.
	 * @return Returns true if the photo was successfully moved. Returns false otherwise.
	 */
	public static boolean movePhoto(Album one, Album two, Photo p) {
		if (!one.hasPhoto(p)) {
			//then we cant move the photo!
			return false;
		}
		if (two.addPhoto(p)) {
			//then we should delete it from the host album
			one.deletePhoto(p);
			return true;
		} else {
			//then the second album already has this image!
			return false;
		}
	}
	
	/**
	 * Method to copy a photo from one album to another
	 * @param one Source album
	 * @param two Destination album
	 * @param p Photo to move
	 * @return Returns true if the copy was successful. Returns false otherwise.
	 */
	public static boolean copyPhoto(Album one, Album two, Photo p) {
		if (!one.hasPhoto(p)) {
			//then nothing to copy!
			return false;
		}
		return two.addPhoto(p);
	}
	
	/**
	 * Getter for the begin date.
	 * @return Returns the LocalDateTime instance associated with the earliest Photo in this Album. If Album is empty, this is null.
	 */
	public LocalDateTime getBeginDate() {
		return beginDate;
	}

	/**
	 * Setter for the beginDate.
	 * @param beginDate This is the LocalDateTime instance we wish to set to our early date for the Album.
	 */
	public void setBeginDate(LocalDateTime beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * Getter for the end date.
	 * @return Returns the LocalDateTime instance associated with the oldest photo in this Album. If Album is empty, this is null.
	 */
	public LocalDateTime getEndDate() {
		return endDate;
	}

	/**
	 * Setter for the end date.
	 * @param endDate This is the desired end date LocalDateTime we wish to set for this Album.
	 */
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter for photo iterator.
	 * @return Returns an iterator that can iterate through the photos in this album.
	 */
	public Iterator<Photo> photoIterator() {
		
		return photos.iterator();
	}
	
	
	
	public String toString() {
		
		return String.format("%s %50s %s - %s", albumName,numOfPhotos,beginDate,endDate);
}
	
	/**
	 * Returns a list of photos in this ALbum.
	 * @return Returns a list of the photos in this album, not null.
	 */
	public List<Photo> getPhotos(){
		return this.photos;
	}
	
	/**
	 * Method to return the beginDate converted to "mon-dd-year"
	 * @return Returns a string that represents the date of the earliest picture in this album.
	 */
	public String convertEarlyDate() {
		if (this.beginDate==null) {
			return "";
		}
		//first 3 letters of the month
				String mon = this.beginDate.getMonth().name().substring(3);
				//day of month
				String day = ""+this.beginDate.getDayOfMonth();
				//last two digits of year
				String year = ""+(this.beginDate.getYear()%100);
				return mon+"-"+day+"-"+year;
	}
	
	/**
	 * Method to return a string representing the endDate of the album in format "mon-dd-year"
	 * @return Returns a string that represents the date of the newest photo in the album.
	 */
	public String convertEndDate() {
		if (this.endDate==null) {
			return "";
		}
		//first 3 letters of the month
				String mon = this.endDate.getMonth().name().substring(3);
				//day of month
				String day = ""+this.endDate.getDayOfMonth();
				//last two digits of year
				String year = ""+(this.endDate.getYear()%100);
				return mon+"-"+day+"-"+year;
	}
}
