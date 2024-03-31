package GUI.Controller.Admin;

import BE.User;
import GUI.Controller.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleRectangle.getStyleClass().add("my-gradient-rectangle");
        radiobtnRectangle.getStyleClass().add("my-rectangle-style");
    }

    public void populateFields(User user) {
        User selectedUser = user;
        tfUserName.setText(selectedUser.getUsername());
        tfUserPassword.setText(selectedUser.getPassword());
        tfUserEmail.setText(selectedUser.getEmail());
    }

    @FXML
    private void onDelete(ActionEvent actionEvent) {
    }

    @FXML
    private void onCancel(ActionEvent actionEvent) {
    }

    @FXML
    private void onConfirm(ActionEvent actionEvent) {
    }

}
