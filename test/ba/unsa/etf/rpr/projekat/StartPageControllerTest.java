package ba.unsa.etf.rpr.projekat;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import static org.testfx.api.FxAssert.verifyThat;

import org.testfx.matcher.base.NodeMatchers;

@ExtendWith(ApplicationExtension.class)
public class StartPageControllerTest {
    Stage mainStage;
    @Start
    void start(Stage stage) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Translation", new Locale("bs", "BA"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/startpage.fxml"),resourceBundle);
        Parent root = loader.load();
        stage.setTitle("Test");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        mainStage = stage;
        stage.show();
        stage.toFront();
    }

    @Test
    void checkLogIn(FxRobot robot){
        robot.clickOn("#fldEmail").write("student@faks.com");
        robot.clickOn("#fldPassword").write("test");
        robot.clickOn("#btnLogIn");

        //log in should be invalid
        verifyThat("OK", NodeMatchers.isVisible());
        robot.clickOn("OK");


        robot.clickOn("#fldEmail").eraseText(17);
        robot.clickOn("#fldEmail").write("admin@faks.com");
        robot.clickOn("#btnLogIn");

        //incorrect password
        verifyThat("OK", NodeMatchers.isVisible());
        robot.clickOn("OK");

        //log in successfull
        robot.clickOn("#fldPassword").eraseText(4);
        robot.clickOn("#fldPassword").write("Test123");

    }
}
