package ba;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPageController {
    public Button btnDoctorLogIn;
    public static Stage startPageStage;
    public Button btnNewAppointment;

    public void logInAction(ActionEvent actionEvent){
        Main.mainLogicStage.hide();

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root2 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Korisnik: Doktor");
            stage.setScene(new Scene(root2));
            startPageStage = stage;
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void dataEntryAction(ActionEvent actionEvent){
        System.out.println("radi");
    }
}
