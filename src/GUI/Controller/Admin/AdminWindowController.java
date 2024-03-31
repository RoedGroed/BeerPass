package GUI.Controller.Admin;

import GUI.Controller.BaseController;
import GUI.Model.Model;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminWindowController extends BaseController implements Initializable {

    private Model model = new Model();

    @FXML
    private ListView lvAdmin;
    @FXML
    private ListView lvEventCo;
    @FXML
    private ListView lvUsers;

    public AdminWindowController() {
        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initListviews();
    }

    private void initListviews() {
        lvAdmin.setItems(model.getUsersByRole("Admin"));
        lvEventCo.setItems(model.getUsersByRole("Event Coordinator"));
        lvUsers.setItems(model.getUsersByRole("User"));

        /*
        lvAdmin.setItems(model.getUsersByRole("Admin").stream()
                .map(user -> user.getUsername() + " - " + user.getEmail())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        lvEventCo.setItems(model.getUsersByRole("Event Coordinator").stream()
                .map(user -> user.getUsername() + " - " + user.getEmail())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        lvUsers.setItems(model.getUsersByRole("User").stream()
                .map(user -> user.getUsername() + " - " + user.getEmail())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
*/
    }

    @FXML
    private void onEditUser(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditUser.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("BrewPass");
            stage.show();
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load App.fxml");
            alert.showAndWait();
        }
    }

    @FXML
    private void onNewUser(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewUser.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("BrewPass");
            stage.show();
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load App.fxml");
            alert.showAndWait();
        }
    }


}
