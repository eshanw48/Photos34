package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




public class User implements Serializable{
	
	String userName;  // contains the user's user name
	
	private List<Album> albums = new ArrayList<Album>();  // holds all the albums that a User has
	
	List<Photo> userPhotos = new ArrayList<Photo>();
	
	public User(String userName) { // User constructor
		
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
	
	public Album getAlbum(int index) {
		
		return albums.get(index);
	}
	
	public Iterator<Album> albumIterator() {
		
		return albums.iterator();
	}
	
	public Iterator<Photo> userPhotosIterator() {
		
		return userPhotos.iterator();
	}
	
	
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
	
}
}