package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Class that represents tags, which can hold values.
 * @author Eshan Wadhwa and Vishal Patel
 *
 */
public class Tag implements Serializable{
	
	/**
	 * Represents the name of the tag. I.e person, location, etc.
	 */
	private String name;
	
	/**
	 * Represents the values associated with the tag. I.e for the tag person, we could have Patricia, mary, etc.
	 */
	private List<String> values;
	
	/**
	 * Boolean that represents if the tag can take on multiple values or not. I.e person could take multiple values, but the photo is only taken in ONE location.
	 */
	private boolean multipleValues;
	
	/**
	 * Constructor to initialize a tag.
	 * @param name String that represents the name of the tag.
	 * @param multipleValues Boolean that represents if the tag can take multiple values or not.
	 */
	public Tag(String name, boolean multipleValues)
	{
		this.name = name.trim().toLowerCase();
		this.values=new ArrayList<String>();
		this.multipleValues=multipleValues;
	}
	
	/**
	 * Method to tell if our tag has no values in it.
	 * @return Returns true if there are no values associated with our tag right now. Returns false otherwise.
	 */
	public boolean noTags() {
		return this.values.isEmpty();
	}
	
	/**
	 * Method to get iterator for the values associated with the tag.
	 * @return Returns the iterator that can iterate through the values of this tag.
	 */
	public Iterator<String> valueIterator(){
		return this.values.iterator();
	}
	
	/**
	 * Method to remove a value from list of values, given a String of the value.
	 * @param value This is the String of the value we wish to remove.
	 * @return Returns false if the value failed to remove. Returns true if the value was successfully removed.
	 */
	public boolean removeValue(String value) {
		
		Iterator<String> v = this.valueIterator();
		if (!v.hasNext()) {
			//then empty
			return false;
		} else {
			while(v.hasNext()) {
				if (v.next().equals(value.trim().toLowerCase())) {
					//then we remove this
					v.remove();
					return true;
				}
			
			}
			//no remove found
			return false;
			
		}
	}
	
	/**
	 * Method to add a value to our list of values. No duplicates are allowed. In addition, we can only add multiple values if the tag allows it.
	 * @param value The new value we wish to add to our list of values for this tag.
	 * @return Returns true if the value was successfully added, false if this is a duplicate and the value was not added.
	 */
	public boolean addValue(String value) {
		if (this.values.size()>=1 && !(this.multipleValues)) {
			//then add shouldnt be allowed
			return false;
		}
		String valueToAdd = value.trim().toLowerCase();
		//getting iterator
		Iterator<String> val = this.valueIterator();
		while(val.hasNext()) {
			if (val.next().equals(valueToAdd)) {
				//then duplicate
				return false;
			}
		}
		//then we can safely add
		this.values.add(value);
		return true;
	}
	
	/**
	 * Method to get a value from our list of values, given an index to it.
	 * @param index Index of the value we wish to return.
	 * @return Returns a string of the value we wish to get.
	 */
	public String getValue(int index) {
		if (index<0) {
			return null;
		}
		String toReturn = null;
		Iterator<String> val = this.valueIterator();
		while(val.hasNext() && index>=0) {
			toReturn=val.next();
		}
		if (index>=0) {
			return null;
		} else {
			return toReturn;
		}
	}
	
	/**
	 * Getter for multipleValues boolean.
	 * @return Returns true if the tag can take on multiple values. Otherwise, false.
	 */
	public boolean isMultipleValues() {
		return multipleValues;
	}

	/**
	 * Setter for multipleValues boolean.
	 * @param multipleValues Desired multiplicity of the tag (true=multiple, false=otherwise)
	 */
	public void setMultipleValues(boolean multipleValues) {
		this.multipleValues = multipleValues;
	}
	
	
	/**
	 * Gets the name of the tag.
	 * @return Returns the string associated with the name of the tag.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the name of the tag.
	 * @param newName The desired new name of the tag. The string will be trimmed.
	 */
	public void setName(String newName)
	{
		name = newName.trim();
	}
	
	
	/**
	 * Overriding Object equals. Two tags are equal if they have the same name, regardless of their contents. This is to ensure the user doesn't try to add two tags with the same name.
	 */
	public boolean equals(Object o)
	{
		if (!(o instanceof Tag) || o==null) {
			return false;
		} else {
			Tag other = (Tag) o;
			if (other.name.equals(this.name)) {
				//if the tag names are the same, then they are the same tag
				return true;
			} else {
				return false;
			}
		}
	}
	
	
	
	/**
	 * To String method that returns a string of the form "tagName: entry1 ; entry2 ; ..."
	 */
	public String toString()
	{
		//return tag and entries in the format "tag: entry1 ; entry2 ; entry 3; ..."
		String toReturn="";
		toReturn+= this.getName();
		toReturn+=" : ";
		
		//iterator
		Iterator<String> val = this.valueIterator();
		while(val.hasNext()) {
			toReturn+=val.next();
			toReturn+=" ; ";
		}
		return toReturn;
	}
	
}