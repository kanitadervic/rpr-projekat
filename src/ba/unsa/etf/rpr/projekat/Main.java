package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.Controllers.RegistrationController;
import ba.unsa.etf.rpr.projekat.DAO.AppointmentDAO;
import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Main extends Application{
    public static Stage mainLogicStage;
    public static UserDAO userDAO = new UserDAO();
    public static AppointmentDAO appointmentDAO = new AppointmentDAO();

    public void resetBase() {
        userDAO.removeInstance();
        File dbfile = new File("users.db");
        dbfile.delete();
        userDAO = UserDAO.getInstance();
        appointmentDAO = AppointmentDAO.getInstance();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        userDAO.importData();
        appointmentDAO.importData();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/startpage.fxml"));
        primaryStage.setTitle("DocOnDuty");
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        mainLogicStage = primaryStage;
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
