package PhotosView;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import app.Album;
import app.Persistance;
import app.Photo;
import app.Tag;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class PhotoDisplayController implements Initializable {
	
	/**
	* Button to finalize caption
	*/
	
    @FXML
    private Button finalize;
    
    /**
  	 * Textfield to enter caption
  	 */

    @FXML
    private TextField caption;
    
    /**
	* Button to restore caption to what it was before the edit
	*/

    @FXML
    private Button restore;
    
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
	* ImageView to display the image
	*/

    @FXML
    private ImageView images;
    
    /**
	* Button to go back to the photoAlbum scene
	*/

    @FXML
    private Button album;
    
    /**
	* Button to go to the copy scene
	*/

    @FXML
    private Button copy;
    
    /**
	* Button to go to the move scene
	*/

    @FXML
    private Button move;
    
    /**
	* Textfield which is uneditable which displays the last date the image was modified
	*/

    @FXML
    private TextField time;
    
    /**
	* ListView which displays the tags the photo has
	*/

    @FXML
    private ListView<Tag> tags;
    
    /**
	* Button to add a tag to the photo
	*/

    @FXML
    private Button add;
    
    /**
	* Button to remove the value of a tag to the photo
	*/

    @FXML
    private Button remove;
    
    /**
	* Button to remove a tag to the photo
	*/
    
    @FXML
    private Button removeTag;
    
    /**
	* Button to add a value of a tag to the photo
	*/
    
    @FXML
    private Button addValue;
    
    /**
	* ObeservableList of the tags
	*/
    
    private static ObservableList<Tag> tagList;
    
    /**
     * Method to add a tag to the photo.
     * @param event Event triggered by user pressing add button.
     */
    

    @FXML
    void addButton(ActionEvent event) {
    	//we should go to tag scene so user can select from preselected tags or make their own
    	try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/PhotosView/tagView.fxml"));
			AnchorPane rootLayout = (AnchorPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			
			//try for popup
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UTILITY);
			stage.setTitle("Adding Tags");
			stage.setOnHidden(e-> {
				User currentUser = Persistance.getUser(LoginController.getUserIndex());
				Album opened = currentUser.getAlbum(UserController.getOpenAlbumIndex());
				Photo photo = opened.getPhoto(PhotoAlbumController.getOpenPhotoIndex());
				tagList = FXCollections.observableArrayList(photo.getPhotoTags());
				tags.setItems(tagList);
				if (!tagList.isEmpty()) {
		    		//select 1st item if list is not null
		    		tags.getSelectionModel().select(0);
		    	}
				});
			
			stage.setScene(scene);
			stage.show();	
			
			
		} catch (IOException m) {
			m.printStackTrace();
		}
		
    }
    
    /**
     * Method to add a value of a tag to the photo.
     * @param event Event triggered by user pressing addValue button.
     */
		
    
    @FXML
    void addValue(ActionEvent event) {
    	if (tagList.isEmpty()) {
			//then no tag to remove values from
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Tag Error");
			error.setContentText("Photo's Tag List is Empty!");
			error.show();
			return;
		}
		
		
		//now we show dialogue popup to remove a value
		TextInputDialog dialog = new TextInputDialog("Value");
		dialog.setTitle("Value Input Dialog");
		//dialog.setHeaderText("Look, a Text Input Dialog");
		dialog.setContentText("Please enter the value to add:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		String valueToAdd;
		if (result.isPresent()){
		  //System.out.println("Your name: " + result.get());
		    valueToAdd=result.get();
		} else {
			//then user cancelled
			return;
		}
		
		if (tags.getSelectionModel().getSelectedItem().addValue(valueToAdd.trim().toLowerCase())) {
			//then value successfully added
			tags.refresh();
		} else {
			//then value is a duplicate or tag can only accept 1 value
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Value Error");
			error.setContentText("Tag Has Value Or Cannot Take More Values!");
			error.show();
			return;
			
		}
    }
    
    /**
     * Method to go back to the PhotoAlbum scene.
     * @param event Event triggered by user pressing album button.
     */

    @FXML
    void albumButton(ActionEvent event) {
    	
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
    
    /**
     * Method to go to the copy scene.
     * @param event Event triggered by user pressing copy button.
     */

    @FXML
    void copyButton(ActionEvent event) {
    	if (Persistance.getUser(LoginController.getUserIndex()).toString().equals("stock")){
    		//we cant change the stock albums
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Stock Error");
			error.setContentText("Use copy for Stock instead of move!");
			error.show();
			return;
    	}
    	//initiates screen to move photo to a different album
    	if (Persistance.getUser(LoginController.getUserIndex()).getAlbums().size()==1) {
    		//then we cant move this to another album
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Move Error");
			error.setContentText("No Other Albums To Copy To!");
			error.show();
			return;
    	}
    	
    	try {
    		
    		PhotoAlbumController.copyOrMove=true;
    		PhotosMoveCopyController.setAlbumOrDisplay(false);
    		
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/PhotosView/PhotosMoveCopy.fxml"));
			AnchorPane rootLayout = (AnchorPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			
			stage.setScene(scene);
			((Node)event.getSource()).getScene().getWindow().hide();
			stage.show();	
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	

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
     * Method to finalize a caption to the photo.
     * @param event Event triggered by user pressing finalize button.
     */

    @FXML
    void finalizeButton(ActionEvent event) {
    	
    	//Photo[] temp;
		//Photo photo;		
			
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		Album opened = currentUser.getAlbum(UserController.getOpenAlbumIndex());
		Photo displayed = opened.getPhoto(PhotoAlbumController.getOpenPhotoIndex());
		
		if (caption.getText().trim().isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Caption Error");
			error.setContentText("Please Fill Out The Caption!");
			error.show();
			return;
		} else {
			displayed.setCaption(caption.getText().trim());
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
     * Method to go to the move scene.
     * @param event Event triggered by user pressing move button.
     */

    @FXML
    void moveButton(ActionEvent event) {
    	if (Persistance.getUser(LoginController.getUserIndex()).toString().equals("stock")){
    		//we cant change the stock albums
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Stock Error");
			error.setContentText("Use copy for Stock instead of move!");
			error.show();
			return;
    	}
    	//initiates screen to move photo to a different album
    	if (Persistance.getUser(LoginController.getUserIndex()).getAlbums().size()==1) {
    		//then we cant move this to another album
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Move Error");
			error.setContentText("No Other Albums To Move To!");
			error.show();
			return;
    	}
    	
    	try {
    		
    		PhotoAlbumController.copyOrMove=false;
    		PhotosMoveCopyController.setAlbumOrDisplay(false);
    		
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/PhotosView/PhotosMoveCopy.fxml"));
			AnchorPane rootLayout = (AnchorPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			
			stage.setScene(scene);
			((Node)event.getSource()).getScene().getWindow().hide();
			stage.show();	
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Method to remove a value of a tag to the photo.
     * @param event Event triggered by user pressing remove button.
     */

    @FXML
    void removeButton(ActionEvent event) {
    		
		
		if (tagList.isEmpty()) {
			//then no tag to remove values from
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Tag Error");
			error.setContentText("Photo's Tag List is Empty!");
			error.show();
			return;
		}
		
		if (tags.getSelectionModel().getSelectedItem().noTags()) {
			//then the selected tag has no values
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Value Error");
			error.setContentText("Tag has No Values to Remove!");
			error.show();
			return;
		}
		
		//now we show dialogue popup to remove a value
		TextInputDialog dialog = new TextInputDialog("Value");
		dialog.setTitle("Value Input Dialog");
		//dialog.setHeaderText("Look, a Text Input Dialog");
		dialog.setContentText("Please enter the value to delete:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		String valueToRemove;
		if (result.isPresent()){
		    //System.out.println("Your name: " + result.get());
		    valueToRemove=result.get();
		} else {
			//then user cancelled
			return;
		}
		
		if (tags.getSelectionModel().getSelectedItem().removeValue(valueToRemove.trim().toLowerCase())) {
			//then value successfully removed
			tags.refresh();
		} else {
			//then value not found
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Value Error");
			error.setContentText("Tag Does not Have This Value!");
			error.show();
			return;
			
		}
			
		}
    
    /**
     * Method to restore the caption to a photo after it has been edited.
     * @param event Event triggered by user pressing restore button.
     */
		

    @FXML
    void restoreButton(ActionEvent event) {
    
		
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		Album opened = currentUser.getAlbum(UserController.getOpenAlbumIndex());
		Photo displayed = opened.getPhoto(PhotoAlbumController.getOpenPhotoIndex());
		caption.setText(displayed.getCaption());
		

    }
    
    /**
     * Method that is automatically called when the user gets to this scene.
     */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
			
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		Album opened = currentUser.getAlbum(UserController.getOpenAlbumIndex());
		Photo photo = opened.getPhoto(PhotoAlbumController.getOpenPhotoIndex());
		
		
		tagList = FXCollections.observableArrayList(photo.getPhotoTags());

		tags.setItems(tagList);
	
		
		Image display = new Image(photo.getLocation());
		images.setImage(display);
		String mon = photo.getPhotoDate().getMonth().toString().substring(0,3);
		//day of month
		String day = ""+photo.getPhotoDate().getDayOfMonth();
		//last two digits of year
		String year = ""+(photo.getPhotoDate().getYear()%100);
		time.setText(mon+"-"+day+"-"+year);
		time.setEditable(false);
		
		caption.setText(photo.getCaption());
		
		tags.setCellFactory(new Callback<ListView<Tag>, ListCell<Tag>>(){
    		@Override
    		 public ListCell<Tag> call(ListView<Tag> p) {
                
                ListCell<Tag> cell= new ListCell<Tag>(){
 
                    @Override
                    protected void updateItem(Tag p, boolean bln) {
                        super.updateItem(p, bln);
                        if (p != null) {
                        	setText(p.toString());
                        	 
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
    	
    	
    	if (!tagList.isEmpty()) {
    		//select 1st item if list is not null
    		tags.getSelectionModel().select(0);
    	}
		
	}
	
	 /**
     * Method to remove a tag to the photo.
     * @param event Event triggered by user pressing removeTag button.
     */
	
	@FXML
	public void removeTag(ActionEvent event) {
		if (tagList.isEmpty()) {
			//no tag to remove
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Delete Error");
			error.setContentText("Tag List is Empty!");
			error.show();
			return;
		}
		
		//removing tag from photo
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		Album opened = currentUser.getAlbum(UserController.getOpenAlbumIndex());
		Photo photo = opened.getPhoto(PhotoAlbumController.getOpenPhotoIndex());
		
		photo.removeTag(tags.getSelectionModel().getSelectedItem().getName());
		tagList.remove(tags.getSelectionModel().getSelectedIndex());
		tags.refresh();
	}

    
 }


