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

import java.time.LocalDate;

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
    public void initialize() {
        ObservableList<User> doctors = userDAO.getDoctorUsers();
        doctorChoice.setItems(doctors);

        doctorChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                doctorChoice.getStyleClass().removeAll("poljeNijeIspravno");
                doctorChoice.getStyleClass().add("poljeIspravno");
            } else {
                doctorChoice.getStyleClass().removeAll("poljeIspravno");
                doctorChoice.getStyleClass().add("poljeNijeIspravno");
            }
        });

        newAppointmentDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (checkAppointmentDate(newVal)) {
                newAppointmentDate.getStyleClass().removeAll("poljeNijeIspravno");
                newAppointmentDate.getStyleClass().add("poljeIspravno");
            } else {
                newAppointmentDate.getStyleClass().removeAll("poljeIspravno");
                newAppointmentDate.getStyleClass().add("poljeNijeIspravno");
            }
        });
    }

    private boolean checkAppointmentDate(LocalDate value) {
        LocalDate localDate = LocalDate.of(2010, 1, 1);
        return (value.isBefore(localDate));
    }

    public void okClickedAction(ActionEvent actionEvent) {
        System.out.println("ok clicked");
    }

    public void cancelAction(ActionEvent actionEvent) {
        System.out.println("cancel clicked");
    }


}
