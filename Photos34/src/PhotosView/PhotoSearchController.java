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
    private Button Exit;

    @FXML
    private Button Logout;

    @FXML
    private Button BackAlbums;

    @FXML
    private RadioButton DateRange;

    @FXML
    private TextField StartDate;

    @FXML
    private TextField EndDate;

    @FXML
    private RadioButton TagAndValue;

    @FXML
    private TextField Tag1;

    @FXML
    private TextField Value1;

    @FXML
    private TextField Tag2;

    @FXML
    private RadioButton And;

    @FXML
    private TextField Value2;

    @FXML
    private Button Search;

    @FXML
    private ListView<?> SearchResults;

    @FXML
    private TextField AlbumName;

    @FXML
    private Button CreateAlbum;

    @FXML
    private RadioButton Or;

    @FXML
    void enterButtonClicked(ActionEvent event) {

    }

}
