package app;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;




public class User implements Serializable{
	
	String userName;  // contains the user's user name
	
	private List<Album> albums = new ArrayList<Album>();  // holds all the albums that a User has
	
	/* Photos shouldnt exist without an album
	List<Photo> userPhotos = new ArrayList<Photo>();
	*/
	
	public User(String userName) { // User constructor
		
		this.userName = userName;
	}
	
	public String toString() {
		
		return userName;
	}
	
	public void setUserName(String userName) {
		
		this.userName = userName;
	}
	
		
	public boolean addAlbum(Album album) {
		
		
		
			
		Iterator<Album> albumIter = this.albumIterator();
		
		while(albumIter.hasNext()) {
			
			
			if(albumIter.next().equals(album)) {
				
				//then we are trying to add an album with the same exact name as an existing one
				return false;
				
				
				
			}
			
		}
		//no duplicates found, so we can safely add
		albums.add(album);
		return true;
	}
	
	
	
	public boolean delAlbum(int index) {
		if (index<0 || index>=this.albums.size()) {
			return false;
		}
		albums.remove(index);
		return true;
	}
	
	public Album getAlbum(int index) {
		if (index<0 || index>=this.albums.size()) {
			return null;
		}
		return albums.get(index);
	}
	
	public Iterator<Album> albumIterator() {
		
		return albums.iterator();
	}
	
	
	public List<Album> getAlbums(){
		return this.albums;
	}
	
	public List<Photo> searchDate(LocalDateTime early, LocalDateTime late){
		//need to iterate through all albums and through all photos
		List<Photo> results = new ArrayList<Photo>();
		Iterator<Album> albums = this.albumIterator();
		while (albums.hasNext()) {
			//if the latest date is earlier than early, or the earliest date later than late, then we can skip this
			Album toConsider = albums.next();
			if (toConsider.getEndDate().compareTo(early)<0 && toConsider.getEndDate().getDayOfMonth()!=early.getDayOfMonth()) {
				//then we skip this album
			} else if (toConsider.getBeginDate().compareTo(late)>=0 && toConsider.getBeginDate().getDayOfMonth()!=late.getDayOfMonth()) {
				//then we can skip this album too
			} else {
				//then we need to iterate through all the photos in this album and add matches
				Iterator<Photo> photos = toConsider.photoIterator();
				while (photos.hasNext()) {
					Photo toCheck = photos.next();
					if (toCheck.getPhotoDate().toLocalDate().compareTo(early.toLocalDate())>=0 && toCheck.getPhotoDate().toLocalDate().compareTo(late.toLocalDate())<=0) {
						//then this photo is in our date range
						results.add(toCheck);
					}
				}
			}
		}
		return results;
	}
	
	
	public List<Photo> searchTag(String tag, String val){
		List<Photo> results = new ArrayList<Photo>();
		tag=tag.trim().toLowerCase();
		val=val.trim().toLowerCase();
		
		//checking if the tag exists (if no tag exists, then definitely no photos)
		Iterator<Album> albums = this.albumIterator();
		while(albums.hasNext()) {
			Iterator<Photo> photos = albums.next().photoIterator();
			while(photos.hasNext()) {
				Photo toConsider = photos.next();
				Tag found = toConsider.getTag(tag);
				if (found!=null) {
					//then this tag exists
					Iterator<String> values = found.valueIterator();
					while (values.hasNext()) {
						String value = values.next();
						if (value.equals(val)) {
							//then we add this photo to the list
							results.add(toConsider);
							break;
						}
					}
				}
			}
		}
		return results;
	}
	
	public List<Photo> searchTag(String tag1, String val1, String tag2, String val2, boolean orAnd) throws Exception{
		//orAnd is false if user wants or and true if user wants and
		tag1=tag1.trim().toLowerCase();
		val1=val1.trim().toLowerCase();
		tag2=tag2.trim().toLowerCase();
		val2=val2.trim().toLowerCase();
		if (tag1.equals(tag2) && !val1.equals(val2)) {
			//then we should check if tag1 supports multiple values
			Iterator<Album> albums = this.albumIterator();
			while(albums.hasNext()) {
				Iterator<Photo> photos = albums.next().photoIterator();
				while(photos.hasNext()) {
					Photo toConsider = photos.next();
					Tag found = toConsider.getTag(tag1);
					if (found!=null) {
						//then this tag exists
						if (found.isMultipleValues()) {
							//then we can continue
							List<Photo> first = searchTag(tag1,val1);
							List<Photo> second = searchTag(tag2,val2);
							
							if (orAnd) {
								//then we have or
								 first.addAll(second);
								 return first;
							} else {
								//then we have AND
								 first.retainAll(second);
								 return first;
							}
						} else {
							//then this operation is not supported
							throw new Exception ("Tag Does Not Support Multiple Values!");
						}
						
					}
				}
			}
			
		} else if (val1.equals(val2)) {
			//then we should just call the first method above
			return searchTag(tag1,val1);
		}
		
		List<Photo> first = searchTag(tag1,val1);
		List<Photo> second = searchTag(tag2,val2);
		
		if (orAnd) {
			//then we have or
			 first.addAll(second);
			 return first;
		} else {
			//then we have AND
			 first.retainAll(second);
			 return first;
		}
	}
	
	
	/*
	public Iterator<Photo> userPhotosIterator() {
		
		return userPhotos.iterator();
	}
	*/
	
	/*
	public void updateUserPhotos()
	{
		boolean photoExistsInAnAlbum = false;
		
		Iterator<Album> albumsToCheck = albumIterator();
		Iterator<Photo> userPhotosIter = userPhotosIterator();
		
		if (userPhotosIter.hasNext())
		{
			if (albumsToCheck.hasNext())
			{
				while(userPhotosIter.hasNext())
				{
					photoExistsInAnAlbum = false;
					Photo userPhotoToCheck = userPhotosIter.next();
					
					while (albumsToCheck.hasNext())
					{
						Album currentAlbum = albumsToCheck.next();
						Iterator<Photo> photosToCheck = currentAlbum.photoIterator();
						
						while (photosToCheck.hasNext())
						{
							Photo currentPhoto = photosToCheck.next();
							
							if (currentPhoto.isEqual(userPhotoToCheck))
							{
								photoExistsInAnAlbum = true;
								break;
							}
					
						}
						
						if (photoExistsInAnAlbum)
						{
							break;
						}
					}
					
					
					if (!photoExistsInAnAlbum)
					{
						userPhotos.remove(userPhotoToCheck);
					}
					
				}
			} else {
				while (userPhotosIter.hasNext())
				{
					userPhotosIter.next();
					userPhotosIter.remove();
				}
			}
		}
	
} */
}