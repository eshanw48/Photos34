package PhotosView;

import java.io.IOException;

import app.Album;
import app.Persistance;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDateTime;

/**
 * UI controller which manages screen for when the user is logged into our photos app. User sees all the albums they have.
 * @author Eshan Wadhwa and Vishal Patel.
 *
 */

public class UserController {
	
	/**
	 * Button to create an album
	 */

    @FXML
    private Button create;
    
    /**
	 * Label displaying the current user logged in
	 */
    
    @FXML
    private Label labelUser;
    
    /**
	 * Textfield for the album name
	 */
    

    @FXML
    private TextField AlbumName;
    
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
	 * Button to rename the album
	 */

    @FXML
    private Button rename;
    
    /**
	 * Button to delete the album
	 */

    @FXML
    private Button delete;
    
    /**
	 * Button to search for an album
	 */

    @FXML
    private Button search;
    
    /**
	 * TableView displaying all the albums for the user logged in
	 */

    @FXML
    private TableView<Album> displayAlbums;
    
    /**
	 * Button to open an album and see all its photos
	 */

    @FXML
    private Button open;
    
    /**
	 * TableColumn that displays the names of the albums
	 */
    
    @FXML
    private TableColumn<Album, String> nameColumn;
    
    /**
	 * TableColumn that displays the number of photos in the album
	 */

    @FXML
    private TableColumn<Album, Integer> photoColumn;
    
    /**
	 * TableColumn that displays the date of the earliest photo modification
	 */

    @FXML
    private TableColumn<Album, LocalDateTime> earlyColumn;
    
    /**
	 * TableColumn that displays the date of the latest photo modification
	 */

    @FXML
    private TableColumn<Album, LocalDateTime> lateColumn;
    
    /**
	 * ObersvableList of the albums
	 */
    
    private static ObservableList<Album> albumList;
    
    /**
	 * Integer that gives the index of the current open album
	 */
    
    private static int openAlbumIndex = -1;
    
    /**
     * Method to create an album.
     * @param event Event triggered by user pressing create button.
     */

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
    
    /**
     * Method to delete an album.
     * @param event Event triggered by user pressing delete button.
     */

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
    
    /**
     * Method to open an album.
     * @param event Event triggered by user pressing open button.
     */

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
    
    /**
     * Method to rename an album.
     * @param event Event triggered by user pressing rename button.
     */

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
    
    /**
     * Method to search for an album.
     * @param event Event triggered by user pressing search button.
     */

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
    
    /**
     * Method that is automatically called when the user gets to this stage.
     */


	public void initialize() {
		
		
		User currUser = Persistance.getUser(LoginController.getUserIndex());  // holds the current user
		
		albumList = FXCollections.observableArrayList(currUser.getAlbums());
		
	
		
		labelUser.setText("Welcome " + currUser + " !");
		
				
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
	
	/**
     * Method to get the open album index.
     * @return integer open album index.
     */
	
	public static int getOpenAlbumIndex() {
		
		return openAlbumIndex;
	}
	
	/**
     * Method to show album into the TableView.
     */
	
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


