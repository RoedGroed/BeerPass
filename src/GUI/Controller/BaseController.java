package GUI.Controller;

import BE.User;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable {

    protected Model model;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnEvent;
    @FXML
    private Button btnAdmin;
    @FXML
    private Button btnTickets;
    @FXML
    private Label lblAdmin;
    @FXML
    private Label lblUsername;
    private User loggedInUser;
    private String user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        AdminButtonVisibility();
        lblUsername.setText(model.getCurrentUser().getUsername());
    }

    private void AdminButtonVisibility() {

        btnAdmin.setVisible(model.isAdmin());
        lblAdmin.setVisible(model.isAdmin());
    }




    /////////////////////
    /// Panel buttons ///
    /////////////////////

    @FXML
    private void onLogout(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            URL url = getClass().getResource("/Login.fxml");

            stage.setTitle("BrewPass");

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not load the window");
        }
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
            showAlert("Error", "Could not load the window");
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
            showAlert("Error", "Could not load the window");
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
            showAlert("Error", "Could not load the window");
        }
    }

    /**
     * Method to open new FXML, and close the current Stage.
     * @param fxmlPath - A String Holding fxmlpath - "/AdminWindow.FXML"
     * @param model - model
     * @param currentStage - get a node in the controller. Example: (Stage) tfEventName.getScene().getWindow());
     */
    public void loadFXML(String fxmlPath, Model model, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            currentStage.close();

            Stage stage = new Stage();
            stage.setTitle("BrewPass");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            showAlert("Error", "Could not load the window");
        }
    }


/**
 * Handling of expression helper methods.
 */

    public void showAlert(String title, String context) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(context);
        alert.showAndWait();
    }
    public void showInformationAlert(String title, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(context);
        alert.showAndWait();
    }

}