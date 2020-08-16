package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.Patient;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

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

        txtPassword.setVisible(false);
        cbShowPassword.setSelected(false);
    }

    public void showPasswordAction(ActionEvent actionEvent){
        if(cbShowPassword.isSelected()){
            txtPassword.setVisible(true);
        }
        else txtPassword.setVisible(false);
    }
}
