package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAO.AppointmentDAO;
import ba.unsa.etf.rpr.projekat.DAO.DiseaseDAO;
import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Main extends Application{
    public static Stage mainLogicStage;
    public static UserDAO userDAO = new UserDAO();
    public static AppointmentDAO appointmentDAO = new AppointmentDAO();
    public static DiseaseDAO diseaseDAO = new DiseaseDAO();
    public static ResourceBundle resourceBundle = ResourceBundle.getBundle("Translation", new Locale("bs","BA"));

    @Override
    public void start(Stage primaryStage) throws Exception{
        userDAO.importData();
        appointmentDAO.importData();
        diseaseDAO.importData();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/startpage.fxml"), resourceBundle);
        primaryStage.setTitle("DocOnDuty");
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.setResizable(false);
        mainLogicStage = primaryStage;
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
