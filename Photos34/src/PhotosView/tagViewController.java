package PhotosView;

import java.io.IOException;

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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;


//this is a helper for dialogues for showing available tags
public class tagViewController {

	@FXML
	private Button addTag;
	
	@FXML
	private Button removeTag;
	
	@FXML
	private ListView<Tag> tagValues;
	
	@FXML
	private RadioButton multipleValuesRadio;
	
	@FXML
	private Button addToPhoto;
	
	@FXML
	private TextField valueField;
	
	private static ObservableList<Tag> usersTags;
	
	public void initialize() {
		//currUser
		User currUser = Persistance.getUser(LoginController.getUserIndex());
		usersTags = FXCollections.observableArrayList(currUser.getAvailableTags());
		
		
		
		//cell factory for tags names and isMultiple
		tagValues.setCellFactory(new Callback<ListView<Tag>, ListCell<Tag>>(){
    		@Override
    		 public ListCell<Tag> call(ListView<Tag> p) {
                
                ListCell<Tag> cell= new ListCell<Tag>(){
 
                    @Override
                    protected void updateItem(Tag p, boolean bln) {
                        super.updateItem(p, bln);
                        if (p == null) {
                        	setText(null);
                        	
                        	 
                        } else {
                        	String name = p.getName();
                        	String values;
                        	if (p.isMultipleValues()) {
                        		values="can take on multiple values.";
                        	} else {
                        		values="cannot take on multiple values.";
                        	}
                        	String toSet = name+" : "+values;
                        	setText(toSet);
                        }
                        
                        	
                        	
                        
                    }
 
                };
                
               
                 
                return cell;
            }
    	});
		tagValues.setItems(usersTags);
		//tagValues.refresh();
		
		
		
		if (usersTags.size()!=0) {
			tagValues.getSelectionModel().select(0);
		}
	}
	
	@FXML
	void addTag(ActionEvent event) {
		//adds a tag to users possible list of tags
		if (valueField.getText().trim().isEmpty() || valueField.getText().trim().toLowerCase().equals("tagname")){
			//show error for user to enter a valid name
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Name Error");
			error.setContentText("Please enter a valid name!");
			error.show();
			return;
		}
		
		User currUser = Persistance.getUser(LoginController.getUserIndex());
		if (currUser.addAvailableTag(valueField.getText().trim().toLowerCase(), multipleValuesRadio.isSelected())) {
			//then the tag was added successfully
			usersTags.add(new Tag(valueField.getText().trim().toLowerCase(), multipleValuesRadio.isSelected()));
			tagValues.refresh();
			tagValues.getSelectionModel().select(usersTags.size()-1);
		} else {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Name Error");
			error.setContentText("TagName is Already in the List!");
			error.show();
			return;
		}
		
	}
	
	@FXML
	void removeTag(ActionEvent event) {
		if (usersTags.isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setContentText("List is Empty!");
			error.show();
			return;
		} else {
			
			User currUser = Persistance.getUser(LoginController.getUserIndex());
			currUser.getAvailableTags().remove(tagValues.getSelectionModel().getSelectedIndex());
			usersTags.remove(tagValues.getSelectionModel().getSelectedIndex());
			tagValues.refresh();
		}
	}
	
	@FXML
	void addTagToPhoto(ActionEvent event) {
		//creating a copy of the selected tag with same name and isMultiple value and adding it to the photo
		if (usersTags.isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setContentText("List of Possible Tags is Empty!");
			error.show();
			return;
		}
		
		User currUser = Persistance.getUser(LoginController.getUserIndex());
		Album opened = currUser.getAlbum(UserController.getOpenAlbumIndex());
		Photo displayed = opened.getPhoto(PhotoAlbumController.getOpenPhotoIndex());
		String tagName = tagValues.getSelectionModel().getSelectedItem().getName();
		boolean isMultiple = tagValues.getSelectionModel().getSelectedItem().isMultipleValues();
		if (displayed.addTag(tagName, isMultiple)) {
			//then we added the tag successfully
			Alert error = new Alert(AlertType.INFORMATION);
			error.setTitle("Success!");
			error.setContentText("Tag Added Successfully to Photo! Go back to view it and add values!");
			error.show();
			return;
		} else {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setContentText("Tag is Already in the Photo!");
			error.show();
			return;
		}
	}

}
