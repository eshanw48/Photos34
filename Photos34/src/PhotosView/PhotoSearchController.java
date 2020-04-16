package PhotosView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * UI controller in which the user can search for a photo based on filters.
 * @author Eshan Wadhwa and Vishal Patel.
 *
 */

public class PhotoSearchController {
	
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
	* Button to go back to the albums
	*/

    @FXML
    private Button backAlbums;
    
    /**
	* RadioButton to select date range
	*/

    @FXML
    private RadioButton dateRange;
    
    /**
	* Textfield to enter start date
	*/

    @FXML
    private TextField StartDate;
    
    /**
	* Textfield to enter end date
	*/

    @FXML
    private TextField EndDate;
    
    /**
   	* RadioButton to select tag and value
   	*/

    @FXML
    private RadioButton tagAndValue;
    
    /**
   	* Textfield to enter tag1
   	*/


    @FXML
    private TextField Tag1;
    
    /**
   	* Textfield to enter value 1
   	*/


    @FXML
    private TextField Value1;
    
    /**
   	* ToggleButton for and option
   	*/


    @FXML
    private ToggleButton and;
    
    /**
   	* Textfield to enter tag2
   	*/

    
    @FXML
    private TextField Tag2;
    
    /**
   	* Textfield to enter value2
   	*/


    @FXML
    private TextField Value2;
    
    /**
   	* Button to search
   	*/


    @FXML
    private Button search;
    
    /**
   	* ListView of the search results
   	*/


    @FXML
    private ListView<Photo> searchResults;
    
    /**
   	* Textfield to enter album name
   	*/


    @FXML
    private TextField AlbumName;
    
    /**
   	* Button to create album
   	*/


    @FXML
    private Button createAlbum;
    
    /**
   	* ToggleButton for or option
   	*/


    @FXML
    private ToggleButton or;
    
    /**
   	* ObservableList of the results
   	*/

    
    private static ObservableList<Photo> results;
    
    /**
     * Method that is automatically called when the user gets to this stage.
     */

