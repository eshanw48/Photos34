package app;

import java.io.Serializable;


public class User implements Serializable{
	
	String userName;  
	

	public User(String userName) { 
		
		this.userName = userName;
	}
	
	public String toString() {
		
		return userName;
	}
	
	public void setUserName(String userName) {
		
		this.userName = userName;
	}
		
	
	
}