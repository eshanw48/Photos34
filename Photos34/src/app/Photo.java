package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Photo implements Serializable {
	
	private String photoName;
	
	private String location;
	
	private String caption = "";
				
	private Date photoDate;
	
	private List<Tag> photoTags = new ArrayList<Tag>();
	
	public Photo(String name, String location)
	{
		photoName = name;
		this.location = location;
	}

}
