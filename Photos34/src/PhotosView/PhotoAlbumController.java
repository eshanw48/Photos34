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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    
    @FXML
    private Button back;
    
    
    //observeable list to monitor changes in our photo list for the album
    private static ObservableList<Photo> photoList;
    
  //index of the photo that is chosen for displaying
    public static int photoIndex;

    //boolean that is true for initiated copy and false for initiated move
    public static boolean copyOrMove;
    
    
    public void initialize() {
    	//getting the album that was opened
    	int albumIndex = UserController.getOpenAlbumIndex();
    	int userIndex = LoginController.getUserIndex();
    	Album opened = Persistance.getUser(userIndex).getAlbum(albumIndex);
    	//setting up observable list 
    	photoList = FXCollections.observableArrayList(opened.getPhotos());
    	
    	photos.setItems(photoList);
    	
    	
    	
    	//basically I want the cells in our listview to be set up as little image boxes with thumbnails followed by the caption
    	photos.setCellFactory(new Callback<ListView<Photo>,ListCell<Photo>>(){
    		@Override
    		 public ListCell<Photo> call(ListView<Photo> p) {
                
                ListCell<Photo> cell= new ListCell<Photo>(){
 
                    @Override
                    protected void updateItem(Photo p, boolean bln) {
                        super.updateItem(p, bln);
                        if (p != null) {
                        	Image thumbnail = new Image(p.getLocation());
                        	ImageView thumb = new ImageView(thumbnail);
                        	thumb.setFitHeight(100);
                        	thumb.setFitWidth(100);
                        	setGraphic(thumb);
                            setText(p.getCaption());
                        }
                        else if (p == null)
                        {
                        	setGraphic(null);
                        	setText(null);
                        }
                    }
 
                };
                 
                return cell;
            }
    	});
    	
    	//setting up listener for our listview
    	photos.getSelectionModel().selectedItemProperty().addListener( 
    			(obs,oldVal,newVal) -> showPhoto()
    			);
    	
    	if (!photoList.isEmpty()) {
    		//select 1st item if list is not null
    		photos.getSelectionModel().select(0);
    	}
    	
    	//we want to just initialize the listview and the name of the album
    	albumName.setText("Album: "+opened.getAlbumName());
    }

    @FXML
    void addButton(ActionEvent event) {
    	if (Persistance.getUser(LoginController.getUserIndex()).toString().equals("stock")){
    		//we cant change the stock albums
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Stock Error");
			error.setContentText("Cannot Alter Stock Album!");
			error.show();
			return;
    	}
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
    			//then add is successful so we should also add to the observable list
    			photoList.add(toAdd);
    			photos.refresh();
    		} else {
    			//then we already have this image in the album
    			Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Rename Error");
				error.setContentText("Image Already Exists In This Album!");
				error.show();
				return;
    		}
    		
    	}
    	
    }

    @FXML
    void copyButton(ActionEvent event) {
    	if (Persistance.getUser(LoginController.getUserIndex()).getAlbums().size()==1) {
    		//then we cant move this to another album
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Move Error");
			error.setContentText("No Other Albums To Move To!");
			error.show();
			return;
    	}
    	try {
    		if (photoList.isEmpty()) {
    			//then we cant possibly move anything 
    			Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Rename Error");
				error.setContentText("Photo List is Empty!");
				error.show();
				return;
    		}
    		photoIndex = photos.getSelectionModel().getSelectedIndex();
    		copyOrMove=true;
    		PhotosMoveCopyController.setAlbumOrDisplay(true);
    		
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/PhotosView/PhotosMoveCopy.fxml"));
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
    void displayButton(ActionEvent event) {
    	
    	//initiates transition to view screen with selected photo
    	
    	
		if (photoList.isEmpty()) {
			//then we cant possibly show a photo
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Display Error");
			error.setContentText("Photo List is Empty!");
			error.show();
			return;
		}
		
		photoIndex=photos.getSelectionModel().getSelectedIndex();
		
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

    @FXML
    void exitButton(ActionEvent event) throws IOException {
    	
    	Persistance.writeUser();
    	
    	Platform.exit();
    	System.exit(0);

    }

    @FXML
    void finalizeButton(ActionEvent event) {
    	//change the caption to the users choice
    	if (photoList.isEmpty()) {
    		//then we dont have a photo selected
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Caption Error");
			error.setContentText("Photo List is Empty!");
			error.show();
			return;
    	}
    	Photo toChange = photos.getSelectionModel().getSelectedItem();
    	toChange.setCaption(caption.getText().trim());
    	photos.refresh();
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
    		if (photoList.isEmpty()) {
    			//then we cant possibly move anything 
    			Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Rename Error");
				error.setContentText("Photo List is Empty!");
				error.show();
				return;
    		}
    		photoIndex = photos.getSelectionModel().getSelectedIndex();
    		copyOrMove=false;
    		PhotosMoveCopyController.setAlbumOrDisplay(true);
    		
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/PhotosView/PhotosMoveCopy.fxml"));
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
    void removeButton(ActionEvent event) {
    	if (Persistance.getUser(LoginController.getUserIndex()).toString().equals("stock")){
    		//we cant change the stock albums
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Stock Error");
			error.setContentText("Cannot Alter Stock Album!");
			error.show();
			return;
    	}
    	//removes photo from the album
    	if (photoList.isEmpty()) {
    		//then we cannot remove from empty list
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Remove Error");
			error.setContentText("Cannot Remove From Empty List!");
			error.show();
			return;
    	}
    	
    	Photo toRemove = photos.getSelectionModel().getSelectedItem();
    	User forRemoval = Persistance.getUser(LoginController.getUserIndex());
    	Album removeFrom =forRemoval.getAlbum(UserController.getOpenAlbumIndex());
    	if (removeFrom.deletePhoto(toRemove)) {
    		//then the photo was removed successfully
    		//updating changes in listview
    		photoList.remove(photos.getSelectionModel().getSelectedIndex());
    		photos.refresh();
    	} else {
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Remove Error");
			error.setContentText("There was an error with deletion!");
			error.show();
			return;
    	}
    	
    }

    @FXML
    void restoreButton(ActionEvent event) {
    	//this is just to reset the caption in case the user wants to cancel their edit or makes changes by mistake
    	if (photoList.isEmpty()) {
    		caption.setText("");
    	}
    	caption.setText(photos.getSelectionModel().getSelectedItem().getCaption());
    }

    @FXML
    void viewButton(ActionEvent event) {
    	

    	//initiates transition to view screen with selected photo
    	
    	
		if (photoList.isEmpty()) {
			//then we cant possibly show a photo
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Display Error");
			error.setContentText("Photo List is Empty!");
			error.show();
			return;
		}
		
		photoIndex=photos.getSelectionModel().getSelectedIndex();
		
	try {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/PhotosView/SlideShow.fxml"));
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
    public void backButton(ActionEvent event) {
    	//just go back to the user page
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
    
    private void showPhoto() {
    	if (photoList.isEmpty()) {
    		caption.setText("");
    	} else {
    		//getting the selected photo
    		Photo selected = photos.getSelectionModel().getSelectedItem();
    		//updating caption
    		caption.setText(selected.getCaption());
    	}
    }
    
    public static int getOpenPhotoIndex() {
    	return photoIndex;
    }
    
    public static boolean getCopyOrMove() {
    	return copyOrMove;
    }

}
