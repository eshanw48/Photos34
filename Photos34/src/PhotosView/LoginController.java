package PhotosView;

import java.io.IOException;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button submit;

    @FXML
    private TextField username;

    @FXML
    private Button clear;

    @FXML
    private Button exit;

    @FXML
    void clearButton(ActionEvent event) {
    	
    	username.setText("");

    }

    @FXML
    void exitButton(ActionEvent event) {
    	
    	Platform.exit();
    	System.exit(0);


    }

    @FXML
    void submitButton(ActionEvent event) {
    
    	
    	String user = username.getText();
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
				
			} catch (IOException m) {
				m.printStackTrace();
			}
		}
		
    }
}


