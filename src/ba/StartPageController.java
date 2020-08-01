package ba;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPageController {
    public Button btnLogIn;
    public static Stage startPageStage;
    public TextField fldUsername;
    public TextField fldPassword;
    public ImageView userImage;


    public void logInAction(ActionEvent actionEvent) {
        Main.mainLogicStage.hide();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root2 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Korisnik: Doktor");
            stage.setScene(new Scene(root2));
            startPageStage = stage;
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
