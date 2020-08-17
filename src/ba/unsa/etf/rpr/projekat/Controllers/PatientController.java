package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.Appointment;
import ba.unsa.etf.rpr.projekat.Models.DateClass;
import ba.unsa.etf.rpr.projekat.Models.Patient;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static ba.unsa.etf.rpr.projekat.Main.*;

public class PatientController {
    private final UserDAO userDAO;
    private Patient patient;
    public Text txtWelcome;
    public Text txtName;
    public Text txtEmail;
    public Text txtPhoneNumber;
    public Text txtUsername;
    public Text txtPassword;
    public CheckBox cbShowPassword;
    public ListView appointmentListView;
    public Button btnChangeAppointment;
    public Button btnDeleteAppointment;


    public PatientController(User user, UserDAO userDAO) {
        patient = new Patient(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getUserName(), user.getPassword(), user.getGender(), user.getDateOfBirth());
        patient.setId(user.getId());
        this.userDAO = userDAO;
    }

    @FXML
    public void initialize(){
        if (patient.getGender().equals("F")) {
            txtWelcome.setText("Dobrodošla, " + patient.getFirstName());
        } else {
            txtWelcome.setText("Dobrodošao, " + patient.getFirstName());
        }
        txtName.setText(patient.getFirstName() + " " + patient.getLastName());
        txtEmail.setText(patient.getEmail());
        txtPhoneNumber.setText(patient.getPhoneNumber());
        txtUsername.setText(patient.getUserName());
        txtPassword.setText(patient.getPassword());
        ObservableList appointmentsForPatient = userDAO.getAppointmentsForPatient(patient.getId());
        appointmentListView.setItems(appointmentsForPatient);
        txtPassword.setVisible(false);
        cbShowPassword.setSelected(false);
    }

    public void showPasswordAction(ActionEvent actionEvent){
        if(cbShowPassword.isSelected()){
            txtPassword.setVisible(true);
        }
        else txtPassword.setVisible(false);
    }

    public void changeAppointmentAction(ActionEvent actionEvent) throws IOException {
        if (appointmentListView.getSelectionModel().getSelectedItem() == null) return; //daje dateclass
        Appointment forModification = new Appointment();
        ArrayList<Appointment> appointments = appointmentDAO.getAllAppointments();
        for(Appointment a: appointments){
            if(appointmentListView.getSelectionModel().getSelectedItem().toString().equals(a.getAppointmentDate().toString()) && this.patient.getId() == a.getPatient().getId()){
                forModification = a;
                forModification.setId(a.getId());
                break;
            }
        }
        AppointmentModificationController ctrl = new AppointmentModificationController(forModification);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointmentmodification.fxml"));
        loader.setController(ctrl);
        Parent root2 = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Modifikacija");
        stage.setScene(new Scene(root2));
        stage.show();
    }

    public void deleteAppointmentAction(ActionEvent actionEvent){
        if (appointmentListView.getSelectionModel().getSelectedItem() == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Brisanje termina");
        alert.setContentText("Da li želite otkazati termin?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            DateClass removeDate = (DateClass) appointmentListView.getSelectionModel().getSelectedItem();
            ArrayList<Appointment> appointmentList = appointmentDAO.getAllAppointments();
            Appointment removing = new Appointment();
            for(Appointment a: appointmentList){
                if(removeDate.equals(a.getAppointmentDate())){
                    removing = a;
                    removing.setId(a.getId());
                    break;
                }
            }
            appointmentDAO.removeAppointment(removing.getId());
            appointmentListView.getItems().remove(removeDate);
        }
    }
}
