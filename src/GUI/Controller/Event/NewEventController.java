package GUI.Controller.Event;

import BE.Event;
import GUI.Controller.BaseController;
import GUI.Model.EventModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class NewEventController extends BaseController implements Initializable {

    @FXML
    private TextField tfEventName;
    @FXML
    private TextField tfEventLocation;
    @FXML
    private TextField tfEventTime;
    @FXML
    private TextArea taEventNotes;
    @FXML
    private TextField tfMaxAttendees;
    @FXML
    private Label lblSelectedImage;
    @FXML
    private ImageView imgEventImage;
    @FXML
    private ListView<?> lvCoordinators;
    @FXML
    private ListView<?> lvAllCoordinators;
    @FXML
    private DatePicker dpEventDate;
    private EventModel eventModel;


    @FXML
    private ComboBox<String> cbEventImages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        eventModel = new EventModel();
        loadImageViewer();
        loadImagesIntoComboBox();

    }
    @FXML
    private void onCancel(ActionEvent actionEvent) {
        loadFXML("/EventWindow.FXML",model, (Stage) tfEventName.getScene().getWindow());
    }

    @FXML
    private void onConfirmUser(ActionEvent actionEvent) throws SQLException, IOException {

        Event event = getUserInput();

        eventModel.createEvent(event);

        // Show and Wait
        // Update the database and lists(Observable Lists)
        loadFXML("/EventWindow.FXML",model, (Stage) tfEventName.getScene().getWindow());
    }

    private Event getUserInput(){
        String name = tfEventName.getText();
        String location = tfEventLocation.getText();
        LocalDate timeDate = dpEventDate.getValue();
        String timeStart = tfEventTime.getText();
        String note = taEventNotes.getText(); // set a limit to 255 NVARS
        int ticketLimit = Integer.parseInt(tfMaxAttendees.getText());
        String imagePath = cbEventImages.getValue();

        String time = eventModel.formatTimeToString(timeDate,timeStart);

        return new Event(-1, name, location, time, note, ticketLimit, imagePath);
    }

    private void loadImageViewer(){
        cbEventImages.setOnAction(event -> {
            String selectedImage = cbEventImages.getValue();
            if (selectedImage != null) {

                // TODO: Ændre når vi har nogle event images til den nye file path
                String imagePath = "resources/Images/App/" + selectedImage;
                Image image = new Image(new File(imagePath).toURI().toString());
                imgEventImage.setImage(image);
            }
        });
    }

    private void loadImagesIntoComboBox(){
        // TODO: Ændre når vi har nogle event images til den nye file path
        File folder = new File("resources/Images/App/");

        if(folder.exists() && folder.isDirectory()) {
            File[] imageList = folder.listFiles();
            if (imageList != null) {
                for (File file : imageList) {
                    if (file.isFile()) {
                        cbEventImages.getItems().add(file.getName());
                    }
                }
            }
        }
    }


    /* TODO: When user gets a date, populate the time textfield with local time, so they can see the format.
            Lav en form for verification for time, så det altid er i korrekt format (Se MyTunes)
            -
            Populate(Metode) Combobox med billeder, hvor billedet så giver den rigtige imagepath.
            Skal man kunne add nye billeder igennem GUI'en?
            Gør så combobox bliver updated hvis der bliver added nye billeder i EventImage Mappen?
            ComboBox image picked -> Load Image i ImageViewer.
            -
            Format DatePicker data om til String(hvis den ikke allerede er det) og format den til ikke Amerikansk.
            Skal have en start og slut tid?

     */


}
