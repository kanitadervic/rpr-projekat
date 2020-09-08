package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.Models.*;
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

import static ba.unsa.etf.rpr.projekat.Main.*;
import static java.lang.Integer.valueOf;

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
        this.appointmentModification.setId(forModification.getId());
    }

    @FXML
    public void initialize() {
        LocalDate localDate = LocalDate.of((appointmentModification.getAppointmentDate().getYear()), (appointmentModification.getAppointmentDate().getMonth()), (appointmentModification.getAppointmentDate().getDayOfMonth()));
        newAppointmentDate.setValue(localDate);
        ObservableList<Doctor> doctors = userDAO.getDoctorUsers();
        doctorChoice.setItems(doctors);

        doctorChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (isDoctorValid(newVal)) {
                doctorChoice.getStyleClass().removeAll("incorrectField");
                doctorChoice.getStyleClass().add("correctField");
            } else {
                doctorChoice.getStyleClass().removeAll("correctField");
                doctorChoice.getStyleClass().add("incorrectField");
            }
        });

        newAppointmentDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (isDateValid(newVal)) {
                newAppointmentDate.getStyleClass().removeAll("incorrectField");
                newAppointmentDate.getStyleClass().add("correctField");
            } else {
                newAppointmentDate.getStyleClass().removeAll("correctField");
                newAppointmentDate.getStyleClass().add("incorrectField");
            }
        });
    }

    private boolean isDoctorValid(Object selectedDoctor) {
        Doctor doctor = (Doctor) selectedDoctor;
        final boolean[] takenDate = {false};
        if(doctor == null) {
            try {
                throw new InvalidDoctorChoice(resourceBundle.getString("invalid.choice"));
            } catch (InvalidDoctorChoice e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return false;
            }
        }
        ObservableList<Doctor> doctors = userDAO.getDoctorUsers();
        LocalDate localDate = newAppointmentDate.valueProperty().get();
        for(User u: doctors){
            if(u.equals(doctor)){
                doctor.setId(doctor.getId());
                break;
            }
        }
        ObservableList<Appointment> appointments = userDAO.getAppointmentsForDoctor(doctor.getId());
        appointments.forEach(appointment -> {
            if(appointment.getAppointmentDate().equals(localDate)) takenDate[0] = true;
        });
        return(!takenDate[0]);
    }

    private boolean checkDoctorChoice() {
        isDoctorValid(doctorChoice.getSelectionModel().getSelectedItem());
        return doctorChoice.getStyleClass().contains("correctField");
    }

    private boolean checkAppointmentDate() {
        return (newAppointmentDate.getStyleClass().contains("correctField"));
    }

    private boolean isDateValid(LocalDate value) {
        LocalDate localDate = LocalDate.now();
        if (value == null || value.isBefore(localDate)) {
            try {
                throw new IllegalDateException(resourceBundle.getString("invalid.date"));
            } catch (IllegalDateException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    public void okClickedAction(ActionEvent actionEvent) {
        if (checkAppointmentDate() && checkDoctorChoice()) {
            appointmentModification.setDoctor((Doctor) doctorChoice.getSelectionModel().getSelectedItem());
            LocalDate localDate = newAppointmentDate.getValue();
            String date = localDate.getDayOfMonth() + "-" + localDate.getMonthValue() + "-" + localDate.getYear();
            appointmentModification.setAppointmentDate(date);
            appointmentDAO.updateAppointmentDate(appointmentModification.getId(), date, appointmentModification.getDoctor().getId());
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
    }

    public void cancelAction(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }


}
