package GUI.Controller.Admin;

import BE.User;
import GUI.Controller.BaseController;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleRectangle.getStyleClass().add("my-gradient-rectangle");
        radiobtnRectangle.getStyleClass().add("my-rectangle-style");
        model = new Model();
    }

    public void populateFields(User user) {
        this.user = user;
        tfUserName.setText(user.getUsername());
        tfUserPassword.setText(user.getPassword());
        tfUserEmail.setText(user.getEmail());
    }

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


                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);

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

}
