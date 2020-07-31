package ba;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class DoctorLogInController {
    public Button btnDoctorLogIn;
    public Button btnGoBack;
    public TextField fldUsername;
    public PasswordField fldPassword;

    public void doctorLogInAction(ActionEvent actionEvent){

    }

    public void goBackAction(ActionEvent actionEvent){
        StartPageController.startPageStage.hide();
        Main.mainLogicStage.show();
    }
}
