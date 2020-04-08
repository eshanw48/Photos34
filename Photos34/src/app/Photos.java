package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Photos extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
    	

		try {
			
			Persistance.readUser();
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/PhotosView/Login.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			
			
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		/*
    	Persistance.readUser();
    	
        Parent root = FXMLLoader.load(getClass().getResource("/PhotosView/Login.fxml"));
        primaryStage.setTitle("Photos By: Vishal Patel and Eshan Wadhwa");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        
        
       */
    }


    public static void main(String[] args) {
        launch(args);
    }
}
