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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    @FXML
    private void onCancel(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminWindow.fxml"));
            Parent root = loader.load();

            BaseController controller = loader.getController();
            controller.setModel(model);

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

    @FXML
    private void onConfirmUser(ActionEvent event) {
        String username = tfUserName.getText();
        String email = tfUserEmail.getText();
        String password = tfUserPassword.getText();
        String role = null;
        if (userEventCoordinator.isSelected()) {
            role = "Event Coordinator";
        } else if (userAdmin.isSelected()) {
            role = "Admin";
        } else if (userCustomer.isSelected()) {
            role = "Customer";
        }

        if (role == null) {
            // No radio button was selected, handle this case appropriately
            System.err.println("Please select a role.");
            return;
        }

        try {
            manager.createNewUser(username, password, role, email);



            // Close current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // And open AdminWindow
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminWindow.fxml"));
            Parent root = loader.load();

            BaseController controller = loader.getController();
            controller.setModel(model);

            Stage adminStage = new Stage();
            adminStage.setScene(new Scene(root));
            adminStage.setTitle("Admin Window");
            adminStage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Show error notification
        }
    }
}