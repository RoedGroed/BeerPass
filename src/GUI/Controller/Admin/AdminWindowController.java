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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    private List<User> selectedCoordinators = new ArrayList<>();


    public AdminWindowController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        initListviews();
        setupHoverFunctionality();
    }

    private void initListviews() {
        try {
            lvAdmin.setItems(model.getUsersByRole("Admin"));
            lvEventCo.setItems(model.getUsersByRole("Event Coordinator"));
            lvUsers.setItems(model.getUsersByRole("User"));
            addListListener();

        } catch (SQLException | IOException e) {
            showAlert("Error", "An error occurred while performing this action \r" +
                    "please contact support for help");
        }


    }


    /**
     *
     * @param actionEvent
     * @throws IOException
     * Checks if a user or a listview != null.
     * Passes the information from the user-object into the corresponding fields.
     */
    @FXML
    private void onEditUser(ActionEvent actionEvent) {

        if ((selectedListView != null && selectedListView.getSelectionModel().getSelectedItem() != null)) {
            User selectedUser = selectedListView.getSelectionModel().getSelectedItem();
            try {
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
            } catch (IOException e) {
                showAlert("Error", "Could not load the window");
            }
        } else {
            showInformationAlert("Warning", "Please select a user to edit");
        }
    }

    @FXML
    private void onNewUser(ActionEvent actionEvent) {
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
            showAlert("Error", "Could not load the window");
        }
    }


    private void addListListener(){
        lvAdmin.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedListView = lvAdmin);
        lvEventCo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedListView = lvEventCo);
        lvUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedListView = lvUsers);
    }
    private void setupHoverFunctionality() {
        lvAdmin.setCellFactory(param -> createListCell());
        lvEventCo.setCellFactory(param -> createListCell());
        lvUsers.setCellFactory(param -> createListCell());
    }

    private ListCell<User> createListCell() {
        return new ListCell<User>() {
            private VBox hoverBox = new VBox();
            private Label emailLabel = new Label();

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(user.getUsername());

                    // Handle mouse hover events
                    setOnMouseEntered(event -> {
                        setText(null);
                        emailLabel.setText("Email: " + user.getEmail());
                        hoverBox.getChildren().setAll(emailLabel);
                        setGraphic(hoverBox);
                    });

                    setOnMouseExited(event -> {
                        setText(user.getUsername());
                        setGraphic(null);
                    });
                }
            }
        };
    }


}
