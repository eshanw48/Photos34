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
import app.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SlideshowController implements Initializable {

    @FXML
    private ImageView images;

    @FXML
    private Button exit;

    @FXML
    private Button previous;

    @FXML
    private Button next;
    
    private static Photo[] slideshow;
    
    private static int currentPhoto = 0;

    @FXML
    void exitButton(ActionEvent event) {
    	
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
    void nextButton(ActionEvent event) {
    	
    	Photo photo;
		
		if (currentPhoto == slideshow.length - 1)
		{
			currentPhoto = 0;
		} else {
			currentPhoto ++;
		}
		
		photo = slideshow[currentPhoto];
	
		Image display = new Image(photo.getLocation());
			images.setImage(display);
		

    }

    @FXML
    void previousButton(ActionEvent event) {
    	Photo photo;
		
    	
    	if (currentPhoto <= 0)
		{
			currentPhoto = slideshow.length - 1;
		} else {
			currentPhoto --;
		}
		
		photo = slideshow[currentPhoto];
	
		
			Image display = new Image(photo.getLocation());
			images.setImage(display);
		


    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		Photo photo;		
		
		currentPhoto = 0;
		
		User currentUser = Persistance.getUser(LoginController.getUserIndex());
		
		Iterator<Photo> photoIter = currentUser.getAlbum(UserController.getOpenAlbumIndex()).photoIterator();
		
		List<Photo> photoList = new ArrayList<>();
		
		while(photoIter.hasNext())
		{
			photoList.add(photoIter.next());
		}
		
		slideshow = photoList.toArray(new Photo[0]);
		
		photo = slideshow[currentPhoto];
	
		
			Image display = new Image(photo.getLocation());
			images.setImage(display);
		
		
		

	
	}

}