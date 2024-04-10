package GUI.Controller.Admin;

import BE.User;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminWindowController extends BaseController implements Initializable {


    @FXML
    private ListView lvAdmin;
    @FXML
    private ListView lvEventCo;
    @FXML
    private ListView lvUsers;
    @FXML
    private Button btnAdmin;
    private ListView<User> selectedListView;

    public AdminWindowController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        initListviews();

    }

    private void initListviews() {
        lvAdmin.setItems(model.getUsersByRole("Admin"));
        lvEventCo.setItems(model.getUsersByRole("Event Coordinator"));
        lvUsers.setItems(model.getUsersByRole("User"));
        addListListener();

    }


    /**
     *
     * @param actionEvent
     * @throws IOException
     * Checks if a user or a listview != null.
     * Passes the information from the user-object into the corresponding fields.
     */
    @FXML
    private void onEditUser(ActionEvent actionEvent) throws IOException {

        if ((selectedListView != null && selectedListView.getSelectionModel().getSelectedItem() != null)) {
            User selectedUser = selectedListView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditUser.fxml"));
            Parent root = loader.load();

            EditUserController editUserController = loader.getController();
            editUserController.populateFields(selectedUser);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit User");
            stage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a user to edit.");
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


    private void addListListener(){
        lvAdmin.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedListView = lvAdmin);
        lvEventCo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedListView = lvEventCo);
        lvUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedListView = lvUsers);
    }


}
