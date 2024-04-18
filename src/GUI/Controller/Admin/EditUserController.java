package GUI.Controller.Admin;

import BE.User;
import GUI.Controller.BaseController;
import GUI.Model.Model;
import io.github.palexdev.materialfx.controls.MFXRadioButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditUserController extends BaseController implements Initializable {

    @FXML
    private Rectangle titleRectangle;
    @FXML
    private Rectangle radiobtnRectangle;
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfUserPassword;
    @FXML
    private TextField tfUserEmail;
    private User user;
    @FXML
    private MFXRadioButton userAdmin;
    @FXML
    private MFXRadioButton userEventCoordinator;
    @FXML
    private MFXRadioButton userCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        titleRectangle.getStyleClass().add("my-gradient-rectangle");
        radiobtnRectangle.getStyleClass().add("my-rectangle-style");
        // Add these listeners:
        userAdmin.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                clearSelectionAndSelect(userAdmin);
            }
        });

        userEventCoordinator.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                clearSelectionAndSelect(userEventCoordinator);
            }
        });

        userCustomer.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                clearSelectionAndSelect(userCustomer);
            }
        });
        ToggleGroup roleGroup = new ToggleGroup();

        userAdmin.setToggleGroup(roleGroup);
        userEventCoordinator.setToggleGroup(roleGroup);
        userCustomer.setToggleGroup(roleGroup);
    }


    public void populateFields(User user) {
        this.user = user;
        tfUserName.setText(user.getUsername());
        //tfUserPassword.setText(user.getPassword());
        tfUserEmail.setText(user.getEmail());
        if (user.getRole().equals("Admin")) {
            userAdmin.setSelected(true);
        } else if (user.getRole().equals("Event Coordinator")) {
            userEventCoordinator.setSelected(true);
        } else if (user.getRole().equals("User")) {
            userCustomer.setSelected(true);
        }
    }

    private void clearSelectionAndSelect(MFXRadioButton selectedRadioButton) {
        if (selectedRoleButton != null) {
            selectedRoleButton.getStyleClass().remove("selected");
        }
        selectedRadioButton.getStyleClass().add("selected");
        selectedRoleButton = selectedRadioButton;
    }

    private MFXRadioButton selectedRoleButton; // Keep track of the currently selected button


    @FXML
    private void onDelete(ActionEvent actionEvent) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this user?");
        confirmationAlert.setContentText("This action cannot be undone");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    model.deleteUser(user.getUserID());
                } catch (SQLException | IOException e) {
                    showAlert("Error", "An error occurred while deleting the user");
                }
                loadFXML("/AdminWindow.FXML", model, (Stage) radiobtnRectangle.getScene().getWindow());
            }
        });

    }

    @FXML
    private void onCancel(ActionEvent actionEvent) {
        loadFXML("/AdminWindow.FXML",model, (Stage) tfUserName.getScene().getWindow());
    }

    @FXML
    private void onConfirm(ActionEvent actionEvent) {
        try {
            if (user != null) {

                String username = tfUserName.getText();
                String password = tfUserPassword.getText();
                String email = tfUserEmail.getText();
                String role = getSelectedRole();

                if (!model.validateStringLength(username,30))
                {
                    showInformationAlert("Warning","Name cannot be more than 30 characters");
                    return;
                }
                if (!model.validateEmail(email)){
                    showInformationAlert("Warning","Invalid Email, please retry:\rexample@email.com");
                    return;
                }
                user.setUsername(username);
                user.setEmail(email);
                user.setRole(role);

                // Checks if the password has been changed.
                if (!password.isEmpty()) {
                    user.setPassword(password);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are changing the password.\rAre you sure you want to do this?");
                    alert.showAndWait();
                }
                // Updates the user object with changed variables.
                model.updateUser(user);

                loadFXML("/AdminWindow.FXML",model,(Stage) tfUserEmail.getScene().getWindow());
            }
        } catch (SQLException | IOException e) {
            showAlert("Error", "Could not update user");
        }
    }

    private String getSelectedRole() {
        if (userAdmin.isSelected()) {
            return "Admin";
        } else if (userCustomer.isSelected()) {
            return "User";
        } else if (userEventCoordinator.isSelected()) {
            return "Event Coordinator";
        }
        //if none of the role is selected it will return null
        return null;
    }


}
