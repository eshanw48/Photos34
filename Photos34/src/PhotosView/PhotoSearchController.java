package PhotosView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

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
    private Label Tag2;

    @FXML
    private RadioButton and;

    @FXML
    private TextField Value2;

    @FXML
    private Button search;

    @FXML
    private ListView<?> searchResults;

    @FXML
    private TextField AlbumName;

    @FXML
    private Button createAlbum;

    @FXML
    private RadioButton or;

    @FXML
    void andButton(ActionEvent event) {

    }

    @FXML
    void backAlbumsButton(ActionEvent event) {

    }

    @FXML
    void createAlbumButton(ActionEvent event) {

    }

    @FXML
    void dateRangeButton(ActionEvent event) {

    }

    @FXML
    void exitButton(ActionEvent event) {

    }

    @FXML
    void logoutButton(ActionEvent event) {

    }

    @FXML
    void orButton(ActionEvent event) {

    }

    @FXML
    void searchButton(ActionEvent event) {

    }

    @FXML
    void tagAndValueButton(ActionEvent event) {

    }

}

