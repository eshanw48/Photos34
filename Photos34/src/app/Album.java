package app;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import PhotosView.LoginController;


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
	
	
	/* We do not need date range, because when the user inputs a date range in the search, we will have to compare anyway
	private String dateRange; 
	*/
	

	
	public Album(String albumName) {
		this.photos=new ArrayList<Photo>();
		
		this.albumName = albumName;
		
		this.numOfPhotos = 0;
		this.beginDate=null;
		this.endDate=null;
		
	}
	
	public void setAlbumName(String albumName) {
		
		this.albumName = albumName;
	}
	
	public String getAlbumName() {
		
		return albumName;
	}
	
	public int getNumOfPhotos() {
		
		return numOfPhotos;
	}
	
	// is there a reason for this method? its not apparent to me (users can only add or delete one photo at a time right?)
	public void opNumOfPhotos(int num, char op)
	{
		if (op == '+')
		{
			numOfPhotos += num;
		} else if (op == '-')
		{
			numOfPhotos -= num;
		}
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
		/* no need to add photo to a user. an album contains photos, and users contain albums
		Iterator<Photo> photoIter = currentUser.userPhotos.iterator();
		*/
		
		Photo photoToAdd = null;
		
		while (photoIter.hasNext())
		{
			photoToAdd = photoIter.next();
			if(p.equals(photoToAdd))
			{
				//then we found a repeat photo in our album, we should not allow this
				return false;
				
				
				
				//p.setCaption(photoToAdd.caption);
				//p.setTags("name", photoToAdd.getTags("name"));
				//p.setTags("location", photoToAdd.getTags("location"));
				
			} /*else {
				photoToAdd = null;
			} */
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
		
		/*
		if (photoToAdd != null)
		{
			photos.add(photoToAdd);
		} else {
			currentUser.userPhotos.add(p);
			photos.add(p);
		}
		*/
		
		
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
		
		
		/* We shouldnt have photos for the user AND for the album. We can just access photos from the album directly
		 	Also, it could be a pain having to synchronize the same photo with different attributes in different albums, since copies of the same photo could be in multiple albums.
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		int lastCopy = 0;
		
		photos.remove(p);
		
		
		Iterator<Album> userAlbums = currentUser.albumIterator();
		while(userAlbums.hasNext())
		{
			Album i = userAlbums.next();
			Iterator<Photo> albumPhotos = i.photoIterator();
			while (albumPhotos.hasNext())
			{
				Photo j = albumPhotos.next();
				if (p.isEqual(j))
				{
					lastCopy = 1;
					break;
				}
			}
			
			if (lastCopy == 1)
			{
				break;
			}
		}
		
		if (lastCopy == 0)
		{
			currentUser.userPhotos.remove(p);
		}

		if (numOfPhotos <= 0)
		{
			numOfPhotos = 0;
		} else {
			numOfPhotos--;
		}
		*/
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
	
	public LocalDateTime getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(LocalDateTime beginDate) {
		this.beginDate = beginDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Iterator<Photo> photoIterator() {
		
		return photos.iterator();
	}
	
	public String getDateRange() {
		
		if(beginDate == null) {
			
			return " - ";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		return  sdf.format(beginDate) + " - " + sdf.format(endDate);
	}
	
	public String toString() {
		
		return String.format("%s %50s %s - %s", albumName,numOfPhotos,beginDate,endDate);
}
}
