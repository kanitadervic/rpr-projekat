package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    public TableView tableViewAppointment;
    public TableColumn columnDate;
    public TableColumn columnDisease;
    public Button btnChangeAppointment;
    public Button btnDeleteAppointment;
    public Button btnLogOut;
    public Button btnNewAppointment;
    public ObservableList<Appointment> appointmentsForPatient = FXCollections.observableArrayList();


    public PatientController(User user, UserDAO userDAO) {
        patient = new Patient(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getPassword(), user.getGender(), user.getDateOfBirth());
        patient.setId(user.getId());
        this.userDAO = userDAO;
    }

    @FXML
    public void initialize() {
        if (resourceBundle.getLocale().toString().equals("en")) {
            txtWelcome.setText("Welcome, " + patient.getFirstName());
        } else {
            if (patient.getGender().equals("M")) {
                txtWelcome.setText("Dobrodošao, " + patient.getFirstName());
            } else {
                txtWelcome.setText("Dobrodošla, " + patient.getFirstName());
            }
        }
        txtName.setText(patient.getFirstName() + " " + patient.getLastName());
        txtEmail.setText(patient.getEmail());
        txtPhoneNumber.setText(patient.getPhoneNumber());
        txtPassword.setText(patient.getPassword());
        columnDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        columnDisease.setCellValueFactory(new PropertyValueFactory<>("diseaseName"));
        refreshList();
        txtPassword.setVisible(false);
        cbShowPassword.setSelected(false);
    }

    private void refreshList() {
        appointmentsForPatient = userDAO.getAppointmentsForPatient(patient.getId());
        tableViewAppointment.setItems(appointmentsForPatient);
        Comparator<DateClass> columnComparator =
                (DateClass o1, DateClass o2) -> {
                    LocalDate l1 = LocalDate.of(Integer.parseInt(o1.getYear()), Integer.parseInt(o1.getMonth()), Integer.parseInt(o1.getDay()));
                    LocalDate l2 = LocalDate.of(Integer.parseInt(o2.getYear()), Integer.parseInt(o2.getMonth()), Integer.parseInt(o2.getDay()));
                    return l1.compareTo(l2);
                };
        columnDate.setComparator(columnComparator);
        columnDate.setSortType(TableColumn.SortType.ASCENDING);
        tableViewAppointment.getSortOrder().add(columnDate);
        tableViewAppointment.sort();
    }

    public void showPasswordAction(ActionEvent actionEvent) {
        if (cbShowPassword.isSelected()) {
            txtPassword.setVisible(true);
        } else txtPassword.setVisible(false);
    }

    public void changeAppointmentAction(ActionEvent actionEvent) throws IOException {
        if (tableViewAppointment.getSelectionModel().getSelectedItem() == null) return; //daje dateclass
        Appointment forModification = new Appointment();
        ArrayList<Appointment> appointments = appointmentDAO.getAllAppointments();
        for (Appointment a : appointments) {
            if (tableViewAppointment.getSelectionModel().getSelectedItem().toString().equals(a.getAppointmentDate().toString()) && this.patient.getId() == a.getPatient().getId()) {
                forModification = a;
                forModification.setId(a.getId());
                break;
            }
        }
        if (forModification.getId() == 0) return;
        AppointmentModificationController ctrl = new AppointmentModificationController(forModification);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointmentmodification.fxml"), resourceBundle);
        loader.setController(ctrl);
        Parent root2 = loader.load();
        Stage stage = new Stage();
        stage.setTitle(resourceBundle.getString("appointment.modification"));
        stage.setScene(new Scene(root2));
        stage.showAndWait();
        refreshList();
    }

    public void deleteAppointmentAction(ActionEvent actionEvent) {
        if (tableViewAppointment.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(resourceBundle.getString("appointment.delete"));
            alert.setContentText(resourceBundle.getString("appointment.error"));
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(resourceBundle.getString("appointment.delete"));
        alert.setContentText(resourceBundle.getString("appointment.confirm"));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Appointment removeDate = (Appointment) tableViewAppointment.getSelectionModel().getSelectedItem();
            appointmentDAO.removeAppointment(removeDate.getId());
            tableViewAppointment.getItems().remove(removeDate);
        }
    }

    public void logOutAction(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/startpage.fxml"), resourceBundle);
        try {
            mainLogicStage.setScene(new Scene(loader.load()));
            mainLogicStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
