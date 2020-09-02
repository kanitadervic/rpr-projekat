package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.Appointment;
import ba.unsa.etf.rpr.projekat.Models.DateClass;
import ba.unsa.etf.rpr.projekat.Models.Patient;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import static ba.unsa.etf.rpr.projekat.Main.*;

public class PatientController {
    private final UserDAO userDAO;
    private Patient patient;
    public Text txtWelcome;
    public Text txtName;
    public Text txtEmail;
    public Text txtPhoneNumber;
    public Text txtPassword;
    public CheckBox cbShowPassword;
    public ListView appointmentListView;
    public Button btnChangeAppointment;
    public Button btnDeleteAppointment;
    public Button btnLogOut;
    public Button btnNewAppointment;


    public PatientController(User user, UserDAO userDAO) {
        patient = new Patient(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getPassword(), user.getGender(), user.getDateOfBirth());
        patient.setId(user.getId());
        this.userDAO = userDAO;
    }

    @FXML
    public void initialize() {
        if (patient.getGender().equals("F")) {
            txtWelcome.setText("Dobrodošla, " + patient.getFirstName());
        } else {
            txtWelcome.setText("Dobrodošao, " + patient.getFirstName());
        }
        txtName.setText(patient.getFirstName() + " " + patient.getLastName());
        txtEmail.setText(patient.getEmail());
        txtPhoneNumber.setText(patient.getPhoneNumber());
        txtPassword.setText(patient.getPassword());
        refreshList();
        txtPassword.setVisible(false);
        cbShowPassword.setSelected(false);
    }

    private void refreshList() {
        ObservableList appointmentsForPatient = userDAO.getAppointmentsForPatient(patient.getId());
        SortedList<Appointment> list = new SortedList(appointmentsForPatient);
        list.setComparator(new Comparator<Appointment>() {
            @Override
            public int compare(Appointment o1, Appointment o2) {
                LocalDate l1 = LocalDate.of(Integer.parseInt(o1.getAppointmentDate().getYear()), Integer.parseInt(o1.getAppointmentDate().getMonth()), Integer.parseInt(o1.getAppointmentDate().getDay()));
                LocalDate l2 = LocalDate.of(Integer.parseInt(o2.getAppointmentDate().getYear()), Integer.parseInt(o2.getAppointmentDate().getMonth()), Integer.parseInt(o2.getAppointmentDate().getDay()));
                return l1.compareTo(l2);
            }
        });
        appointmentListView.setItems(list);
    }

    public void showPasswordAction(ActionEvent actionEvent) {
        if (cbShowPassword.isSelected()) {
            txtPassword.setVisible(true);
        } else txtPassword.setVisible(false);
    }

    public void changeAppointmentAction(ActionEvent actionEvent) throws IOException {
        if (appointmentListView.getSelectionModel().getSelectedItem() == null) return; //daje dateclass
        Appointment forModification = new Appointment();
        ArrayList<Appointment> appointments = appointmentDAO.getAllAppointments();
        for (Appointment a : appointments) {
            if (appointmentListView.getSelectionModel().getSelectedItem().toString().equals(a.getAppointmentDate().toString()) && this.patient.getId() == a.getPatient().getId()) {
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
        stage.setTitle(resourceBundle.getString("appointment.modification"));
        stage.setScene(new Scene(root2));
        stage.show();
    }

    public void deleteAppointmentAction(ActionEvent actionEvent) {
        if (appointmentListView.getSelectionModel().getSelectedItem() == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(resourceBundle.getString("appointment.delete"));
        alert.setContentText(resourceBundle.getString("appointment.confirm"));
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            DateClass removeDate = (DateClass) appointmentListView.getSelectionModel().getSelectedItem();
            ArrayList<Appointment> appointmentList = appointmentDAO.getAllAppointments();
            Appointment removing = new Appointment();
            for (Appointment a : appointmentList) {
                if (removeDate.equals(a.getAppointmentDate())) {
                    removing = a;
                    removing.setId(a.getId());
                    break;
                }
            }
            appointmentDAO.removeAppointment(removing.getId());
            appointmentListView.getItems().remove(removeDate);
        }
    }

    public void logOutAction(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
        mainLogicStage.show();
    }

    public void addAppointmentAction(ActionEvent actionEvent) throws IOException {
        NewAppointmentController ctrl = new NewAppointmentController(patient);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newappointment.fxml"), resourceBundle);
        loader.setController(ctrl);
        Parent root2 = loader.load();
        Stage stage = new Stage();
        stage.setTitle(resourceBundle.getString("new.appointment"));
        stage.setScene(new Scene(root2));
        stage.showAndWait();
        refreshList();
    }
}
