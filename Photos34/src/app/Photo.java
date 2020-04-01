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
public class Photo implements Serializable {
	
	
	
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

	
	/**
	 * 
	 * @return
	 */
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public LocalDateTime getPhotoDate() {
		return photoDate;
	}

	public void setPhotoDate(LocalDateTime photoDate) {
		this.photoDate = photoDate;
	}

	public List<Tag> getPhotoTags() {
		return photoTags;
	}

	public void setPhotoTags(List<Tag> photoTags) {
		this.photoTags = photoTags;
	}

}
