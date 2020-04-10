package PhotosView;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import app.Album;
import app.Persistance;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



public class UserController implements Initializable {

    @FXML
    private Button create;
    
    @FXML
    private Label labelUser;
    

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
    private TableView<Album> displayAlbums;

    @FXML
    private Button open;
    
    @FXML
    private TableColumn<Album, String> nameColumn;

    @FXML
    private TableColumn<Album, String> photoColumn;

    @FXML
    private TableColumn<Album, String> earlyColumn;

    @FXML
    private TableColumn<Album, String> lateColumn;
    
    private static ObservableList<Album> albumList;
    
    private static int openAlbumIndex = -1;

    @FXML
    void createButton(ActionEvent event) {
    	
    	int userIndex = LoginController.getUserIndex();
		
		String albumName = AlbumName.getText().trim();  
		
		if(AlbumName.getText().trim().isEmpty()) {
			
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("Must Enter Album Name");
			error.show();
			
			return;
		}
		
		User currUser = Persistance.getUser(LoginController.getUserIndex());
		
		Iterator<Album> albumIter = currUser.albumIterator();
		
		while(albumIter.hasNext()) {
			
			
			if(AlbumName.getText().trim().equals(albumIter.next().getAlbumName())) {
				
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Input Error");
				error.setContentText("Existing Album");
				error.show();
				
				return;
				
			}
			
		}
		
		
		Album album = new Album(albumName);  // create new album instance
				
		//User currUser = Persistance.getUser(userIndex);
	
		currUser.addAlbum(album);
		
		albumList.add(album);  // insert album in observable list
		
		
		
		displayAlbums.getSelectionModel().select(albumList.size()-1);  // selects the last album to be inserted into list


    }

    @FXML
    void deleteButton(ActionEvent event) {
    	
    	if(albumList.isEmpty()) {
			
			return;
		}
		
		Album selectedAlbum = displayAlbums.getSelectionModel().getSelectedItem();  // gets title of song that is selected
		
		int i = 0;
		for(Album a: albumList) {  // find song in observable list
			
			if(selectedAlbum.toString().equals(a.toString())) {  
				
				break;
				
			}
			
			i++;
		}
		
		
		User currUser = Persistance.getUser(LoginController.getUserIndex());
		
		currUser.delAlbum(i);
		
		albumList.remove(i);
		
		if(albumList.size() == i) {  // if last item in list is deleted
			
			displayAlbums.getSelectionModel().select(--i);  // select previous item in list
			
		} else {
			
			displayAlbums.getSelectionModel().select(i);  // select next item in list
			
		}

    }

    @FXML
    void exitButton(ActionEvent event) throws IOException {
    	
    	Persistance.writeUser();	
    	
    	Platform.exit();
    	System.exit(0);


    }

    @FXML
    void logoutButton(ActionEvent event) {
    	
    	albumList = FXCollections.observableArrayList();
    	
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
    	
    	if(albumList.isEmpty()) {
			
			return;
		}
		
		openAlbumIndex = displayAlbums.getSelectionModel().getSelectedIndex();
		
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
    void renameButton(ActionEvent event) {
    	
    	

    }

    @FXML
    void searchButton(ActionEvent event) {
    	
    	

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		albumList = FXCollections.observableArrayList();

		User currUser = Persistance.getUser(LoginController.getUserIndex());  // holds the current user
		
		labelUser.setText("Welcome " + currUser + " !");
		
		
		Iterator<Album> albumInter = currUser.albumIterator();
		
		while(albumInter.hasNext()) {  // Load all albums from current user
			
			albumList.add(albumInter.next());
						
		}			
		
				
		displayAlbums.setItems(albumList);
		
	//	displayAlbums.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);	
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("albumName"));
		
		
		photoColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPhotos"));
	
		
	//	earlyColumn.setCellValueFactory(new PropertyValueFactory<>("early"));  
	
		
	//	lateColumn.setCellValueFactory(new PropertyValueFactory<>("late"));  
		
		
		
		
		if(!albumList.isEmpty()) {  // if list is not empty; select first item
			
			displayAlbums.getSelectionModel().select(0);
		}
		

}
	
	public static int getOpenAlbumIndex() {
		
		return openAlbumIndex;
	}
}


