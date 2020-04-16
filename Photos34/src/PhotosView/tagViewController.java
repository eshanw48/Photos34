package PhotosView;


import app.Album;
import app.Persistance;
import app.Photo;
import app.Tag;
import app.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;

/**
 * UI controller which helps with tags; gives a dialogue showing available tags.
 * @author Eshan Wadhwa and Vishal Patel.
 *
 */


public class tagViewController {
	
	/**
	 * Button to add tag
	 */

	@FXML
	private Button addTag;
	
	/**
	 * Button to remove tag
	 */
	
	@FXML
	private Button removeTag;
	
	/**
	 * ListView displaying tag values
	 */
	
	@FXML
	private ListView<Tag> tagValues;
	
	/**
	 * RadioButton to determine if a tag can have multiple values
	 */
	
	@FXML
	private RadioButton multipleValuesRadio;
	
	/**
	 * Button to add tag to photo
	 */
	
	@FXML
	private Button addToPhoto;
	
	/**
	 * Textfield to enter value of tag
	 */
	
	@FXML
	private TextField valueField;
	
	/**
	 * ObersvableList of the user tags
	 */
	
	private static ObservableList<Tag> usersTags;
	
    /**
     * Method that is automatically called when the user gets to this stage.
     */
	
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
	
    /**
     * Method to add a tag.
     * @param event Event triggered by user pressing addTag button.
     */
	
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
	
    /**
     * Method to remove a tag.
     * @param event Event triggered by user pressing removeTag button.
     */
	
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
	
    /**
     * Method to add tag to a photo.
     * @param event Event triggered by user pressing addToPhoto button.
     */
	
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
