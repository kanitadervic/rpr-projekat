package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.Main;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.Patient;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

import static ba.unsa.etf.rpr.projekat.Main.mainLogicStage;
import static ba.unsa.etf.rpr.projekat.Main.userDAO;

public class StartPageController {
    public Button btnLogIn;
    public TextField fldUsername;
    public TextField fldPassword;
    public ImageView userImage;
    public static Stage registrationStage;

    public void registerAction(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
        try {
            RegistrationController ctrl = new RegistrationController(userDAO);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"));
            loader.setController(ctrl);
            Parent root2 = loader.load();
            stage = new Stage();
            stage.setTitle("Registracija");
            stage.setScene(new Scene(root2));
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
        for (User u : users) {
            if (u.getUserName().equals(fldUsername.getText()) && u.getPassword().equals(fldPassword.getText())) {
                doctor = userDAO.checkIfDoctor(u);
                found = true;
                user = new User(u.getFirstName(), u.getLastName(), u.getEmail(), u.getPhoneNumber(), u.getUserName(), u.getPassword(), u.getGender(), u.getDateOfBirth());
                user.setId(u.getId());
                break;
            }
        }
        if (found && doctor) {
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
            DoctorController ctrl = new DoctorController(user, userDAO);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor.fxml"));
            loader.setController(ctrl);
            Parent root2 = loader.load();
            stage = new Stage();
            stage.setTitle("Doktor");
            stage.setScene(new Scene(root2));
            stage.show();
        } else if (found) {
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
            PatientController ctrl = new PatientController(user, userDAO);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patient.fxml"));
            loader.setController(ctrl);
            Parent root2 = loader.load();
            stage = new Stage();
            stage.setTitle("Pacijent");
            stage.setScene(new Scene(root2));
            stage.show();
        } else System.out.println("ne radi");
    }
}
