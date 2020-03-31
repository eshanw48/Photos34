package app;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


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