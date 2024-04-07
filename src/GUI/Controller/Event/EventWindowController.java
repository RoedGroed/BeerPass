package GUI.Controller.Event;

import BE.Event;
import GUI.Controller.BaseController;
import GUI.Model.EventModel;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;


public class EventWindowController extends BaseController implements Initializable {
    EventModel eventModel = new EventModel();
    //private Model model;
    @FXML
    private VBox EventsVBox;
    @FXML
    Button btnNewEvent;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        //model = Model.getInstance();
        super.initialize(location, resources);
        try {
            String userRole = model.getCurrentUser().getRole();
            //System.out.println(userRole);
            List<Event> events;

            if ("Event coordinator".equals(userRole)){
                int userID = model.getCurrentUser().getUserID();
                events = eventModel.getEventsForEventCo(userID);
            } else {
                events = eventModel.getAllEvents();
            }
            displayEvents(events);
        } catch (SQLException | IOException e) {
            System.out.println(e);
        }
    }

    private void displayEvents(List<Event> events) {
        int imagesPerRow = 3;
        HBox hbox = null;

        for (int i = 0; i < events.size(); i++) {
            if (i % imagesPerRow == 0) {
                hbox = new HBox(10);
                EventsVBox.getChildren().add(hbox);
            }
            Event event = events.get(i);
            ImageView imageView = createImageViewForEvent(event);
            hbox.getChildren().add(imageView);
        }
    }

    private ImageView createImageViewForEvent(Event event) {
        try {
            String imagePath = "/Images/App/" + event.getImagePath();
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            return imageView;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved indlæsning af billede: " + e.getMessage());
        }
    }


    @FXML
    private void onNewEvent(ActionEvent actionEvent) {
        loadFXML("/NewEvent.FXML", model, (Stage) btnNewEvent.getScene().getWindow());
    }



}