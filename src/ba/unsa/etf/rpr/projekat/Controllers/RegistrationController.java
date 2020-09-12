package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.Patient;
import ba.unsa.etf.rpr.projekat.Utilities.IllegalDateException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ba.unsa.etf.rpr.projekat.Main.mainLogicStage;
import static java.lang.Character.*;


public class RegistrationController {
    public Button btnGoBack;
    public TextField fldName;
    public TextField fldLastName;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public TextField fldPhoneNumber;
    public PasswordField fldPasswordRepeat;
    public Button btnConfirmRegistration;
    public TextField fldEmail;
    public TextField fldEmailRepeat;
    public DatePicker birthdayPicker;
    public CheckBox isDoctor;
    public TextField fldCity;
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
        rbMale.setSelected(true);
        fldClinicPassword.setDisable(true);


        fldEmail.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkEmail(newVal)) {
                fldEmail.getStyleClass().removeAll("incorrectField");
                fldEmail.getStyleClass().add("correctField");
            } else {
                fldEmail.getStyleClass().removeAll("correctField");
                fldEmail.getStyleClass().add("incorrectField");
            }
        });

        fldEmailRepeat.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkEmail(newVal) && newVal.equals(fldEmail.getText())) {
                fldEmailRepeat.getStyleClass().removeAll("incorrectField");
                fldEmailRepeat.getStyleClass().add("correctField");
            } else {
                fldEmailRepeat.getStyleClass().removeAll("correctField");
                fldEmailRepeat.getStyleClass().add("incorrectField");
            }
        });

        fldPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkPassword(newVal)) {
                fldPassword.getStyleClass().removeAll("incorrectField");
                fldPassword.getStyleClass().add("correctField");
            } else {
                fldPassword.getStyleClass().removeAll("correctField");
                fldPassword.getStyleClass().add("incorrectField");
            }
        });

        fldPasswordRepeat.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkPassword(newVal) && newVal.equals(fldPassword.getText())) {
                fldPasswordRepeat.getStyleClass().removeAll("incorrectField");
                fldPasswordRepeat.getStyleClass().add("correctField");
            } else {
                fldPasswordRepeat.getStyleClass().removeAll("correctField");
                fldPasswordRepeat.getStyleClass().add("incorrectField");
            }
        });

        fldName.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkName(newVal)) {
                fldName.getStyleClass().removeAll("incorrectField");
                fldName.getStyleClass().add("correctField");
            } else {
                fldName.getStyleClass().removeAll("correctField");
                fldName.getStyleClass().add("incorrectField");
            }
        });

        fldLastName.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && checkName(newVal)) {
                fldLastName.getStyleClass().removeAll("incorrectField");
                fldLastName.getStyleClass().add("correctField");
            } else {
                fldLastName.getStyleClass().removeAll("correctField");
                fldLastName.getStyleClass().add("incorrectField");
            }
        });

        birthdayPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (checkBirthDate(newVal)) {
                birthdayPicker.getStyleClass().removeAll("incorrectField");
                birthdayPicker.getStyleClass().add("correctField");
            } else {
                birthdayPicker.getStyleClass().removeAll("correctField");
                birthdayPicker.getStyleClass().add("incorrectField");
            }
        });

        fldPhoneNumber.textProperty().addListener((obs, oldVal, newVal) -> {
            if (checkPhoneNumber(newVal)) {
                fldPhoneNumber.getStyleClass().removeAll("incorrectField");
                fldPhoneNumber.getStyleClass().add("correctField");
            } else {
                fldPhoneNumber.getStyleClass().removeAll("correctField");
                fldPhoneNumber.getStyleClass().add("incorrectField");
            }
        });

        fldClinicPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals("secretAdmin")) {
                fldClinicPassword.getStyleClass().removeAll("incorrectField");
                fldClinicPassword.getStyleClass().add("correctField");
            } else {
                fldClinicPassword.getStyleClass().removeAll("correctField");
                fldClinicPassword.getStyleClass().add("incorrectField");
            }
        });


    }

    public void registrationAction(ActionEvent actionEvent) throws IllegalDateException {
        if (checkData()) {
            LocalDate localDate = birthdayPicker.getValue();
            LocalDate dateOfBirth = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
            if (!isDoctor.isSelected()) {
                Patient p = new Patient(fldName.getText(), fldLastName.getText(), fldEmail.getText(), fldPhoneNumber.getText(),
                        fldPassword.getText(), getGender(), dateOfBirth);
                userDAO.addUser(p);
                userDAO.getUsers().add(p);
            } else {
                Doctor d = new Doctor(fldName.getText(), fldLastName.getText(), fldEmail.getText(), fldPhoneNumber.getText(),
                        fldPassword.getText(), getGender(), dateOfBirth);
                userDAO.addUser(d);
                userDAO.getUsers().add(d);
            }
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
            mainLogicStage.show();
        }
    }

    private boolean checkData() {
        return (checkName(fldName.getText()) &&
                checkName(fldLastName.getText()) &&
                checkEmail(fldEmail.getText()) &&
                fldEmailRepeat.getText().equals(fldEmail.getText()) &&
                checkPassword(fldPassword.getText()) &&
                fldPasswordRepeat.getText().equals(fldPassword.getText()) &&
                checkBirthDate(birthdayPicker.getValue())
                && checkPhoneNumber(fldPhoneNumber.getText()));
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
        if (value == null) return false;
        LocalDate localDate = LocalDate.of(2010, 1, 1);
        return (value.isBefore(localDate));
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
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
        mainLogicStage.show();
    }
}
