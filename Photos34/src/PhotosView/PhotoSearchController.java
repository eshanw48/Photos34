package PhotosView;

import java.io.IOException;

import app.Photo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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

    }

    @FXML
    void createAlbumButton(ActionEvent event) {

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

    }

   

}

