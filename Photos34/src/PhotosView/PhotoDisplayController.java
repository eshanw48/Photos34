package PhotosView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import app.Persistance;
import app.Photo;
import app.Tag;
import app.User;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private Button key;

    @FXML
    private Button value;

    @FXML
    private Button add;

    @FXML
    private Button remove;
    
    private ObservableList<Tag> tagList;

    @FXML
    void addButton(ActionEvent event) {

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Photo photo;
		File file;
	
		
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		
		Iterator<Photo> photoIter = currentUser.getAlbum(UserController.getOpenAlbumIndex()).photoIterator();
		
		List<Photo> photoList = new ArrayList<>();
		
		while(photoIter.hasNext())
		{
			photoList.add(photoIter.next());
		}
		
		
			Image display = new Image("test.png"); //just for the time being replace this
			images.setImage(display);
		
	}

}