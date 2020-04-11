package PhotosView;

import java.io.IOException;

import app.Album;
import app.Persistance;
import app.Photo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
    private ListView<Photo> photos;

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
    private Label albumName;
    
    
    //observeable list to monitor changes in our photo list for the album
    private static ObservableList<Photo> photoList;
    
    
    public void initialize() {
    	//getting the album that was opened
    	int albumIndex = UserController.getOpenAlbumIndex();
    	int userIndex = LoginController.getUserIndex();
    	Album opened = Persistance.getUser(userIndex).getAlbum(albumIndex);
    	//setting up observable list 
    	photoList = FXCollections.observableArrayList(opened.getPhotos());
    	
    	photos.setItems(photoList);
    	
    	if (!photoList.isEmpty()) {
    		//select 1st item if list is not null
    		photos.getSelectionModel().select(0);
    	}
    	
    	//we want to just initialize the listview and the name of the album
    	albumName.setText("Album: "+opened.getAlbumName());
    }

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
    		Photo toAdd = new Photo("Enter Caption",selectedFile);
    		if (Persistance.getUser(LoginController.getUserIndex()).getAlbum(UserController.getOpenAlbumIndex()).addPhoto(toAdd)) {
    			//then add is successful
    		} else {
    			//then we already have this image in the album
    		}
    		
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
