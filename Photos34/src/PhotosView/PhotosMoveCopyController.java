package PhotosView;

import java.io.IOException;

import app.Album;
import app.Persistance;
import app.Photo;
import app.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * UI controller which manages the move photo or copy photo to another album buttons.
 * @author Eshan Wadhwa and Vishal Patel.
 *
 */

public class PhotosMoveCopyController {
	
	/**
	 * Label that changes dynamically based on what the user clicked (move or copy)
	 */

	@FXML
	private Label welcome;
	
	/**
	 * Button to exit the program
	 */
	
    @FXML
    private Button exit;
    
    /**
	 * Button to logout of the program
	 */

    @FXML
    private Button logout;
    
    /**
	 * ListView of the albums
	 */

    @FXML
    private ListView<Album> albums;
    
    /**
	 * Button to go back to where the user came from
	 */

    @FXML
    private Button back;
    
    /**
	 * Button to confirm the move or copy
	 */

    @FXML
    private Button confirm;
    
    /**
	 * OberservableList of albums
	 */
    
    private static ObservableList<Album> otherAlbumList;
    
    /**
	 * Boolean to keep track of where the user came from
	 */
    
    private static boolean albumOrDisplay;
    
    /**
	 * Method that is automatically called when the user gets to this stage.
	 */
    
    public void initialize() {
    	//we first want to initialize the label
    	StringBuilder greeting = new StringBuilder("Please select the album you want to ");
    	String operation;
    	if (PhotoAlbumController.copyOrMove) {
    		operation="copy ";
    	} else {
    		operation="move ";
    	}
    	greeting.append(operation);
    	greeting.append("to!");
    	welcome.setText(greeting.toString());
    	
    	//setting up listview and observable list for our albums that are not the current one
    	User currUser = Persistance.getUser(LoginController.getUserIndex());
    	Album currentAlbum = currUser.getAlbum(UserController.getOpenAlbumIndex());
    	otherAlbumList=FXCollections.observableArrayList(currUser.getAlbums());
    	//removing the selected source album from the list of albums to consider
    	otherAlbumList.remove(UserController.getOpenAlbumIndex());
    	
    	//setting up the actual listview
    	albums.setItems(otherAlbumList);
    	
    	//setting up cell factory
    	albums.setCellFactory(new Callback<ListView<Album>,ListCell<Album>>(){
    		@Override
    		 public ListCell<Album> call(ListView<Album> p) {
                
                ListCell<Album> cell= new ListCell<Album>(){
 
                    @Override
                    protected void updateItem(Album p, boolean bln) {
                        super.updateItem(p, bln);
                        if (p != null) {
                        	
                            setText(p.getAlbumName());
                        }
                        else if (p == null)
                        {
                        	
                        	setText(null);
                        }
                    }
 
                };
                 
                return cell;
            }
    	});
    	
    	//setting first album to automatically be selected
    	albums.getSelectionModel().select(0);
    }
    
    /**
     * Method to go back where the user came from.
     * @param event Event triggered by user pressing back button.
     */


    @FXML
    void backButton(ActionEvent event) {
    	//should go back to either photoAlbumController view or PhotoDisplay view based on where the user came from
    	if (isAlbumOrDisplay()) {
    		//then we came from album display
    		try {
    			Stage stage = new Stage();
    			FXMLLoader loader = new FXMLLoader();
    			loader.setLocation(getClass().getResource("/PhotosView/PhotoAlbum.fxml"));
    			AnchorPane rootLayout = (AnchorPane) loader.load();
    			
    			Scene scene = new Scene(rootLayout);
    			
    			stage.setScene(scene);
    			((Node)event.getSource()).getScene().getWindow().hide();
    			stage.show();	
    			
    		} catch (IOException m) {
    			m.printStackTrace();
    		}
    	} else {
    		//then we came from photo display
    		try {
    			Stage stage = new Stage();
    			FXMLLoader loader = new FXMLLoader();
    			loader.setLocation(getClass().getResource("/PhotosView/PhotoDisplay.fxml"));
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
    
    /**
     * Method to confirm the changes.
     * @param event Event triggered by user pressing confirm button.
     */


    @FXML
    void confirmButton(ActionEvent event) {
    	//confirms the move or cancel to the destination album
    	//doing the move
    	Album moveTo = albums.getSelectionModel().getSelectedItem();
    	//adding the photo
    	User currUser = Persistance.getUser(LoginController.getUserIndex());
    	Album currAlbum=currUser.getAlbum(UserController.getOpenAlbumIndex());
    	Photo toAdd = currAlbum.getPhoto(PhotoAlbumController.getOpenPhotoIndex());
    	//if the photo exists already in the album, we throw an error
    	//we have to check if we have a move operation, or a copy operation
    	if (PhotoAlbumController.getCopyOrMove()) {
    		//then we copy
    		if(!Album.copyPhoto(currAlbum, moveTo, toAdd)) {
    			//then copy was not successful and we should display error
    			//error is that the destination album already has this photo!
    			Alert error = new Alert(AlertType.ERROR);
    			error.setTitle("Copy Error");
    			error.setContentText("Selected Album Has This Photo!");
    			error.show();
    			return;
    		} else {
    			//displaying confirmation message
    			Alert error = new Alert(AlertType.INFORMATION);
    			error.setTitle("Success!");
    			error.setContentText("Photo Successfully Copied!");
    			error.show();
    			return;
    		}
    	} else {
    		//then we move
    		if (!Album.movePhoto(currAlbum, moveTo, toAdd)) {
    			//then error is that the destination album already has this photo
    			Alert error = new Alert(AlertType.ERROR);
    			error.setTitle("Move Error");
    			error.setContentText("Selected Album Has This Photo!");
    			error.show();
    			return;
    		} else {
    			//then the move was successful, and we will go back to the album list of the source Album
    			try {
        			Stage stage = new Stage();
        			FXMLLoader loader = new FXMLLoader();
        			loader.setLocation(getClass().getResource("/PhotosView/PhotoAlbum.fxml"));
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
    
    /**
     * Method to exit the program.
     * @param event Event triggered by user pressing exit button.
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
    
    /**
     * Method to logout of the program.
     * @param event Event triggered by user pressing logout button.
     */


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
    
    /**
     * Method to return boolean.
     * @return boolean where the user came from.
     */


	public static boolean isAlbumOrDisplay() {
		return albumOrDisplay;
	}
	
	
	/**
	 * Method to set where the user came from.
	 * @param other Other is true if they came from Album Scene, but false if they came from display screen.
	 */
	public static void setAlbumOrDisplay(boolean other) {
		albumOrDisplay = other;
	}
    
    

}
