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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDateTime;

public class UserController {

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
    private TableColumn<Album, Integer> photoColumn;

    @FXML
    private TableColumn<Album, LocalDateTime> earlyColumn;

    @FXML
    private TableColumn<Album, LocalDateTime> lateColumn;
    
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
		
		User currUser = Persistance.getUser(userIndex);
		if (currUser.toString().equals("stock")) {
			//we cant add albums to our default stock user
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("Cant add albums to default stock user. Please create your own user!");
			error.show();
			
			return;
		}
		Album toAdd = new Album(albumName);
		if (!currUser.addAlbum(toAdd)) {
			//then our user entered a duplicate
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("Existing Album");
			error.show();
			
			return;
		}
		
		//if we got here, then the album was successfully added to the user object
		//adding to observeable list
		albumList.add(toAdd);
		
		
		displayAlbums.getSelectionModel().select(albumList.size()-1);  // selects the last album to be inserted into list
		

    }

    @FXML
    void deleteButton(ActionEvent event) {
    	
    	if(albumList.isEmpty()) {
			
			return;
		}
    	
    	if (Persistance.getUser(LoginController.getUserIndex()).toString().equals("stock")) {
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("We cannot delete albums from the stock user!");
			error.show();
			
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
    	//the selected album is the album that the user wishes to rename
    	if (albumList.isEmpty()) {
    		//then show error dialog
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("No Album In The List to Rename!");
			error.show();
			
			return;
    	} else if(Persistance.getUser(LoginController.getUserIndex()).toString().equals("stock")) {
    		//then we are in stock user and trying to rename stock album
    		//this should not be allowed
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Input Error");
			error.setContentText("Cannot rename Stock Default Album!");
			error.show();
			
			return;
    	}else {
    	
    		//we now rename
    		String newName = AlbumName.getText().trim();
    		
    		for (int i=0;i<albumList.size();i++) {
    			if (i!=displayAlbums.getSelectionModel().getSelectedIndex()) {
    				//then we are checking other albums
    				Album compare = albumList.get(i);
    				if (compare.getAlbumName().equals(newName)) {
    					//then we have a naming conflict and we should show dialogue error
    					//then our user entered a duplicate
    					Alert error = new Alert(AlertType.ERROR);
    					error.setTitle("Rename Error");
    					error.setContentText("Cannot Rename This Album With The Same Name As Another Album!");
    					error.show();
    					return;
    				}
    			}
    			
    		}
    		
    		//then we have no renaming conflict and we can change the name
    		//changing name in observeable list
    		albumList.get(displayAlbums.getSelectionModel().getSelectedIndex()).setAlbumName(newName);
    		//refreshing tableview to show update
    		displayAlbums.refresh();
    		
    	}
    	

    }

    @FXML
    void searchButton(ActionEvent event) {
    	//should go to search screen now
    	try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/PhotosView/PhotoSearch.fxml"));
			AnchorPane rootLayout = (AnchorPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			
			stage.setScene(scene);
			((Node)event.getSource()).getScene().getWindow().hide();
			stage.show();	
			
		} catch (IOException m) {
			m.printStackTrace();
		}

    }


	public void initialize() {
		
		
		

		
		User currUser = Persistance.getUser(LoginController.getUserIndex());  // holds the current user
		
		albumList = FXCollections.observableArrayList(currUser.getAlbums());
		
	
		
		labelUser.setText("Welcome " + currUser + " !");
		
		/*
		Iterator<Album> albumInter = currUser.albumIterator();
		
		while(albumInter.hasNext()) {  // Load all albums from current user
			
			albumList.add(albumInter.next());
						
		}			
		*/
				
		displayAlbums.setItems(albumList);
		
		displayAlbums.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);	
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("albumName"));
		
		
		photoColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPhotos"));
	
		//populating column with LocalDateTime instances
		earlyColumn.setCellValueFactory(new PropertyValueFactory<>("beginDate"));  
		
		//displaying LocalDateTime instances in human readable format
		earlyColumn.setCellFactory(new Callback<TableColumn<Album,LocalDateTime>,TableCell<Album,LocalDateTime>>(){
			@Override
			public TableCell<Album,LocalDateTime> call(TableColumn<Album,LocalDateTime> t){
				TableCell<Album,LocalDateTime> cell = new TableCell<Album,LocalDateTime>(){
					
					@Override
					protected void updateItem(LocalDateTime a,boolean bln) {
						super.updateItem(a,bln);
						if (a==null) {
							setText(null);
						} else {
							String mon = a.getMonth().toString().substring(0,3);
							//day of month
							String day = ""+a.getDayOfMonth();
							//last two digits of year
							String year = ""+(a.getYear()%100);
							setText(mon+"-"+day+"-"+year);
						}
					}
				};
				return cell;
			}
		});
		
		
		//populating column values with LocalDateTime instances
		lateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));  
		
		//displaying LocalDateTime instances in human readable format
		lateColumn.setCellFactory(new Callback<TableColumn<Album,LocalDateTime>,TableCell<Album,LocalDateTime>>(){
			@Override
			public TableCell<Album,LocalDateTime> call(TableColumn<Album,LocalDateTime> t){
				TableCell<Album,LocalDateTime> cell = new TableCell<Album,LocalDateTime>(){
					
					@Override
					protected void updateItem(LocalDateTime a,boolean bln) {
						super.updateItem(a,bln);
						if (a==null) {
							setText(null);
						} else {
							String mon = a.getMonth().toString().substring(0,3);
							//day of month
							String day = ""+a.getDayOfMonth();
							//last two digits of year
							String year = ""+(a.getYear()%100);
							setText(mon+"-"+day+"-"+year);
						}
					}
				};
				return cell;
			}
		});
		
		//setting up listener for our tableView
		displayAlbums
		.getSelectionModel()
		.selectedItemProperty()
		.addListener( 
				(obs,oldVal,newVal)->
				showAlbum());
		
		
		
		
		if(!albumList.isEmpty()) {  // if list is not empty; select first item
			
			displayAlbums.getSelectionModel().select(0);
		}
		
		

}
	
	public static int getOpenAlbumIndex() {
		
		return openAlbumIndex;
	}
	
	private void showAlbum() {
		if (albumList.isEmpty()) {
			AlbumName.setText("");
		} else {
			//getting selected album
			Album selected =displayAlbums.getSelectionModel().getSelectedItem();
			AlbumName.setText(selected.getAlbumName());
		}
		
	}
}


