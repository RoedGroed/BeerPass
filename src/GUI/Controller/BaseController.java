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

    public Model model = new Model();
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
    private User loggedInUser;

    public void setModel(Model model) {
        this.model = model;
        AdminButtonVisibility();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AdminButtonVisibility();
    }

    private void AdminButtonVisibility() {

        btnAdmin.setVisible(model.isAdmin());
        lblAdmin.setVisible(model.isAdmin());
    }




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

            BaseController controller = loader.getController();
            controller.setModel(model);

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

            BaseController controller = loader.getController();
            controller.setModel(model);

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

            BaseController controller = loader.getController();
            controller.setModel(model);

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




}