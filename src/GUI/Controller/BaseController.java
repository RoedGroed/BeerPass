package GUI.Controller;

import BE.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class BaseController {

    @FXML
    private Button btnLogout;
    @FXML
    private Button btnEvent;
    @FXML
    private Button btnAdmin;
    @FXML
    private Button btnTickets;
    private User loggedInUser;


    /////////////////////
    /// Panel buttons ///
    /////////////////////

    @FXML
    private void onLogout(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        URL url = getClass().getResource("/Login.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            //stage.setFullScreen(true);
            stage.setTitle("BrewPass");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load EventWindow.fxml");
            alert.showAndWait();
        }
    }

    @FXML
    private void onAdmin(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            //stage.setFullScreen(true);
            stage.setTitle("BrewPass");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load AdminWindow.fxml");
            alert.showAndWait();
        }
    }

    @FXML
    private void onTicket(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TicketWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            //stage.setFullScreen(true);
            stage.setTitle("BrewPass");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load TicketWindow.fxml");
            alert.showAndWait();
        }
    }


    /*protected void configureUIBasedOnRole(User user) {
        switch (loggedInUser.getRole()) {
            case role:
                btnAdmin.setVisible(true);
                loadAllEvents();
                break;
            case EVENT_COORDINATOR:
                btnAdmin.setVisible(false);
                loadAssignedEvents();
                break;
        }
    }*/
    private void loadAllEvents() {
    }

    private void loadAssignedEvents() {
    }

}