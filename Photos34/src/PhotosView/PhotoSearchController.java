package PhotosView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Label;
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

public class PhotoSearchController {

    @FXML
    private Button exit;

    @FXML
    private Button logout;

    @FXML
    private Button backAlbums;

    @FXML
    private RadioButton dateRange;

    @FXML
    private TextField StartDate;

    @FXML
    private TextField EndDate;

    @FXML
    private RadioButton tagAndValue;

    @FXML
    private TextField Tag1;

    @FXML
    private TextField Value1;

    @FXML
    private ToggleButton and;
    
    @FXML
    private TextField Tag2;

    @FXML
    private TextField Value2;

    @FXML
    private Button search;

    @FXML
    private ListView<Photo> searchResults;

    @FXML
    private TextField AlbumName;

    @FXML
    private Button createAlbum;

    @FXML
    private ToggleButton or;
    
    private static ObservableList<Photo> results;

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
    	if (results.isEmpty()) {
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
    			searchResults.refresh();
    			return;
    		} catch (Exception e) {
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
    		if (Tag2.getText().trim().isEmpty()) {
    			List<Photo> resulting = currUser.searchTag(Tag1.getText(), Value1.getText());
    			results=FXCollections.observableArrayList(resulting);
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

