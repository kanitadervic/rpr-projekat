package ba;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class RegistrationController {
    public Button btnGoBack;
    public TextField fldName;
    public TextField fldLastName;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public Button btnConfirmRegistration;
    public TextField fldEmail;
    public DatePicker birthdayPicker;
    public CheckBox isDoctor;
    public TextField fldCity;
    public ChoiceBox hospitalChoice;
    public RadioButton rbMale;
    public RadioButton rbFemale;


    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        rbMale.setToggleGroup(group);
        rbFemale.setToggleGroup(group);
        hospitalChoice.setDisable(true);

        fldUsername.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldUsername.getStyleClass().removeAll("poljeNijeIspravno");
                fldUsername.getStyleClass().add("poljeIspravno");
            } else {
                fldUsername.getStyleClass().removeAll("poljeIspravno");
                fldUsername.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldEmail.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldEmail.getStyleClass().removeAll("poljeNijeIspravno");
                fldEmail.getStyleClass().add("poljeIspravno");
            } else {
                fldEmail.getStyleClass().removeAll("poljeIspravno");
                fldEmail.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPassword.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldPassword.getStyleClass().removeAll("poljeNijeIspravno");
                fldPassword.getStyleClass().add("poljeIspravno");
            } else {
                fldPassword.getStyleClass().removeAll("poljeIspravno");
                fldPassword.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldCity.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldCity.getStyleClass().removeAll("poljeNijeIspravno");
                fldCity.getStyleClass().add("poljeIspravno");
            } else {
                fldCity.getStyleClass().removeAll("poljeIspravno");
                fldCity.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldName.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldName.getStyleClass().removeAll("poljeNijeIspravno");
                fldName.getStyleClass().add("poljeIspravno");
            } else {
                fldName.getStyleClass().removeAll("poljeIspravno");
                fldName.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldLastName.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldLastName.getStyleClass().removeAll("poljeNijeIspravno");
                fldLastName.getStyleClass().add("poljeIspravno");
            } else {
                fldLastName.getStyleClass().removeAll("poljeIspravno");
                fldLastName.getStyleClass().add("poljeNijeIspravno");
            }
        });


    }

    public void registrationAction(ActionEvent actionEvent) {

    }

    public void doctorCheck(ActionEvent actionEvent) {
        if (isDoctor.isSelected()) {
            hospitalChoice.setDisable(false);
        } else {
            hospitalChoice.setDisable(true);
        }
    }

    public void goBackAction(ActionEvent actionEvent) {
        StartPageController.startPageStage.hide();
        Main.mainLogicStage.show();
    }
}