    public void initialize() {
    	//we have to initialize the radio buttons to be in the same group
    	//togglegroup for search methods
    	ToggleGroup searches = new ToggleGroup();
    	dateRange.setToggleGroup(searches);
    	tagAndValue.setToggleGroup(searches);
    	//another  togglegroup for and/or
    	ToggleGroup orAnd = new ToggleGroup();
    	or.setToggleGroup(orAnd);
    	and.setToggleGroup(orAnd);
    	
    	searchResults.setItems(results);
    	//setting up ListView cell factory
    	searchResults.setCellFactory(new Callback<ListView<Photo>,ListCell<Photo>>(){
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
    }
    
    /**
     * Method to go back to the albums scene.
     * @param event Event triggered by user pressing backAlbums button.
     */
    

    @FXML
    void backAlbumsButton(ActionEvent event) {
    	//go back to users album list
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
    
    /**
     * Method create an album based on the results.
     * @param event Event triggered by user pressing createAlbum button.
     */

    @FXML
    void createAlbumButton(ActionEvent event) {
    	User currUser = Persistance.getUser(LoginController.getUserIndex());
    	if (currUser.toString().equals("stock")) {
    			//we cant alter stock user
				Alert error = new Alert(AlertType.ERROR);
    			error.setTitle("Stock Error");
    			error.setContentText("Altering Stock Album Is Illegal!");
    			error.show();
    			return;
			
    	}
    	if (results==null) {
    		//we cant make an album out of empty search results
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Creation Error");
			error.setContentText("Search Results Are Empty!");
			error.show();
			return;
    	}  else if (results.isEmpty()) {
    		//we cant make an album out of empty search results
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Creation Error");
			error.setContentText("Search Results Are Empty!");
			error.show();
			return;
    	} else if (AlbumName.getText().trim().isEmpty()) {
    		//then the user needs to try again with a valid name
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Name Error");
			error.setContentText("Please Enter A Valid Name!");
			error.show();
			return;
    	}
    	Album toMake = new Album(AlbumName.getText().trim());
    	
    	//seeing if album is a duplicate
    	if (currUser.addAlbum(toMake)) {
    		//then we can add all the photos in the observable list
    		for (Photo p:results) {
    			toMake.addPhoto(p);
    		}
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Creation Status");
    		alert.setHeaderText("Creating Album From Search Results:");
    		alert.setContentText("Album Successfully Created From Search Results!");

    		alert.show();
    	} else {
    		//then the album name is a duplicate and the user needs to change the name
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Name Error");
			error.setContentText("The Album Name Already Exists!");
			error.show();
			return;
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
     * Method to search for the photos.
     * @param event Event triggered by user pressing search button.
     */

  
    @FXML
    void searchButton(ActionEvent event) {
    	//needs to populate the observable list and make results show up on the listview based on search type (i.e date vs tag value)
    	User currUser = Persistance.getUser(LoginController.getUserIndex());
    	if (dateRange.isSelected()) {
    		//now we parse the first date and the second date
    		if (StartDate.getText().trim().isEmpty() || EndDate.getText().trim().isEmpty()) {
    			//then we should tell the user to please fill in both dates
    			Alert error = new Alert(AlertType.ERROR);
    			error.setTitle("Search Error");
    			error.setContentText("Please Fill In Both Dates");
    			error.show();
    			return;
    		}
    		try {
    			DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yy");
    			LocalDate start = LocalDate.parse(StartDate.getText().trim(),format);
    			LocalDate end = LocalDate.parse(EndDate.getText().trim(),format);
    			List<Photo> resulting = currUser.searchDate(start.atStartOfDay(), end.atStartOfDay());
    			results = FXCollections.observableArrayList(resulting);
    			searchResults.setItems(results);
    			searchResults.refresh();
    			return;
    		} catch (DateTimeParseException e) {
    			//parse throws exception if date is not formatted correctly
    			Alert error = new Alert(AlertType.ERROR);
    			error.setTitle("Search Error");
    			error.setContentText("Please format like mm/dd/yy!");
    			error.show();
    			return;
    			
    		}
    	} else if (tagAndValue.isSelected()) {
    		//different cases if both are filled or if only one is filled
    		if (Tag1.getText().trim().isEmpty()) {
    			//need tag to not be empty
    			Alert error = new Alert(AlertType.ERROR);
    			error.setTitle("Search Error");
    			error.setContentText("Please Fill In A Tag!");
    			error.show();
    			return;
    		} 
    		if (Value1.getText().trim().isEmpty()) {
    			//need value to not be empty
    			Alert error = new Alert(AlertType.ERROR);
    			error.setTitle("Search Error");
    			error.setContentText("Please Fill In A Value!");
    			error.show();
    			return;
    		}
    		if (Tag2.getText().trim().isEmpty() || Tag2.getText().trim().equals("Tag 2")) {
    			List<Photo> resulting = currUser.searchTag(Tag1.getText(), Value1.getText());
    			results=FXCollections.observableArrayList(resulting);
    			searchResults.setItems(results);
    			searchResults.refresh();
    			return;
    			
    			
    		}
    		if (Value2.getText().trim().isEmpty()) {
    			Alert error = new Alert(AlertType.ERROR);
    			error.setTitle("Search Error");
    			error.setContentText("Please Fill In Second Value!");
    			error.show();
    			return;
    		}
    		//now everything is filled out
    		if (or.isSelected()) {
    			try {
    				List<Photo> resulting = currUser.searchTag(Tag1.getText(), Value1.getText(),Tag2.getText(),Value2.getText(),true);
    				results = FXCollections.observableArrayList(resulting);
    				searchResults.setItems(results);
    				searchResults.refresh();
    			} catch (Exception e) {
    				//then the tag doesnt support multiple values
    				Alert error = new Alert(AlertType.ERROR);
        			error.setTitle("Search Error");
        			error.setContentText("Tag Does Not Support Multiple Values!");
        			error.show();
        			return;
    			}
    			
    			
    		} else if (and.isSelected()) {
    			try {
    				List<Photo> resulting = currUser.searchTag(Tag1.getText(), Value1.getText(),Tag2.getText(),Value2.getText(),false);
    				results = FXCollections.observableArrayList(resulting);
    				searchResults.setItems(results);
    				searchResults.refresh();
    			} catch (Exception e) {
    				//then the tag doesnt support multiple values
    				Alert error = new Alert(AlertType.ERROR);
        			error.setTitle("Search Error");
        			error.setContentText("Tag Does Not Support Multiple Values!");
        			error.show();
        			return;
    			}
    		} else {
    			Alert error = new Alert(AlertType.ERROR);
    			error.setTitle("Search Error");
    			error.setContentText("Please Select A Logical Operator!");
    			error.show();
    			return;
    		}
    	} else {
    		//then we need to tell the user to fill in the search!
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Search Error");
			error.setContentText("Please Choose a Search Method!");
			error.show();
			return;
    	}
    }
    
    

   

}

