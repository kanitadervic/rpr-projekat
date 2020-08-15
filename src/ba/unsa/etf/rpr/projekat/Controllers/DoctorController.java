package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DoctorController {
    public Label welcomeLabel;
    public Doctor doctor = new Doctor();

    public DoctorController(User u){
        doctor = new Doctor(u.getFirstName(), u.getLastName(), u.getEmail(), u.getPhoneNumber(), u.getUserName(), u.getPassword(), u.getGender(), u.getDateOfBirth());
    }

    @FXML
    public void initialize(){
        if(doctor.getGender().equals("F")) {
            welcomeLabel.setText("Dobrodošla, " + doctor.getFirstName());
        }
        else{
            welcomeLabel.setText("Dobrodošao, "+ doctor.getFirstName());
        }
    }
}
