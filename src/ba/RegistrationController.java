package ba;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class RegistrationController {
    public Button btnGoBack;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public Button btnConfirmRegistration;
    public PasswordField fldPassword2;
    public TextField fldEmail;
    public DatePicker birthdayPicker;
    public CheckBox isDoctor;
    public TextField fldCity;
    public ChoiceBox hospitalChoice;

    public void registrationAction(ActionEvent actionEvent){

    }

    public void goBackAction(ActionEvent actionEvent){
        StartPageController.startPageStage.hide();
        Main.mainLogicStage.show();
    }
}
