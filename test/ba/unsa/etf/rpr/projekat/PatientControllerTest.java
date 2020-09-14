package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.Controllers.PatientController;
import ba.unsa.etf.rpr.projekat.Controllers.RegistrationController;
import ba.unsa.etf.rpr.projekat.DAO.AppointmentDAO;
import ba.unsa.etf.rpr.projekat.DAO.DiseaseDAO;
import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.Appointment;
import ba.unsa.etf.rpr.projekat.Models.Disease;
import ba.unsa.etf.rpr.projekat.Models.Patient;
import ba.unsa.etf.rpr.projekat.Models.User;
import ba.unsa.etf.rpr.projekat.Utilities.IllegalDateException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static junit.framework.TestCase.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(ApplicationExtension.class)
public class PatientControllerTest {
    private UserDAO userDAO = new UserDAO();
    private Patient user = (Patient) userDAO.getAllUsers().get(1);

    @Start
    void start(Stage stage) throws IOException, IllegalDateException {
        user.setId(2);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Translation", new Locale("bs", "BA"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patient.fxml"), resourceBundle);
        PatientController ctrl = new PatientController(user, userDAO);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Test");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
        stage.toFront();
    }


    @Test
    void checkPassword(FxRobot robot) {
        Text textField = robot.lookup("#txtPassword").queryAs(Text.class);
        CheckBox field = robot.lookup("#cbShowPassword").queryAs(CheckBox.class);

        assertFalse(textField.isVisible());

        robot.clickOn(field);
        assertFalse(textField.isDisabled());
        assertEquals("Test123", textField.getText());
    }

}
