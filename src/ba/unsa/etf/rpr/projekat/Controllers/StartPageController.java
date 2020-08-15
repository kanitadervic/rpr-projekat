package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.Main;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

import static ba.unsa.etf.rpr.projekat.Main.mainLogicStage;
import static ba.unsa.etf.rpr.projekat.Main.userDAO;

public class StartPageController {
    public Button btnLogIn;
    public static Stage startPageStage;
    public TextField fldUsername;
    public TextField fldPassword;
    public ImageView userImage;
    public static Stage registrationStage;
    public static Stage doctorStage;


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

    public void logInAction(ActionEvent actionEvent) throws IOException {
        boolean found = false;
        Boolean doctor = false;
        User user = new User();
        ObservableList<User> users = userDAO.getUsers();
        for(User u: users){
            if(u.getUserName().equals(fldUsername.getText()) && u.getPassword().equals(fldPassword.getText())){
                doctor = userDAO.checkIfAdmin(u);
                found = true;
                user = new User(u.getFirstName(), u.getLastName(), u.getEmail(), u.getPhoneNumber(), u.getUserName(), u.getPassword(), u.getGender(), u.getDateOfBirth());
                break;
            }
        }
        if(found && doctor) {
            mainLogicStage.hide();
            DoctorController ctrl = new DoctorController(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor.fxml"));
            loader.setController(ctrl);
            Parent root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Doktor");
            stage.setScene(new Scene(root2));
            doctorStage = stage;
            stage.show();
        }

        else if(found) System.out.println("samo korisnik");
        else System.out.println("ne radi");
    }
}
