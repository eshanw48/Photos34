package app;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import PhotosView.LoginController;



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
	
	public void addPhoto(Photo p)
	{
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		Iterator<Photo> photoIter = currentUser.userPhotos.iterator();
		Photo photoToAdd = null;
		
		while (photoIter.hasNext())
		{
			photoToAdd = photoIter.next();
			if(p.isFileLocationEqual(photoToAdd))
			{
				p.setCaption(photoToAdd.caption);
				//p.setTags("name", photoToAdd.getTags("name"));
				//p.setTags("location", photoToAdd.getTags("location"));
				break;
			} else {
				photoToAdd = null;
			}
		}
		
		if (photoToAdd != null)
		{
			photos.add(photoToAdd);
		} else {
			currentUser.userPhotos.add(p);
			photos.add(p);
		}
		
		numOfPhotos++;
	}
	
	public void deletePhoto(Photo p)
	{
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		int lastCopy = 0;
		
		photos.remove(p);
		
		/*remove photo from user*/
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
	}
	
	public void setBeginDate(Date d) {
		
		beginDate = d;
	}
	
	public Date getBeginDate() {
		
		return beginDate;
	}
	
	public Date getEndDate() {
		
		return endDate;
	}
	
	public void setEndDate(Date d) {
		
		endDate = d;
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
