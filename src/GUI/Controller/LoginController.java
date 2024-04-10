package GUI.Controller;

import BE.User;
import GUI.Model.Model;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
    private PasswordField txtPass;

    @FXML
    private TextField txtEmail;
    @FXML
    private MFXButton btnLogin;
    private Model model;

    public LoginController() {
        this.model = Model.getInstance();
    }

    @FXML
    private void onLogin(ActionEvent actionEvent) {
        String email = txtEmail.getText();
        String password = txtPass.getText();

        try {
            User user = model.validateUser(email, password);
            if (user != null){
                Stage stage = (Stage) txtEmail.getScene().getWindow();
                stage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventWindow.fxml"));
                Parent root = loader.load();


                Stage secStage = new Stage();
                secStage.setTitle("BrewPass");
                secStage.setScene(new Scene(root));
                secStage.show();
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Wrong password or username\rPlease try again");
                alert.showAndWait();
            }
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onForgotPassword(){
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "If you have forgotten your password\r" +
                        "Please contact tech support at\r" +
                        "GoodLuck@GettingYourPassword.Back");
        alert.showAndWait();
    }

}
