package PhotosView;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserController {
	
	//we have to display our albums in the specific format with album name, early date, end date, num photos
	//we can use add album method in user etc when add

    @FXML
    private Button create;

    @FXML
    private TextField AlbumName;

    @FXML
    private Button exit;

    @FXML
    private Button logout;

    @FXML
    private Button rename;

    @FXML
    private Button delete;

    @FXML
    private Button search;

    @FXML
    private TableView<?> displayAlbums;

    @FXML
    private Button open;

    @FXML
    void createButton(ActionEvent event) {

    }

    @FXML
    void deleteButton(ActionEvent event) {

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
    void openButton(ActionEvent event) {

    }

    @FXML
    void renameButton(ActionEvent event) {

    }

    @FXML
    void searchButton(ActionEvent event) {

    }

}
