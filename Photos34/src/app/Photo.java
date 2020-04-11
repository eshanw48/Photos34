 package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.io.File;
import java.util.Iterator;

/**
 * Class that is intended to store information about a photo that the user gives to Photos.
 * @author Eshan Wadhwa and Vishal Patel
 *
 */
public class Photo implements Serializable , Comparable<Photo>{
	
	
	
	/**
	 * Stores the address of the photo on the user's machine. Intention is to then refer to the photo and display it via URL.
	 */
	private String location;
	
	/**
	 * User made caption for the photo.
	 */
	private String caption;
	
	/**
	 * Date for the "last modified" attribute of the photo file on the user's system in ISO-8601. Has date and time which considers the user's timezone.
	 */
	private LocalDateTime photoDate;
	
	/**
	 * Each photo has a list of tags that the user can refer to in order to find this photo.
	 */
	private List<Tag> photoTags;
	
	/**
	 * Constructor to create an instance that stores our info for a specific photo. 
	 * @param caption This is the string that the user wants to caption the photo with.
	 * @param found This is the photo file that is passed by our java controller, which checks if this file exists before passing it here. So, this file is guaranteed to exist and it is a photo file.
	 */
	public Photo(String caption, File found)
	{
		this.caption = caption;
		this.photoTags=new ArrayList<Tag>();
		
		//setting the date of the file
		long milliseconds = found.lastModified();
		Instant mill = Instant.ofEpochMilli(milliseconds);
		
		//LocalDateTime instance for our photos
		this.photoDate= LocalDateTime.ofInstant(mill,ZoneId.systemDefault());
		this.location="file:"+found.getAbsolutePath();
		
	}
	
	//tag logic
	
	/**
	 * Adds a tag to the list of tags associated with this photo.
	 * @param name Name of the tag to add.
	 * @param value First value associated with the tag.
	 * @param multipleValues  Boolean representing if the tag is allowed to have multiple values or not.
	 * @return returns true if the tag was successfully added. False otherwise.
	 */
	public boolean addTag(String name, String value,boolean multipleValues)
	{
		Tag possibleAdd = new Tag(name,value,multipleValues);
		for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
		{
			Tag element = i.next();
			if(element.equals(possibleAdd))
			{
				return false;
			}
		}
		photoTags.add(possibleAdd);
		
		return true;
	}
	
	/**
	 * Method to remove an entire tag from our photo.
	 * @param tagName This is the name of the tag we wish to remove.
	 * @return Returns true if the tag was successfully deleted. False otherwise.
	 */
	public boolean removeTag(String tagName)
	{
		for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
		{
			Tag element = i.next();
			if(element.getName().equals(tagName.trim().toLowerCase()))
			{
				i.remove();
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Method to add a value to a certain tag for a Photo.
	 * @param tagName Tag we wish to add our value to.
	 * @param value Value to add.
	 * @return Returns true if value successfully added. False otherwise.
	 */
	public boolean addValue(String tagName, String value) {
		for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
		{
			Tag element = i.next();
			if(element.getName().equals(tagName.trim().toLowerCase()))
			{
				return element.addValue(value);
			}
		}
		return false;
	}
	
	/**
	 * Method to remove a value from a certain tag in our Photo. If there are no values in our tag after removal, then we delete the tag.
	 * @param tagName Name of the tag we wish to remove a value from
	 * @param value Value to remove.
	 * @return Returns true if value was successfully removed. False otherwise. 
	 */
	public boolean removeValue(String tagName, String value) {
		for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
		{
			Tag element = i.next();
			if(element.getName().equals(tagName.trim().toLowerCase()))
			{
				if (element.removeValue(value)) {
					//then value was removed and we should check if the tag is empty now
					//if tag is empty then a new iterator should have no next
					Iterator<String> val = element.valueIterator();
					if (!val.hasNext()) {
						//then we should delete the tag
						i.remove();
					}
					return true;
				} else {
					//value was not removed
					return false;
				}
			}
		}
		return false;
	}
	
	public Iterator<Tag> tagIterator() {
		
		return photoTags.iterator();
	}
	
	/**
	 * Overriding Object equals to provide equals functionality for Photos.
	 * Two Photos are equal if they have the same location on the user's machine, regardless of caption.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Photo) || o==null) {
			return false;
		} else {
	
			Photo toCompare = (Photo) o;
			if (toCompare.location==null || this.location==null) {
				return false;
			}
			if (toCompare.location.equals(this.location)){
				//they are located in the same place, so they represent the same photo,regardless of caption
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Compareto implementation to compare two photos based on their localDateTime
	 */
	public int compareTo(Photo other) {
		
		//comparing photos by earlier LocalDateTime
		return this.getPhotoDate().compareTo(other.getPhotoDate());
	}
	/**
	 * Getter for photo location on machine.
	 * @return String that represents the URL Path where the photo is stored on the machine.
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Setter for photo location on machine
	 * @param location This is the photo path to be set for this photo.
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Getter for the caption of a photo.
	 * @return Returns a string, which represents the caption of the photo.
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * Setter for the caption of a photo.
	 * @param caption This is the caption to set for the photo.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * Getter for the date and time of the photo.
	 * @return Returns the LocalDateTime instance that represents the date and time of the photo, based on the machines timezone.
	 */
	public LocalDateTime getPhotoDate() {
		return photoDate;
	}

	/**
	 * Setter for the date and time of the photo.
	 * @param photoDate The localDateTime instance to be set for the Photo.
	 */
	public void setPhotoDate(LocalDateTime photoDate) {
		this.photoDate = photoDate;
	}

	/**
	 * Getter for the list of tags of a photo.
	 * @return Returns a list, which represents the list of tags associated with the photo.
	 */
	public List<Tag> getPhotoTags() {
		return photoTags;
	}

	/**
	 * Setter for the list of tags for the photo.
	 * @param photoTags This is the list of tags to be set for the photo.
	 */
	public void setPhotoTags(List<Tag> photoTags) {
		this.photoTags = photoTags;
	}
	
	/**
	 * String conversion of a Photo into the date it was taken (i.e Mon-Day-Year)
	 */
	public String toString() {
		//first 3 letters of the month
		String mon = this.getPhotoDate().getMonth().name().substring(3);
		//day of month
		String day = ""+this.getPhotoDate().getDayOfMonth();
		//last two digits of year
		String year = ""+(this.getPhotoDate().getYear()%100);
		return mon+"-"+day+"-"+year;
	}

}
