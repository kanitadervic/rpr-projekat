package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.Appointment;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class DoctorController {
    public Text txtWelcome;
    public Doctor doctor = new Doctor();
    public ListView appointmentListView;
    public UserDAO userDAO;
    public ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public DoctorController(User u, UserDAO userDAO){
        doctor = new Doctor(u.getFirstName(), u.getLastName(), u.getEmail(), u.getPhoneNumber(), u.getUserName(), u.getPassword(), u.getGender(), u.getDateOfBirth());
        doctor.setId(1);
        this.userDAO = userDAO;
    }

    @FXML
    public void initialize(){
        if(doctor.getGender().equals("F")) {
            txtWelcome.setText("Dobrodošla, " + doctor.getFirstName());
        }
        else{
            txtWelcome.setText("Dobrodošao, "+ doctor.getFirstName());
        }
        appointments = userDAO.getAppointmentsForDoctor(doctor.getId());
        appointmentListView.setItems(appointments);

    }
}
