package GUI.Controller.Event;

import BE.Event;
import GUI.Controller.BaseController;
import GUI.Model.EventModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
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
    private int imageWidth = 300;
    private int imageHeight = 200;
    @FXML
    private ScrollPane scrollPane;


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
                displayEvents(events);
            } else if ("Admin".equals(userRole)){
                events = eventModel.getAllEvents();
                displayEvents(events);
            }
        } catch (SQLException | IOException e) {
            System.out.println(e);
        }
    }

    private void displayEvents(List<Event> events) {
        int imagesPerRow = 3;
        HBox hbox = null;

        for (int i = 0; i < events.size(); i++) {
            if (i % imagesPerRow == 0) {
                hbox = new HBox(40);
                hbox.setAlignment(Pos.CENTER_LEFT);
                EventsVBox.getChildren().add(hbox);
            }
            Event event = events.get(i);

            VBox vBox = new VBox(0);
            vBox.setAlignment(Pos.CENTER);

            ImageView imageView = createImageViewForEvent(event);
            Label label = createLabelForEvent(event);


            vBox.getChildren().addAll(imageView, label);
            hbox.getChildren().add(vBox);
        }
    }

    private ImageView createImageViewForEvent(Event event) {
        try {
            String imagePath = "/Images/App/" + event.getImagePath();
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(imageWidth);
            imageView.setFitHeight(imageHeight);

            imageView.setOnMouseClicked(e -> onImageViewClick(event));

            return imageView;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved indlæsning af billede: " + e.getMessage());
        }
    }

    private Label createLabelForEvent(Event event) {
        String labelText = event.getName() + " - " + event.getTime();
        Label label = new Label(labelText);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPrefWidth(imageWidth);
        label.setPrefHeight(35);

        Rectangle background = new Rectangle(imageWidth, 35);
        background.getStyleClass().add("my-imagelabel-rectangle-style");
        label.setGraphic(background);
        label.setContentDisplay(ContentDisplay.CENTER);

        return label;
    }

    private void onImageViewClick(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SpecificEvent.fxml"));
            Parent root = loader.load();

            SpecificEventController controller = loader.getController();
            controller.populateFields(event);

            Stage stage = new Stage();
            stage.setTitle(event.getName());
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) btnNewEvent.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("noget gik galt");
        }
    }


        @FXML
    private void onNewEvent(ActionEvent actionEvent) {
        loadFXML("/NewEvent.FXML", model, (Stage) btnNewEvent.getScene().getWindow());
    }


}