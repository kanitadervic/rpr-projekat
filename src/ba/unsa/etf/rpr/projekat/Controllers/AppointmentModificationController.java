package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.Models.Appointment;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.Patient;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import static ba.unsa.etf.rpr.projekat.Main.appointmentDAO;
import static ba.unsa.etf.rpr.projekat.Main.userDAO;

public class AppointmentModificationController {
    public Appointment appointmentModification;
    public Patient patient;
    public Doctor doctor;
    public Button btnCancel;
    public Button btnOk;
    public DatePicker newAppointmentDate;
    public ChoiceBox doctorChoice;

    public AppointmentModificationController(Appointment forModification) {
        this.appointmentModification = forModification;
    }

    @FXML
    public void initialize(){
        ObservableList<User> doctors = userDAO.getDoctorUsers();
        doctorChoice.setItems(doctors);
    }
    public void okClickedAction(ActionEvent actionEvent){
        System.out.println("ok clicked");
    }

    public void cancelAction(ActionEvent actionEvent){
        System.out.println("cancel clicked");
    }
}
