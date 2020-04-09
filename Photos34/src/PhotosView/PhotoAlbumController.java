package PhotosView;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
//import for file browser users need to pick their images for albums.
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

public class PhotoAlbumController {

    @FXML
    private Button finalize;

    @FXML
    private TextField caption;

    @FXML
    private Button restore;

    @FXML
    private Button exit;

    @FXML
    private Button logout;

    @FXML
    private ListView<?> photos;

    @FXML
    private Button move;

    @FXML
    private Button copy;

    @FXML
    private Button display;

    @FXML
    private Button remove;

    @FXML
    private Button add;

    @FXML
    private Button view;

    @FXML
    void addButton(ActionEvent event) {
    		//we want this to basically open a file browser so the user can select their photo
    		//we should check for photo specific endings (.jpeg,.png,etc.)?
    		//we should check for duplicates here too.
    		//supported types are bmp,gif,jpeg,png
    	
    	//creating filter for our fileChooser
    	FileChooser.ExtensionFilter imageFilter = new ExtensionFilter("image extensions", new String[]{"*.jpeg","*.png","*.gif","*.bmp"});
    	
    	//creating the actual fileChooser
    	FileChooser imageSelector = new FileChooser();
    	
    	//setting window title for file browser popup
    	imageSelector.setTitle("Please select an image.");
    	//applying filter
    	imageSelector.setSelectedExtensionFilter(imageFilter);
    	
    	//showing the file browser to select a file
    	File selectedFile = imageSelector.showOpenDialog(add.getScene().getWindow());
    	
    	if (selectedFile==null || !selectedFile.isFile()) {
    		//error case
    	} else {
    		//then we should create a photo object and see if its possible to add into the current album
    		
    	}
    	
    }

    @FXML
    void copyButton(ActionEvent event) {

    }

    @FXML
    void displayButton(ActionEvent event) {

    }

    @FXML
    void exitButton(ActionEvent event) {
    	
    	Platform.exit();
    	System.exit(0);

    }

    @FXML
    void finalizeButton(ActionEvent event) {

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

    @FXML
    void moveButton(ActionEvent event) {

    }

    @FXML
    void removeButton(ActionEvent event) {

    }

    @FXML
    void restoreButton(ActionEvent event) {

    }

    @FXML
    void viewButton(ActionEvent event) {

    }

}
