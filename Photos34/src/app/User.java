package app;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




/**
 * Class that represents the users of our photo application.
 * @author Eshan Wadhwa and Vishal Patel
 *
 */
public class User implements Serializable{
	
	/**
	 * String which represents the name of the user.
	 */
	String userName;  // contains the user's user name
	
	/**
	 * List of Albums which are associated with the user.
	 */
	private List<Album> albums = new ArrayList<Album>();  // holds all the albums that a User has
	
	
	
	//every tag that this user has in their list
	/**
	 * List of tags that are visible to the user for adding to photos.
	 */
	private List<Tag> availableTags = new ArrayList<Tag>();
	
	/**
	 * Constructor for the User. Initializes the person and location tags to the User so that they have some premade tags to add to photos.
	 * @param userName Name of the user as a string.
	 */
	public User(String userName) { // User constructor
		
		this.userName = userName;
		this.availableTags.add(new Tag("person",true));
		this.availableTags.add(new Tag("location",false));
	}
	
	/**
	 * Getter for a list of tags that the user can use.
	 * @return Returns a list of available tags for the user.
	 */
	public List<Tag> getAvailableTags(){
		return this.availableTags;
	}
	
	/**
	 * Method to add a tag to the list of available tags for the user.
	 * @param tagName Represents the name of the tag to add.
	 * @param isMultiple Represents if the tag can take multiple values (true) or not (false)
	 * @return Returns true if the tag was successfully added, and returns false if the tag has the same name as another tag in the list, in which case the addition is not executed.
	 */
	public boolean addAvailableTag(String tagName,boolean isMultiple) {
		Iterator<Tag> tags = availableTags.iterator();
		while(tags.hasNext()) {
			Tag consider = tags.next();
			if (consider.getName().equals(tagName.trim().toLowerCase())) {
				//then we have a repeat
				return false;
			}
		}
		//then we dont have a duplicate
		this.availableTags.add(new Tag(tagName.trim().toLowerCase(),isMultiple));
		return true;
	}
	
	/**
	 * Removes a tag from the list of tags visible to the user.
	 * @param tagName String of the tag to remove.
	 * @return Returns true if the tag was removed. Returns false if the tag was not found, so no removal is done.
	 */
	public boolean removeAvailableTag(String tagName) {
		Iterator<Tag> tags = availableTags.iterator();
		while(tags.hasNext()) {
			Tag consider = tags.next();
			if (consider.getName().equals(tagName.trim().toLowerCase())) {
				//then we remove this
				tags.remove();
				return true;
			}
		}
		//then we dont have a match
		return false;
	}
	
	public String toString() {
		
		return userName;
	}
	
	/**
	 * Setter for user's username.
	 * @param userName String of the users desired name.
	 */
	public void setUserName(String userName) {
		
		this.userName = userName;
	}
	
	/**
	 * Method to add an album to a user.
	 * @param album This is the Album to associate with the user.
	 * @return Returns true if the album is successfully added. Returns false if this album has the same name as another of the user's albums, in which case the add is not executed.
	 */
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
	
	
	/**
	 * Deletes an album from the user's list of associated albums.
	 * @param index Represents the index of the album to delete.
	 * @return Returns true if the album was successfully deleted and false if the index is invalid.
	 */
	public boolean delAlbum(int index) {
		if (index<0 || index>=this.albums.size()) {
			return false;
		}
		albums.remove(index);
		return true;
	}
	
	/**
	 * Getter for an album.
	 * @param index Index of the album to get.
	 * @return Returns the album if found, and returns null if the index is invalid.
	 */
	public Album getAlbum(int index) {
		if (index<0 || index>=this.albums.size()) {
			return null;
		}
		return albums.get(index);
	}
	
	/**
	 * Gets the iterator of albums for this user.
	 * @return Returns the iterator that can iterate through the albums of this user.
	 */
	public Iterator<Album> albumIterator() {
		
		return albums.iterator();
	}
	
	/**
	 * Returns the list of albums for this user.
	 * @return Returns a list of albums associated with this user.
	 */
	public List<Album> getAlbums(){
		return this.albums;
	}
	
	/**
	 * Method to search and find photos based on date.
	 * @param early This is the LocalDateTime instance which represents the start date for the search.
	 * @param late This is the LocalDateTime instance which represents the end date for the search.
	 * @return Returns the list of photos which are in the date range.
	 */
	public List<Photo> searchDate(LocalDateTime early, LocalDateTime late){
		//need to iterate through all albums and through all photos
		List<Photo> results = new ArrayList<Photo>();
		Iterator<Album> albums = this.albumIterator();
		while (albums.hasNext()) {
			//if the latest date is earlier than early, or the earliest date later than late, then we can skip this
			Album toConsider = albums.next();
			if (toConsider.getEndDate()==null || toConsider.getBeginDate()==null) {
				//then no photos, so we can skip
				continue;
			}
			if (toConsider.getEndDate().toLocalDate().compareTo(early.toLocalDate())<0 ) {
				//then we skip this album
			} else if (toConsider.getBeginDate().toLocalDate().compareTo(late.toLocalDate())>0) {
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
		return removeDuplicate(results);
	}
	
	/**
	 * Method to search for photos based on a single tag value pair.
	 * @param tag String of the tag to find.
	 * @param val String of the value associated with the tag.
	 * @return Returns a list of all photos associated with this user which have this tag and associated value.
	 */
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
		return removeDuplicate(results);
	}
	
	/**
	 * Overloaded method for searchTag, which searches for photos based on two tag-value pairs.
	 * @param tag1 First tag string name.
	 * @param val1 Value (string) to find associated with the first tag.
	 * @param tag2 Second tag string name.
	 * @param val2 Value (string) to find associated with the second tag.
	 * @param orAnd Boolean that represents if we want to find photos that match both tag-value pairs (false), or match one of the two tag-value pairs (true).
	 * @return Returns a list of photos associated with the search.
	 * @throws Exception Throws an exception if we try to search with both tags being the same tag and the tag cannot support multiple values.
	 */
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
								 return removeDuplicate(first);
							} else {
								//then we have AND
								 first.retainAll(second);
								 return removeDuplicate(first);
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
			 return removeDuplicate(first);
		} else {
			//then we have AND
			 first.retainAll(second);
			 return removeDuplicate(first);
		}
	}
	
	/**
	 * Helper method that removes duplicate photos in a list of photos.
	 * @param photos Given list of photos.
	 * @return Returns a list of photos which has all the photos of the original list with no duplicates.
	 */
	public List<Photo> removeDuplicate(List<Photo> photos){
		List<Photo> withoutDupe = new ArrayList<Photo>();
		Iterator<Photo> orig = photos.iterator();
		while (orig.hasNext()) {
			Photo toConsider = orig.next();
			if (withoutDupe.contains(toConsider)) {
				//then we dont add
			} else {
				withoutDupe.add(toConsider);
			}
		}
		return withoutDupe;
	}
	
	

}