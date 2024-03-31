package GUI.Controller.Admin;

import BE.User;
import GUI.Controller.BaseController;
import GUI.Model.Model;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    private Model model;
    private User user;
    @FXML
    private MFXRadioButton userAdmin;
    @FXML
    private MFXRadioButton userEventCoordinator;
    @FXML
    private MFXRadioButton userCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleRectangle.getStyleClass().add("my-gradient-rectangle");
        radiobtnRectangle.getStyleClass().add("my-rectangle-style");
        model = new Model();
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
        tfUserPassword.setText(user.getPassword());
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
    }

    @FXML
    private void onCancel(ActionEvent actionEvent) {

        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminWindow.fxml"));
            Parent root = loader.load();

            Stage adminStage = new Stage();
            adminStage.setScene(new Scene(root));
            adminStage.setTitle("Admin Window");
            adminStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load AdminWindow.fxml");
            alert.showAndWait();
        }
    }

    @FXML
    private void onConfirm(ActionEvent actionEvent) {
        try {
            if (user != null) {
                String username = tfUserName.getText();
                String password = tfUserPassword.getText();
                String email = tfUserEmail.getText();
                String role = getSelectedRole();

                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                user.setRole(role);
                model.updateUser(user);

                // Handle FXML Navigation

                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                currentStage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminWindow.fxml"));
                Parent root = loader.load();

                Stage adminStage = new Stage();
                adminStage.setScene(new Scene(root));
                adminStage.setTitle("Admin Window");
                adminStage.show();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not update user.");
            alert.showAndWait();
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
