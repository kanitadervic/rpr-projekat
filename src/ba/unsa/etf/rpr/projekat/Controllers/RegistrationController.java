package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ba.unsa.etf.rpr.projekat.Controllers.StartPageController.registrationStage;
import static ba.unsa.etf.rpr.projekat.Main.mainLogicStage;
import static java.lang.Character.*;


public class RegistrationController {
    public Button btnGoBack;
    public TextField fldName;
    public TextField fldLastName;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public TextField fldPhoneNumber;
    public Button btnConfirmRegistration;
    public TextField fldEmail;
    public DatePicker birthdayPicker;
    public CheckBox isDoctor;
    public TextField fldCity;
    public ChoiceBox hospitalChoice;
    public RadioButton rbMale;
    public RadioButton rbFemale;
    public PasswordField fldClinicPassword;
    private UserDAO userDAO;


    public RegistrationController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        rbMale.setToggleGroup(group);
        rbFemale.setToggleGroup(group);
        fldClinicPassword.setDisable(true);


        fldUsername.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkUserName(newVal)) {
                fldUsername.getStyleClass().removeAll("poljeNijeIspravno");
                fldUsername.getStyleClass().add("poljeIspravno");
            } else {
                fldUsername.getStyleClass().removeAll("poljeIspravno");
                fldUsername.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldEmail.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkEmail(newVal)) {
                fldEmail.getStyleClass().removeAll("poljeNijeIspravno");
                fldEmail.getStyleClass().add("poljeIspravno");
            } else {
                fldEmail.getStyleClass().removeAll("poljeIspravno");
                fldEmail.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkPassword(newVal)) {
                fldPassword.getStyleClass().removeAll("poljeNijeIspravno");
                fldPassword.getStyleClass().add("poljeIspravno");
            } else {
                fldPassword.getStyleClass().removeAll("poljeIspravno");
                fldPassword.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldCity.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                fldCity.getStyleClass().removeAll("poljeNijeIspravno");
                fldCity.getStyleClass().add("poljeIspravno");
            } else {
                fldCity.getStyleClass().removeAll("poljeIspravno");
                fldCity.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldName.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkName(newVal)) {
                fldName.getStyleClass().removeAll("poljeNijeIspravno");
                fldName.getStyleClass().add("poljeIspravno");
            } else {
                fldName.getStyleClass().removeAll("poljeIspravno");
                fldName.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldLastName.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkName(newVal)) {
                fldLastName.getStyleClass().removeAll("poljeNijeIspravno");
                fldLastName.getStyleClass().add("poljeIspravno");
            } else {
                fldLastName.getStyleClass().removeAll("poljeIspravno");
                fldLastName.getStyleClass().add("poljeNijeIspravno");
            }
        });

        birthdayPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (checkBirthDate(newVal)) {
                birthdayPicker.getStyleClass().removeAll("poljeNijeIspravno");
                birthdayPicker.getStyleClass().add("poljeIspravno");
            } else {
                birthdayPicker.getStyleClass().removeAll("poljeIspravno");
                birthdayPicker.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPhoneNumber.textProperty().addListener((obs, oldVal, newVal) -> {
            if (checkPhoneNumber(newVal)) {
                fldPhoneNumber.getStyleClass().removeAll("poljeNijeIspravno");
                fldPhoneNumber.getStyleClass().add("poljeIspravno");
            } else {
                fldPhoneNumber.getStyleClass().removeAll("poljeIspravno");
                fldPhoneNumber.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldClinicPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.equals("secretAdmin")){
                fldClinicPassword.getStyleClass().removeAll("poljeNijeIspravno");
                fldClinicPassword.getStyleClass().add("poljeIspravno");
            } else {
                fldClinicPassword.getStyleClass().removeAll("poljeIspravno");
                fldClinicPassword.getStyleClass().add("poljeNijeIspravno");
            }
        });


    }

    public void registrationAction(ActionEvent actionEvent) {
        if (checkData()) {
            LocalDate localDate = birthdayPicker.getValue();
            DateClass dateOfBirth = new DateClass(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
            if(!isDoctor.isSelected()) {
                Patient p = new Patient(fldName.getText(), fldLastName.getText(), fldEmail.getText(), fldPhoneNumber.getText(), fldUsername.getText(),
                        fldPassword.getText(), getGender(), dateOfBirth);
                userDAO.addUser(p);
                userDAO.getUsers().add(p);
            }
            else{
                Doctor d = new Doctor(fldName.getText(), fldLastName.getText(), fldEmail.getText(), fldPhoneNumber.getText(), fldUsername.getText(),
                        fldPassword.getText(), getGender(), dateOfBirth);
                userDAO.addUser(d);
                userDAO.getUsers().add(d);
            }
            registrationStage.close();
            mainLogicStage.show();
        }
    }

    private boolean checkData() {
        return (checkName(fldName.getText()) && checkName(fldLastName.getText()) && checkEmail(fldEmail.getText()) && checkPassword(fldPassword.getText()) && checkUserName(fldUsername.getText()) && checkBirthDate(birthdayPicker.getValue()) && checkPhoneNumber(fldPhoneNumber.getText()));
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        boolean check = false;
        if (phoneNumber.length() == 11 || phoneNumber.length() == 12) {
            if (phoneNumber.charAt(3) == '/' && phoneNumber.charAt(7) == '-') {
                check = true;
            }
        }
        if (!check) return false;
        for (int i = 0; i < 3; i++) {
            if (!isDigit(phoneNumber.charAt(i))) return false;
        }
        for (int i = 4; i < 7; i++) {
            if (!isDigit(phoneNumber.charAt(i))) return false;
        }
        for (int i = 8; i < 11; i++) {
            if (!isDigit(phoneNumber.charAt(i))) return false;
        }
        if (phoneNumber.length() == 12) {
            if (!isDigit(phoneNumber.charAt(11)) || phoneNumber.charAt(2) != '0') return false;
        }
        return true;
    }

    private boolean checkBirthDate(LocalDate value) {
        LocalDate localDate = LocalDate.of(2010, 1, 1);
        return (value.isBefore(localDate));
    }

    private boolean checkUserName(String username) {
        Pattern p = Pattern.compile("[!@#$%&*()+=|<>?{}\\[\\]~.,-]");
        if (username.length() > 16 || username.isEmpty()) return false;
        if (username.startsWith("_") || username.startsWith("$")) return false;
        Matcher m = p.matcher(username);
        if (m.find()) return false;
        if (hasSpace(username)) return false;
        else return true;
    }

    private boolean hasSpace(String text) {
        return text.indexOf(' ') >= 0;
    }

    private boolean checkPassword(String password) {
        boolean valid = false;
        for (int i = 0; i < password.length(); i++) {
            if (isUpperCase(password.charAt(i))) {
                valid = true;
                break;
            }
        }
        if (!valid) return false;
        valid = false;

        for (int i = 0; i < password.length(); i++) {
            if (isLowerCase(password.charAt(i))) {
                valid = true;
                break;
            }
        }
        if (!valid) return false;
        valid = false;

        for (int i = 0; i < password.length(); i++) {
            if (isDigit(password.charAt(i))) {
                valid = true;
                break;
            }
        }
        return valid;
    }

    private boolean checkEmail(String email) {
        if (email.isEmpty()) return false;
        Pattern p = Pattern.compile("[!#$%&*()+=|<>?{}\\[\\]~,]");
        Matcher m = p.matcher(email);
        if (m.find()) return false;
        int indeks = email.indexOf('@');
        if (!(indeks > 0) || indeks == email.length() - 1) return false;
        return true;
    }

    private boolean checkName(String name) {
        Pattern p = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~.,]");
        if (name.length() < 3) return false;
        Matcher m = p.matcher(name);
        if (m.find()) return false;
        p = Pattern.compile("[0-9]");
        m = p.matcher(name);
        if (m.find()) return false;
        else return true;
    }

    private String getGender() {
        if (rbFemale.isSelected()) {
            return "F";
        } else return "M";
    }

    public void doctorCheck(ActionEvent actionEvent) {
        if (isDoctor.isSelected()) {
            fldClinicPassword.setDisable(false);
        } else {
            fldClinicPassword.setDisable(true);
        }
    }

    public void goBackAction(ActionEvent actionEvent) {
        registrationStage.hide();
        mainLogicStage.show();
    }
}
