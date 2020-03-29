package PhotosView;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PhotoDisplayController {

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
    private ListView<?> photo;

    @FXML
    private Button album;

    @FXML
    private Button copy;

    @FXML
    private Button move;

    @FXML
    private TextField time;

    @FXML
    private ListView<?> photo1;

    @FXML
    private Button key;

    @FXML
    private Button value;

    @FXML
    private Button add;

    @FXML
    private Button remove;

    @FXML
    void addButton(ActionEvent event) {

    }

    @FXML
    void albumButton(ActionEvent event) {

    }

    @FXML
    void copyButton(ActionEvent event) {

    }

    @FXML
    void exitButton(ActionEvent event) {
    	
    	Platform.exit();
    	System.exit(0);

    }

    @FXML
    void finalizeButton(ActionEvent event) {

    }

    @FXML
    void keyButton(ActionEvent event) {

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

    }

    @FXML
    void restoreButton(ActionEvent event) {

    }

    @FXML
    void valueButton(ActionEvent event) {

    }

}