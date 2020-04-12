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
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private RadioButton and;
    
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
    private RadioButton or;

    

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

