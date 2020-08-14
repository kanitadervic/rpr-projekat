package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

import static ba.unsa.etf.rpr.projekat.Main.userDAO;

public class StartPageController {
    public Button btnLogIn;
    public static Stage startPageStage;
    public TextField fldUsername;
    public TextField fldPassword;
    public ImageView userImage;
    public static Stage registrationStage;


    public void registerAction(ActionEvent actionEvent) {
        Main.mainLogicStage.hide();
        try {
            RegistrationController ctrl = new RegistrationController(userDAO);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"));
            loader.setController(ctrl);
            Parent root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registracija");
            stage.setScene(new Scene(root2));
            registrationStage = stage;
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logInAction(ActionEvent actionEvent) {

    }
}
