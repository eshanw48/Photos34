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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PhotosMoveCopyController {

	@FXML
	private Label welcome;
	
    @FXML
    private Button exit;

    @FXML
    private Button logout;

    @FXML
    private ListView<Album> albums;

    @FXML
    private Button back;

    @FXML
    private Button confirm;
    
    private static ObservableList<Album> otherAlbumList;
    
    private static boolean albumOrDisplay;
    
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

    @FXML
    void exitButton(ActionEvent event) {
    	
    	Platform.exit();
    	System.exit(0);

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

	public static boolean isAlbumOrDisplay() {
		return albumOrDisplay;
	}

	public static void setAlbumOrDisplay(boolean other) {
		albumOrDisplay = other;
	}
    
    

}
