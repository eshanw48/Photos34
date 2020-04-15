package PhotosView;

import java.io.IOException;
import java.util.Iterator;

import app.Persistance;
import app.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
	
	/**
	 * Button to login
	 */

    @FXML
    private Button submit;
    
    /**
	 * Textfield to enter username
	 */

    @FXML
    private TextField username;
    
    /**
	 * Button to clear text in textfield
	 */

    @FXML
    private Button clear;
    
    /**
	 * Button exit the program
	 */

    @FXML
    private Button exit;
    
    /**
	 * Integer giving the index of the current user that is active
	 */
    
    private static int userIndex;
    
    
    /**
     * Method to clear the username textfield.
     * @param event Event triggered by user pressing clear button.
     */

    @FXML
    void clearButton(ActionEvent event) {
    	
    	username.setText("");

    }
    
    
    /**
     * Method to exit the program.
     * @param event Event triggered by user pressing exit button.
     */

    @FXML
    void exitButton(ActionEvent event) throws IOException {
    	
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
    
    
    /**
     * Method to submit the username textfield and go to the next stage.
     * @param event Event triggered by user pressing submit button.
     */

    @FXML
    void submitButton(ActionEvent event) {
    
    	
    	String user = username.getText().trim();
		if(user.equals("") || user == null){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Must input a username");
			alert.show();
			return;
		}
		
		if(user.equalsIgnoreCase("admin")){
			try {
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/PhotosView/Admin.fxml"));
				AnchorPane rootLayout = (AnchorPane) loader.load();
				
				Scene scene = new Scene(rootLayout);
				
				stage.setScene(scene);
				((Node)event.getSource()).getScene().getWindow().hide();
				stage.show();
				return;
				
			} catch (IOException m) {
				m.printStackTrace();
			}
		}
	
		Iterator<User> userIter = Persistance.userIterator();
		
		for(int i = 0; userIter.hasNext(); i++) {
						
			if(userIter.next().toString().equals(user)) {
				
				userIndex = i;  // index in user array where current user is located
						
				try {
					Stage stage = new Stage();
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/PhotosView/User.fxml"));
					AnchorPane rootLayout = (AnchorPane) loader.load();
					
					Scene scene = new Scene(rootLayout);
					
					stage.setScene(scene);
					((Node)event.getSource()).getScene().getWindow().hide();
					stage.show();
					return;
					
				} catch (IOException m) {
					m.printStackTrace();
				}
							
			}	
						
		}
		
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Input Error");
		error.setContentText("Nonexistent User Name");
		error.show();
		
		username.clear();
	
    }
    
    
    /**
     * Method to get the index of the current user active.
     */
   
    public static int getUserIndex() {
		
		return userIndex;
	}
}



