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
    private TextField txtPass;

    @FXML
    private TextField txtEmail;
    @FXML
    private MFXButton btnLogin;
    private Model model;

    public LoginController() {
        this.model = new Model();
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

                BaseController controller = loader.getController();
                controller.setModel(model);

                Stage secStage = new Stage();
                secStage.setTitle("BrewPass");
                secStage.setScene(new Scene(root));
                secStage.show();
            }else {
                System.out.println("Wrong email or password.");
            }
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
