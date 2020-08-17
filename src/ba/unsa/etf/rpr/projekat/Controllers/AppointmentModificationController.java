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

import static ba.unsa.etf.rpr.projekat.Main.appointmentDAO;
import static ba.unsa.etf.rpr.projekat.Main.userDAO;
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
        LocalDate localDate = LocalDate.of(valueOf(appointmentModification.getAppointmentDate().getYear()), valueOf(appointmentModification.getAppointmentDate().getMonth()), valueOf(appointmentModification.getAppointmentDate().getDay()));
        newAppointmentDate.setValue(localDate);
        ObservableList<User> doctors = userDAO.getDoctorUsers();
        doctorChoice.setItems(doctors);

        doctorChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                doctorChoice.getStyleClass().removeAll("incorrectField");
                doctorChoice.getStyleClass().add("correctField");
            } else {
                doctorChoice.getStyleClass().removeAll("correctField");
                doctorChoice.getStyleClass().add("incorrectField");
            }
        });

        newAppointmentDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (checkAppointmentDate(newVal)) {
                newAppointmentDate.getStyleClass().removeAll("incorrectField");
                newAppointmentDate.getStyleClass().add("correctField");
            } else {
                newAppointmentDate.getStyleClass().removeAll("correctField");
                newAppointmentDate.getStyleClass().add("incorrectField");
            }
        });
    }

    private boolean checkAppointmentDate(LocalDate value) {
        LocalDate localDate = LocalDate.now();
        return (!value.isBefore(localDate));
    }

    public void okClickedAction(ActionEvent actionEvent) {
        if (!newAppointmentDate.getStyleClass().contains("incorrectField") && !doctorChoice.getStyleClass().contains("incorrectField") && doctorChoice.getSelectionModel().getSelectedItem() != null) {
            appointmentModification.setDoctor((Doctor) doctorChoice.getSelectionModel().getSelectedItem());
            LocalDate localDate = newAppointmentDate.getValue();
            DateClass date = new DateClass(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
            appointmentModification.setAppointmentDate(date);
            System.out.println(appointmentModification.getId());
            appointmentDAO.updateAppointmentDate(appointmentModification.getId(), date.toString(), appointmentModification.getDoctor().getId());
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Polja nisu popunjena");
            alert.showAndWait();
        }
    }

    public void cancelAction(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }


}
