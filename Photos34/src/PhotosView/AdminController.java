package PhotosView;


import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;


import app.Persistance;
import app.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * UI controller which manages screen for the admin view of our photos app.
 * @author Eshan Wadhwa and Vishal Patel.
 *
 */
public class AdminController implements Initializable {

    @FXML
    private Button delete;

    @FXML
    private TextField username;

    @FXML
    private Button add;

    @FXML
    private Button exit;

    @FXML
    private Button logout;

    @FXML
    private ListView<User> users;
    
    private ObservableList<User> usersList;
    
    /**
     * Method to add a user to our list of users.
     * @param event Event triggered by user pressing add button.
     */
    @FXML
    void addButton(ActionEvent event) {
    	
    	String userName = username.getText().trim();  // get text input from text box
		
		
		
		if(userName.isEmpty()) {  // nothing entered; invalid user name
			
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("Must Enter User Name");
			error.show();
			
			return;
		}
		
		String[] userCheck = userName.split(" ");
		
		if(userCheck.length > 1) {
			
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("User Name must be one word");
			error.show();
			
			username.clear();
			
			return;
		}
		
		for(User u: usersList) {  // check for duplicate user name
			
			if(userName.equals(u.toString())) {   // if duplicate, do not create user
				
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Input Error");
				error.setContentText("Existing Username");
				error.show();
				
				username.clear();
				
				return;
				
			}
						
		}
		
		User newUser = new User(userName);  // creates new user object
		
		usersList.add(newUser);  // add new user to observable list
				
		Persistance.addUser(newUser);  // add user to user array
		
		users.getSelectionModel().select(newUser);  // select the user in listview
		
		username.clear();  // clear text box
    	

    }

    /**
     * Method to delete a user from our list of users.
     * @param event Event triggered by a user hitting the delete button.
     */
    @FXML
    void deleteButton(ActionEvent event) {
    	if(usersList.isEmpty()) {  // if list empty; nothing to delete
			
			return;
		}
		
		User selectedAlbum = users.getSelectionModel().getSelectedItem();  // gets selected album
		if (selectedAlbum.toString().equals("stock")) {
			//then delete shouldnt be allowed
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Delete Error");
			error.setContentText("Cannot Delete Stock Album!");
			error.show();
			return;
		}
		
		int i = 0;
		for(User u: usersList) {  // finds album in observable list
			
			if(selectedAlbum.toString().equals(u.toString())) {  
				
				break;
				
			}
			
			i++;
		}
		
		usersList.remove(i);  // remove song from observable list
		
		Persistance.delUser(i);  // delete user from array
		
		if(usersList.size() == i) {  // if last item in list is deleted
			
			users.getSelectionModel().select(--i);  // select previous item in list
			
		} else {
			
			users.getSelectionModel().select(i);  // select next item in list
			
		}
    	
    	
    }

    /**
     * Method to exit the photos application.
     * @param event Event triggered by user hitting the exit button.
     */
    @FXML
    void exitButton(ActionEvent event) {
    	try {
    	Persistance.writeUser();
    	
    	Platform.exit();
    	System.exit(0);
    	} catch (IOException e) {
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Save Error");
			error.setContentText("Error Saving! Will Quit Without Saving!");
			error.showAndWait();
			
			Platform.exit();
	    	System.exit(0);
			
    	}

    }

    @FXML
    void logoutButton(ActionEvent event) {
    	
    	try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/PhotosView/Login.fxml"));
			AnchorPane rootLayout = (AnchorPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			
			stage.setScene(scene);
			((Node)event.getSource()).getScene().getWindow().hide();
			stage.show();	
			
		} catch (IOException m) {
			m.printStackTrace();
		}

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		   		
		    	usersList = FXCollections.observableArrayList(); 
			
				Iterator<User> userIter = Persistance.userIterator();  // iterator to go through user array
				
				while(userIter.hasNext()) {  // loads all the users into the listview 
					
					usersList.add(userIter.next());
				}
				
				users.setItems(usersList);  // sets Observable list to the listview
				
				if(!usersList.isEmpty()) {  // if list is not empty; select first item
					
					users.getSelectionModel().select(0);
				}
		   		
		   	}

	}

   