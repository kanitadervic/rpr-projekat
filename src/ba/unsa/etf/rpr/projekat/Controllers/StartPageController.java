package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.User;
import ba.unsa.etf.rpr.projekat.Utilities.IllegalDateException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static ba.unsa.etf.rpr.projekat.Controllers.DoctorController.doctorStage;
import static ba.unsa.etf.rpr.projekat.Controllers.PatientController.patientStage;
import static ba.unsa.etf.rpr.projekat.Main.*;

public class StartPageController {
    public RadioButton rbBosnian;
    public RadioButton rbEnglish;
    public Button btnLogIn;
    public TextField fldEmail;
    public TextField fldPassword;
    public ImageView userImage;
    public static Stage registrationStage;
    private UserDAO userDAO = new UserDAO();


    @FXML
    public void initialize() {
        if (resourceBundle.getLocale().toString().equals("en")) {
            rbEnglish.setSelected(true);
        } else rbBosnian.setSelected(true);
        rbBosnian.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (rbBosnian.isSelected()) {
                    Locale.setDefault(new Locale("bs", "BA"));
                    resourceBundle = ResourceBundle.getBundle("Translation", new Locale("bs", "BA"));
                    setLanguage(new Locale("bs", "BA"));
                }
            }
        });
        rbEnglish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (rbEnglish.isSelected()) {
                    Locale.setDefault(new Locale("en", "EN"));
                    resourceBundle = ResourceBundle.getBundle("Translation", new Locale("en", "EN"));
                    setLanguage(new Locale("en", "EN"));
                }
            }
        });
    }

    private void setLanguage(Locale locale) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/startpage.fxml"), resourceBundle);
        try {
            Scene scene = new Scene(loader.load());
            mainLogicStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerAction(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
        try {
            RegistrationController ctrl = new RegistrationController(userDAO);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"), resourceBundle);
            loader.setController(ctrl);
            Parent root2 = loader.load();
            stage = new Stage();
            stage.setTitle(resourceBundle.getString("register"));
            stage.setResizable(false);
            stage.setScene(new Scene(root2));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logInAction(ActionEvent actionEvent) throws IOException, IllegalDateException {
        boolean found = false;
        Boolean doctor = false;
        User user = new User();
        ArrayList<User> users = userDAO.getAllUsers();
        for (User u : users) {
            if (u.getEmail().equals(fldEmail.getText()) && u.getPassword().equals(fldPassword.getText())) {
                doctor = (u instanceof Doctor);
                found = true;
                user = new User(u.getFirstName(), u.getLastName(), u.getEmail(), u.getPhoneNumber(), u.getPassword(), u.getGender(), u.getDateOfBirth());
                user.setId(u.getId());
                break;
            }
        }
        if (found && doctor) {
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
            DoctorController ctrl = new DoctorController(user, userDAO);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor.fxml"), resourceBundle);
            loader.setController(ctrl);
            Parent root2 = loader.load();
            stage = new Stage();
            stage.setTitle(resourceBundle.getString("doctor"));
            stage.setResizable(false);
            stage.setScene(new Scene(root2));
            doctorStage = stage;
            stage.show();
        } else if (found) {
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
            PatientController ctrl = new PatientController(user, userDAO);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patient.fxml"), resourceBundle);
            loader.setController(ctrl);
            Parent root2 = loader.load();
            stage = new Stage();
            stage.setTitle(resourceBundle.getString("patient"));
            stage.setResizable(false);
            stage.setScene(new Scene(root2));
            patientStage = stage;
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(resourceBundle.getString("error"));
            alert.setContentText(resourceBundle.getString("wrong.data"));
            alert.showAndWait();
        }
    }
}
