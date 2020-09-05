package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.Appointment;
import ba.unsa.etf.rpr.projekat.Models.DateClass;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;

import static ba.unsa.etf.rpr.projekat.Main.*;

public class DoctorController {
    public Text txtWelcome;
    public Doctor doctor = new Doctor();
    public TableView tableViewPatients;
    public MenuItem btnPatient;
    public MenuItem btnAppointmentDelete;
    public Button btnLogOut;
    public UserDAO userDAO;
    public TableColumn columnLastName;
    public TableColumn columnDate;
    public TableColumn columnName;
    public TableColumn columnDisease;
    public ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public Menu fileMenu1 = new Menu("_Help");
    public Menu fileMenu2 = new Menu("_File");
    public Menu fileMenu3 = new Menu("_Edit");
    MenuItem btnExit = new MenuItem("_Exit");
    MenuItem btnSave = new MenuItem("_Save");
    MenuItem btnAbout = new Menu("_About");
    public static Stage doctorStage;

    public DoctorController(User u, UserDAO userDAO) {
        doctor = new Doctor(u.getFirstName(), u.getLastName(), u.getEmail(), u.getPhoneNumber(), u.getPassword(), u.getGender(), u.getDateOfBirth());
        doctor.setId(u.getId());
        this.userDAO = userDAO;
    }

    private void refresh() {
        tableViewPatients.setItems(appointments);
        Comparator<DateClass> columnComparator =
                (DateClass o1, DateClass o2) -> {
                    LocalDate l1 = LocalDate.of(Integer.parseInt(o1.getYear()), Integer.parseInt(o1.getMonth()), Integer.parseInt(o1.getDay()));
                    LocalDate l2 = LocalDate.of(Integer.parseInt(o2.getYear()), Integer.parseInt(o2.getMonth()), Integer.parseInt(o2.getDay()));
                    return l1.compareTo(l2);
                };
        columnDate.setComparator(columnComparator);
        columnDate.setSortType(TableColumn.SortType.ASCENDING);
        tableViewPatients.getSortOrder().add(columnDate);
        tableViewPatients.sort();
    }

    @FXML
    public void initialize() {
        if (resourceBundle.getLocale().toString().equals("en")) {
            txtWelcome.setText("Welcome, " + doctor.getFirstName());
        } else {
            if (doctor.getGender().equals("M")) {
                txtWelcome.setText("Dobrodošao, " + doctor.getFirstName());
            } else {
                txtWelcome.setText("Dobrodošla, " + doctor.getFirstName());
            }
        }
        appointments = userDAO.getAppointmentsForDoctor(doctor.getId());
        columnName.setCellValueFactory(new PropertyValueFactory<>("patientFirstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("patientLastName"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        columnDisease.setCellValueFactory(new PropertyValueFactory<>("diseaseName"));
        refresh();
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

    public void showPatientAction(ActionEvent actionEvent) {
        if (tableViewPatients.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(resourceBundle.getString("patient.info"));
            alert.setContentText(resourceBundle.getString("patient.error"));
            alert.showAndWait();
            return;
        }
        Appointment appointment = (Appointment) tableViewPatients.getSelectionModel().getSelectedItem();
        User patient = appointment.getPatient();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(resourceBundle.getString("patient.info"));
        String gender = patient.getGender().equals("F") ? resourceBundle.getString("genderf") : resourceBundle.getString("genderm");
        alert.setContentText(resourceBundle.getString("name") + patient.getFirstName() + "\n" +
                resourceBundle.getString("last.name") + patient.getLastName() + "\n" +
                resourceBundle.getString("email") + patient.getEmail() + "\n" +
                resourceBundle.getString("phone") + patient.getPhoneNumber() + "\n" +
                resourceBundle.getString("gender") + gender + "\n" +
                resourceBundle.getString("disease") + appointment.getDisease());
        alert.showAndWait();

    }

    public void deleteAppointmentAction(ActionEvent actionEvent) {
        if (tableViewPatients.getSelectionModel().getSelectedItem() == null) {
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
            Appointment appointment = (Appointment) tableViewPatients.getSelectionModel().getSelectedItem();
            appointmentDAO.removeAppointment(appointment.getId());
            tableViewPatients.getItems().remove(appointment);
        }
    }

    public void saveAppointmentsAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            appointmentDAO.writeFileForDoctor(selectedFile, doctor.getId());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid file");
            alert.showAndWait();
        }
    }

    public void bosnianAction(ActionEvent actionEvent) {
        Locale.setDefault(new Locale("bs", "BA"));
        resourceBundle = ResourceBundle.getBundle("Translation", new Locale("bs", "BA"));
        setLanguage(new Locale("bs", "BA"));
    }

    public void englishAction(ActionEvent actionEvent) {
        Locale.setDefault(new Locale("en", "EN"));
        resourceBundle = ResourceBundle.getBundle("Translation", new Locale("en", "EN"));
        setLanguage(new Locale("en", "EN"));
    }

    private void setLanguage(Locale locale) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/doctor.fxml"), resourceBundle);
        loader.setController(this);
        try {
            doctorStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
