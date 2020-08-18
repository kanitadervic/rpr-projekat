package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.Appointment;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import static ba.unsa.etf.rpr.projekat.Main.appointmentDAO;
import static ba.unsa.etf.rpr.projekat.Main.mainLogicStage;

public class DoctorController {
    public Text txtWelcome;
    public Doctor doctor = new Doctor();
    public TableView tableViewPatients;
    public Button btnPatient;
    public Button btnAppointmentDelete;
    public Button btnLogOut;
    public UserDAO userDAO;
    public TableColumn columnLastName;
    public TableColumn columnDate;
    public TableColumn columnName;
    public ObservableList<Appointment> appointments = FXCollections.observableArrayList();


    public DoctorController(User u, UserDAO userDAO) {
        doctor = new Doctor(u.getFirstName(), u.getLastName(), u.getEmail(), u.getPhoneNumber(), u.getUserName(), u.getPassword(), u.getGender(), u.getDateOfBirth());
        doctor.setId(u.getId());
        this.userDAO = userDAO;
    }

    private void refresh() {
        tableViewPatients.setItems(appointments);
    }

    @FXML
    public void initialize() {
        if (doctor.getGender().equals("F")) {
            txtWelcome.setText("Dobrodošla, " + doctor.getFirstName());
        } else {
            txtWelcome.setText("Dobrodošao, " + doctor.getFirstName());
        }
        appointments = userDAO.getAppointmentsForDoctor(doctor.getId());
        columnName.setCellValueFactory(new PropertyValueFactory<>("patientFirstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("patientLastName"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        refresh();
    }

    public void logOutAction(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
        mainLogicStage.show();
    }

    public void showPatientAction(ActionEvent actionEvent) {
        if (tableViewPatients.getSelectionModel().getSelectedItem() == null) return;
        Appointment appointment = (Appointment) tableViewPatients.getSelectionModel().getSelectedItem();
        User patient = appointment.getPatient();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Informacije o pacijentu");
        String gender = patient.getGender().equals("F")? "Žensko" : "Muško";
        alert.setContentText("Ime: " + patient.getFirstName() + "\n" +
                "Prezime: " + patient.getLastName() + "\n" +
                "Email: " + patient.getEmail() + "\n" +
                "Broj telefona: " + patient.getPhoneNumber() + "\n" +
                "Spol: " + gender);
        alert.showAndWait();
    }

    public void deleteAppointmentAction(ActionEvent actionEvent) {
        if (tableViewPatients.getSelectionModel().getSelectedItem() == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Brisanje termina");
        alert.setContentText("Da li želite izbrisati termin?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            Appointment appointment = (Appointment) tableViewPatients.getSelectionModel().getSelectedItem();
            appointmentDAO.removeAppointment(appointment.getId());
            tableViewPatients.getItems().remove(appointment);
        }
    }

    public void saveAppointmentsAction(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null) {
            appointmentDAO.writeFileForDoctor(selectedFile, doctor.getId());
        } else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid file");
            alert.showAndWait();
        }
    }
}
