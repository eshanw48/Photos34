package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
public class Photo implements Serializable{
	
	
	
	private String photoName;
	
	private String location;
	
	String caption = "";
				
	private Date photoDate;
	
	private List<Tag> photoTags = new ArrayList<Tag>();
	
	public Photo(String name, String location)
	{
		photoName = name;
		this.location = location;
	}
	
	public boolean addTag(String name, String value)
	{
		for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
		{
			Tag element = i.next();
			if(element.isEqual(name, value))
			{
				return false;
			}
		}
		photoTags.add(new Tag(name, value));
		
		return true;
	}
	
	public boolean removeTag(String name, String value)
	{
		for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
		{
			Tag element = i.next();
			if(element.isEqual(name, value))
			{
				i.remove();
				return true;
			}
		}

		return false;
	}
	
	public Iterator<Tag> tagIterator() {
		
		return photoTags.iterator();
	}
	
	public String getPhotoName()
	{
		return photoName;
	}
	
	public void setPhotoName(String name)
	{
		photoName = name;
	}
	
	public String getPhotoLocation()
	{
		return location;
	}
	
	public void setPhotoLocation(String filePath)
	{
		location = filePath;
	}
	
	public String getCaption()
	{
		return caption;
	}
	
	public void setCaption(String caption)
	{
		this.caption = caption;
	}
	
	public Date getPhotoDate() {
		
		return photoDate;
	}
	
	public String getPhotoDateString()
	{
		return photoDate.toString();
	}
	
	public void setPhotoDate(Date d) {
		
		photoDate = d;
	}

	public String getTags(String name)
	{
		StringBuilder tagList = new StringBuilder();
		
		for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
		{
			Tag element = i.next();
			String tagSet[] = element.toString().split("~");
			if (tagSet[0].equals(name))
			{
				tagList.append(tagSet[1] + ",");
			}
		}
		
		if (tagList.length() > 0)
		{
			tagList.deleteCharAt(tagList.length()-1);
		}
		
		return tagList.toString();
	}
	
	public void setTags(String name, String tags)
	{
		String[] tagArray = tags.split(",");
		
		for (int i = 0; i < tagArray.length; i++)
		{
			Tag tag = new Tag(name, tagArray[i]);
			photoTags.add(tag);
		}
	}
	
	public String getAllTags()
	{
		StringBuilder tagList = new StringBuilder();
		
		for (Iterator<Tag> i = photoTags.iterator(); i.hasNext();)
		{
			Tag element = i.next();
			String tagSet[] = element.toString().split("~");
			tagList.append(tagSet[1] + ",");
		}
		
		if (tagList.length() > 0)
		{
			tagList.deleteCharAt(tagList.length()-1);
		}
		
		return tagList.toString();
	}
	
	/**
	 * 
	 * @param p
	 * @return true if two photos share the same file location
	 */
	public boolean isFileLocationEqual(Photo p)
	{
		return this.getPhotoLocation().compareTo(p.getPhotoLocation()) == 0;
	}
	
	/**
	 * 
	 * @param p
	 * @return true if two photos are equal
	 */
	public boolean isEqual(Photo p)
	{
		return this.getPhotoName().compareTo(p.getPhotoName()) == 0
				&& this.getPhotoLocation().compareTo(p.getPhotoLocation()) == 0
				&& this.getPhotoDateString().compareTo(p.getPhotoDateString()) == 0
				&& this.getCaption().compareTo(p.getCaption()) == 0
				&& this.getAllTags().compareTo(p.getAllTags()) == 0;
	}
	
	/**
	 * returns the photoName
	 */
	public String toString()
	{
		return getPhotoName();
	}



}

