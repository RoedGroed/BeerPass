package GUI.Controller.Admin;

import BLL.Manager;
import DAL.UserDAO;
import GUI.Controller.BaseController;
import io.github.palexdev.materialfx.controls.MFXButton;
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
    private ToggleGroup roleGroup;

    /**
     *  Adds listeners to the radio buttons for user roles
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        titleRectangle.getStyleClass().add("my-gradient-rectangle");
        radiobtnRectangle.getStyleClass().add("my-rectangle-style");

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

    /**
     * Goes back to AdminWindow
     */
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
            showAlert("Error", "could not open the window");
        }
    }

    /**
     * Clears the previously selected radio button
     */
    private void clearSelectionAndSelect(MFXRadioButton selectedRadioButton) {
        if (selectedRoleButton != null) {
            selectedRoleButton.getStyleClass().remove("selected");
        }
        selectedRadioButton.getStyleClass().add("selected");
        selectedRoleButton = selectedRadioButton;
    }

    private MFXRadioButton selectedRoleButton; // Keep track of the currently selected button

    private Manager manager;

    public NewUserController() {
        this.manager = new Manager();
    }

    /**
     * Handle the conformation of creating a new user
     */
    @FXML
    private void onConfirmUser(ActionEvent event) {

        String username = tfUserName.getText();
        if (!model.validateStringLength(username,30))
        {
            showInformationAlert("Warning", "Name cannot be more than 30 characters");
            return;
        }
        String email = tfUserEmail.getText();
        if (!model.validateEmail(email)){
            showInformationAlert("Warning", "Invalid Email, please check its correct");
            return;
        }

        String password = tfUserPassword.getText();
        String role = null;
        if (userEventCoordinator.isSelected()) {
            role = "Event Coordinator";
        } else if (userAdmin.isSelected()) {
            role = "Admin";
        } else if (userCustomer.isSelected()) {
            role = "User";
        }

        if (role == null) {
            // No radio button was selected, handle this case appropriately
            showInformationAlert("Warning", "No user role selected");
            return;
        }

        try {
            manager.createNewUser(username, password, role, email);

        loadFXML("/AdminWindow.FXML", model, (Stage) tfUserName.getScene().getWindow());

        } catch (SQLException | IOException e) {
            showAlert("Error", "An error occurred while creating a new user");
        }
    }
}