package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.Models.Appointment;
import ba.unsa.etf.rpr.projekat.Models.DateClass;
import ba.unsa.etf.rpr.projekat.Models.Patient;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;

import static ba.unsa.etf.rpr.projekat.Main.appointmentDAO;
import static ba.unsa.etf.rpr.projekat.Main.userDAO;

public class NewAppointmentController {
    public Button addButton;
    public Button backButton;
    public DatePicker appointmentDate;
    public ChoiceBox cbDoctorChoice;
    private User patient;

    public NewAppointmentController(User patient) {
        this.patient = (User) patient;
        this.patient.setId(patient.getId());
    }

    @FXML
    public void initialize(){
        ObservableList<User> doctors = userDAO.getDoctorUsers();
        cbDoctorChoice.setItems(doctors);

        cbDoctorChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cbDoctorChoice.getStyleClass().removeAll("incorrectField");
                cbDoctorChoice.getStyleClass().add("correctField");
            } else {
                cbDoctorChoice.getStyleClass().removeAll("correctField");
                cbDoctorChoice.getStyleClass().add("incorrectField");
            }
        });

        appointmentDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (checkAppointmentDate(newVal)) {
                appointmentDate.getStyleClass().removeAll("incorrectField");
                appointmentDate.getStyleClass().add("correctField");
            } else {
                appointmentDate.getStyleClass().removeAll("correctField");
                appointmentDate.getStyleClass().add("incorrectField");
            }
        });

    }

    private boolean checkAppointmentDate(LocalDate value) {
        LocalDate localDate = LocalDate.now();
        boolean takenDate = false;
        User doctor = (User) cbDoctorChoice.getSelectionModel().getSelectedItem();
        ObservableList<User> doctors = userDAO.getDoctorUsers();
        DateClass date = new DateClass(value.getDayOfMonth(), value.getMonthValue(), value.getYear());
        for(User u: doctors){
            if(u.equals(doctor)){
                doctor.setId(doctor.getId());
                break;
            }
        }
        ObservableList<Appointment> appointments = userDAO.getAppointmentsForDoctor(doctor.getId());
        for(Appointment a: appointments){
            if(a.getAppointmentDate().equals(date)){
                takenDate = true;
                break;
            }
        }
        return (!value.isBefore(localDate) && !takenDate);
    }

    public void addAction(ActionEvent actionEvent) {
        if (!appointmentDate.getStyleClass().contains("incorrectField") && !cbDoctorChoice.getStyleClass().contains("incorrectField") && cbDoctorChoice.getSelectionModel().getSelectedItem() != null) {
            User doctor = (User) cbDoctorChoice.getSelectionModel().getSelectedItem();
            LocalDate localDate = appointmentDate.getValue();
            DateClass date = new DateClass(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
            Appointment toAdd= new Appointment(doctor, this.patient, date);
            appointmentDAO.addAppointment(toAdd);
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Zauzet datum ili neispravna polja! Probajte ponovo");
            alert.showAndWait();
        }
    }
    public void goBackAction(ActionEvent actionEvent){
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
}
