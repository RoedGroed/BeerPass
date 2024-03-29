package GUI.Controller;

import BE.User;
import DAL.DBConnector;
import GUI.Model.Model;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private MFXButton btnForgot;

    @FXML
    private Label lblPass;

    @FXML
    private Label lblUser;

    @FXML
    private TextField txtPass;

    @FXML
    private TextField txtUser;
    @FXML
    private MFXButton btnLogin;
    private Model model;

    public LoginController() {
        this.model = new Model();
    }

    @FXML
    private void onLogin(ActionEvent actionEvent) throws SQLException, IOException {
        String username = txtUser.getText();
        String password = txtPass.getText();
        if(model.isValidUser(username, password)) {
            Stage stage = (Stage) txtUser.getScene().getWindow();
            stage.close();
            try {

                //Loading the new stage
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventWindow.fxml"));
                Parent root = loader.load();
                //stage stuff
                Stage secStage = new Stage();
                secStage.setTitle("BrewPass");
                secStage.setScene(new Scene(root));
                secStage.show();
            } catch (IOException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("Invalid username or password");
        }
    }

}
