package GUI.Controller.Admin;

import DAL.UserDAO;
import GUI.Controller.BaseController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Rectangle;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewUserController extends BaseController implements Initializable {

    @FXML
    private Rectangle titleRectangle;
    @FXML
    private Rectangle radiobtnRectangle;
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfUserEmail;
    @FXML
    private TextField tfUserPassword;
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
        // Add these listeners:
        userAdmin.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                clearSelectionAndSelect(userAdmin);
            }
            ToggleGroup roleGroup = new ToggleGroup();

            userAdmin.setToggleGroup(roleGroup);
            userEventCoordinator.setToggleGroup(roleGroup);
            userCustomer.setToggleGroup(roleGroup);
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
    }

    @FXML
    private void onCancel(ActionEvent actionEvent) {
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
    private void onConfirmUser(ActionEvent event) {
        String username = tfUserName.getText();
        String email = tfUserEmail.getText();
        String password = tfUserPassword.getText();
        

        String role;
        if (selectedRoleButton == userAdmin) {
            role = "Admin";
        } else if (selectedRoleButton == userEventCoordinator) {
            role = "Event coordinator";
        } else if (selectedRoleButton == userCustomer) {
            role = "User";
        } else {
            role = null; // handle the case where no button is selected, if needed
        }

        try {
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.createNewUser(username, password, role, email);
            if (success) {
                // Clear the text fields
                tfUserName.clear();
                tfUserEmail.clear();
                tfUserPassword.clear();

                // Deselect all MFXRadioButtons
                userAdmin.setSelected(false);
                userEventCoordinator.setSelected(false);
                userCustomer.setSelected(false);

            } else {

            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Show error notification
        }
    }
}