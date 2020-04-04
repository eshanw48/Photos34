package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.io.File;

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
		
	}

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

}
