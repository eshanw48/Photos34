package PhotosView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PhotoDisplayController implements Initializable {

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
    private ImageView images;

    @FXML
    private Button album;

    @FXML
    private Button copy;

    @FXML
    private Button move;

    @FXML
    private TextField time;

    @FXML
    private ListView<Tag> tags;

    @FXML
    private TextField key;

    @FXML
    private TextField value;

    @FXML
    private Button add;

    @FXML
    private Button remove;
    
    private ObservableList<Tag> tagList;
    
    private static boolean location = true; //ensures user only tags 1 location per photo

    @FXML
    void addButton(ActionEvent event) {
    	
    	Photo[] temp;
		Photo photo;		
			
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		
		Iterator<Photo> photoIter = currentUser.getAlbum(UserController.getOpenAlbumIndex()).photoIterator();
		
		List<Photo> photoList = new ArrayList<>();
			
		
		while(photoIter.hasNext())
		{
			photoList.add(photoIter.next());
		}
		
		temp = photoList.toArray(new Photo[0]);
		
		photo = temp[PhotoAlbumController.photoIndex];
		
		String tag = tagHelper("add");
		
		if (tag != null)
		{
			
			String[] args = tag.split("~");
			
		//	if(photo.addTag(args[0], args[1], false))
			{
			//	photo.setPhotoTags(tagList);
			//	tagList.add(tag); //ADD  TO LISTVIEW 
				//tags.refresh();
				
				
		//	} else {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Same Tag Error");
				error.setContentText("Duplicate tag detected. Tag not added.");
			}
		//	photo.setPhotoTags(tagList);
			tagList.add(photo.getTag(tag));
			tags.setItems(tagList);
			tags.refresh();
		
		
		
		
    }
		tags.refresh();
    }

    

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

    @FXML
    void copyButton(ActionEvent event) {
    	
    	

    }

    @FXML
    void exitButton(ActionEvent event) throws IOException {
    	
    	Persistance.writeUser();
    	
    	Platform.exit();
    	System.exit(0);

    }

    @FXML
    void finalizeButton(ActionEvent event) {
    	
    	Photo[] temp;
		Photo photo;		
			
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		
		Iterator<Photo> photoIter = currentUser.getAlbum(UserController.getOpenAlbumIndex()).photoIterator();
		
		List<Photo> photoList = new ArrayList<>();
		
		while(photoIter.hasNext())
		{
			photoList.add(photoIter.next());
		}
		
		temp = photoList.toArray(new Photo[0]);
		
		photo = temp[PhotoAlbumController.photoIndex];
    	
    	Photo toChange = photo;
    	toChange.setCaption(caption.getText().trim());
    	
    	caption.setText(toChange.getCaption());
    	
    	tags.refresh();

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
    	
    	Photo[] temp;
		Photo photo;		
			
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		
		Iterator<Photo> photoIter = currentUser.getAlbum(UserController.getOpenAlbumIndex()).photoIterator();
		
		List<Photo> photoList = new ArrayList<>();
		
		Alert error = new Alert(AlertType.ERROR);
			
		
		while(photoIter.hasNext())
		{
			photoList.add(photoIter.next());
		}
		
		temp = photoList.toArray(new Photo[0]);
		
		photo = temp[PhotoAlbumController.photoIndex];
		
		String tag = tagHelper("delete");
		
		if (tag != null)
		{
			
			
			String[] args = tag.split("~");
			
			
			

		//	if(photo.removeTag(args[0], args[1]))
		//	{
				photo.removeTag(tag);
				
				
					tags.refresh();
		//	} else {
				error.setTitle("Invalid Tag Error");
				error.setContentText("Entered tag does not exist.");
			}
		
			photo.removeTag(tag);
			tags.refresh();
		}
		

    @FXML
    void restoreButton(ActionEvent event) {
    	
    	//this is just to reset the caption in case the user wants to cancel their edit or makes changes by mistake
    	

    	Photo[] temp;
		Photo photo;		
			
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		
		Iterator<Photo> photoIter = currentUser.getAlbum(UserController.getOpenAlbumIndex()).photoIterator();
		
		List<Photo> photoList = new ArrayList<>();
		
		while(photoIter.hasNext())
		{
			photoList.add(photoIter.next());
		}
		
		temp = photoList.toArray(new Photo[0]);
		
		photo = temp[PhotoAlbumController.photoIndex];
    	
    	caption.setText(photo.getCaption());

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Photo[] temp;
		Photo photo;		
			
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		
		Iterator<Photo> photoIter = currentUser.getAlbum(UserController.getOpenAlbumIndex()).photoIterator();
		
		List<Photo> photoList = new ArrayList<>();
		
		while(photoIter.hasNext())
		{
			photoList.add(photoIter.next());
		}
		
		temp = photoList.toArray(new Photo[0]);
		
		photo = temp[PhotoAlbumController.photoIndex];
		
		tagList = FXCollections.observableArrayList(photo.getPhotoTags());
		tags.setItems(tagList);
	
		
		Image display = new Image(photo.getLocation());
		images.setImage(display);
			
		time.setText(photo.getPhotoDate().toString());
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
                        	setText("something is here");
                        	 
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
    	
    	//setting up listener for our listview
		tags.getSelectionModel().selectedItemProperty().addListener( 
    			(obs,oldVal,newVal) -> showPhoto()
    			);
    	
    	if (!photoList.isEmpty()) {
    		//select 1st item if list is not null
    		tags.getSelectionModel().select(0);
    	}
		
	}
		
	
	
	public static String tagHelper(String operation)
	{
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Tag Editing");
		alert.setHeaderText("Tag Type");
		
		ButtonType personButton = new ButtonType("Person");
		ButtonType locationButton = new ButtonType("Location");
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(personButton, locationButton, cancelButton);
		
		if (operation == "add")
		{
			alert.setContentText("Select the type of tag you want to add.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == personButton)
			{
			    return editDialog("person");
			} else if (result.get() == locationButton) {
				return editDialog("location");
			} else {
			    
			}
		} else if (operation == "delete") {
			alert.setContentText("Select the type of tag you want to delete.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == personButton)
			{
				return editDialog("person");
			} else if (result.get() == locationButton) {
				return editDialog("location");
			} else {
			    
			}
		}
		
		return null;
	
}
	
	public static String editDialog(String keyword)
	{
		TextInputDialog addDialog = new TextInputDialog();
		Optional<String> result;
		Alert error = new Alert(AlertType.ERROR);
		
		if (keyword == "person" || keyword == "location")
		{
			addDialog.setTitle("Tag Editing");
			addDialog.setHeaderText("Add a " + keyword + " Tag");
			addDialog.setContentText("Enter Tag:");
			
			result = addDialog.showAndWait();
			
			if(result.isPresent())
			{
				if(result.get().isEmpty())
				{
					error.setTitle("Tag Input Error");
					error.setContentText("No tag was entered. Please enter in a tag.");
					
					return null;
				}
				
				return keyword + "~" + result.get().trim();
			}
		} 
		
		return null;
		
	}
	

    private void showPhoto() {
    
    	Photo[] temp;
		Photo photo;		
			
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		
		Iterator<Photo> photoIter = currentUser.getAlbum(UserController.getOpenAlbumIndex()).photoIterator();
		
		List<Photo> photoList = new ArrayList<>();
		
		while(photoIter.hasNext())
		{
			photoList.add(photoIter.next());
		}
		
		temp = photoList.toArray(new Photo[0]);
		
		photo = temp[PhotoAlbumController.photoIndex];
		
		caption.setText(photo.getCaption());
		
		tags.setItems(tagList);
    	}
    }


