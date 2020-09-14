package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.Controllers.RegistrationController;
import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


@ExtendWith(ApplicationExtension.class)
public class RegistrationControllerTest {
    private UserDAO userDAO = new UserDAO();
    @Start
    void start(Stage stage) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Translation", new Locale("bs", "BA"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"),resourceBundle);
        RegistrationController ctrl = new RegistrationController(userDAO);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Test");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
        stage.toFront();
    }

    @Test
    void nameValidationTest(FxRobot robot){
        TextField field = robot.lookup("#fldName").queryAs(TextField.class);
        robot.clickOn("#fldName").write("1");
        assertTrue(field.getStyleClass().contains("incorrectField"));

        robot.clickOn("#fldName").eraseText(1);
        robot.clickOn("#fldName").write("As");
        assertTrue(field.getStyleClass().contains("incorrectField"));

        robot.clickOn("#fldName").eraseText(2);
        robot.clickOn("#fldName").write("User");
        assertTrue(field.getStyleClass().contains("correctField"));

    }

    @Test
    void lastNameValidationTest(FxRobot robot){
        TextField field;
        field = robot.lookup("#fldLastName").queryAs(TextField.class);
        robot.clickOn("#fldLastName").write("1");
        assertTrue(field.getStyleClass().contains("incorrectField"));

        robot.clickOn("#fldLastName").eraseText(1);
        robot.clickOn("#fldLastName").write("Surname");
        assertTrue(field.getStyleClass().contains("correctField"));

    }

    @Test
    void emailValidationTest(FxRobot robot){
        TextField field;
        field = robot.lookup("#fldEmail").queryAs(TextField.class);
        robot.clickOn("#fldEmail").write("email");
        assertTrue(field.getStyleClass().contains("incorrectField"));

        robot.clickOn("#fldEmail").write("@email.com");
        assertTrue(field.getStyleClass().contains("correctField"));
    }

    @Test
    void emailRepeatValidationTest(FxRobot robot){
        TextField field, field2;
        field = robot.lookup("#fldEmail").queryAs(TextField.class);
        field2 = robot.lookup("#fldEmailRepeat").queryAs(TextField.class);
        robot.clickOn("#fldEmail").write("email");
        robot.clickOn("#fldEmailRepeat").write("email");
        assertTrue(field.getStyleClass().contains("incorrectField"));
        assertTrue(field2.getStyleClass().contains("incorrectField"));

        robot.clickOn("#fldEmail").write("@email.com");
        assertTrue(field.getStyleClass().contains("correctField"));
        assertTrue(field2.getStyleClass().contains("incorrectField"));

        robot.clickOn("#fldEmailRepeat").write("@email.com");
        assertTrue(field.getStyleClass().contains("correctField"));
        assertTrue(field.getStyleClass().contains("correctField"));
    }

    @Test
    void passwordValidationTest(FxRobot robot){
        TextField field;
        field = robot.lookup("#fldPassword").queryAs(TextField.class);

        robot.clickOn("#fldPassword").write("pass");
        assertTrue(field.getStyleClass().contains("incorrectField"));

        robot.clickOn("#fldPassword").write("1");
        assertTrue(field.getStyleClass().contains("incorrectField"));

        robot.clickOn("#fldPassword").eraseText(5);
        robot.clickOn("#fldPassword").write("Test1");
        assertTrue(field.getStyleClass().contains("correctField"));
    }

    @Test
    void passwordRepeatValidationTest(FxRobot robot) {
        TextField field2;
        field2 = robot.lookup("#fldPasswordRepeat").queryAs(TextField.class);

        robot.clickOn("#fldPassword").write("Test1");
        robot.clickOn("#fldPasswordRepeat").write("p");
        assertTrue(field2.getStyleClass().contains("incorrectField"));

        robot.clickOn("#fldPasswordRepeat").write("pass");
        assertTrue(field2.getStyleClass().contains("incorrectField"));

        robot.clickOn("#fldPasswordRepeat").eraseText(5);
        robot.clickOn("#fldPasswordRepeat").write("Test1");
        assertTrue(field2.getStyleClass().contains("correctField"));
    }

    @Test
    void checkBoxNotSelectedTest(FxRobot robot){
        TextField field = robot.lookup("#fldClinicPassword").queryAs(TextField.class);
        assertTrue(field.isDisabled());
    }

    @Test
    void checkBoxSelectedTest(FxRobot robot){
        TextField field2 = robot.lookup("#fldClinicPassword").queryAs(TextField.class);
        CheckBox field = robot.lookup("#isDoctor").queryAs(CheckBox.class);
        robot.clickOn(field);
        assertFalse(field2.isDisabled());

        robot.clickOn(field2).write("test");
        assertTrue(field2.getStyleClass().contains("incorrectField"));

        robot.clickOn(field2).eraseText(4).write("secretAdmin");
        assertTrue(field2.getStyleClass().contains("correctField"));
    }
}
