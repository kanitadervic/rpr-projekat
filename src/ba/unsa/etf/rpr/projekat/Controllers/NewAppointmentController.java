package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.Models.*;
import ba.unsa.etf.rpr.projekat.Utilities.IllegalDateException;
import ba.unsa.etf.rpr.projekat.Utilities.InvalidDoctorChoice;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

import static ba.unsa.etf.rpr.projekat.Main.*;
import static java.lang.Character.isLetter;
import static java.lang.Character.isWhitespace;

public class NewAppointmentController {
    public ListView listViewDiseases;
    public TextField fldSearch;
    public Button btnAdd;
    public Button btnSearch;
    public Button btnCancel;
    public DatePicker appointmentDate;
    public ChoiceBox cbDoctorChoice;
    private Patient patient;
    public Text txtDisease;


    public NewAppointmentController(Patient patient) {
        this.patient = patient;
        this.patient.setId(patient.getId());
    }

    private ArrayList<String> getResults(int startIndex, String result) {
        ArrayList<String> bolesti = new ArrayList<>();
        StringBuilder slovo = new StringBuilder();
        for (int j = startIndex; j < result.length(); j++) {
            if (isLetter(result.charAt(j)) || isWhitespace(result.charAt(j)) || result.charAt(j) == '-') {
                slovo.append(result.charAt(j));
            } else if (result.charAt(j) == ']' && result.charAt(j + 1) == ',') {
                if (!bolesti.contains(String.valueOf(slovo))) {
                    bolesti.add(String.valueOf(slovo));
                }
                slovo = new StringBuilder();
            } else if (result.charAt(j) == ']' && result.charAt(j + 1) == ']') {
                break;
            }
        }
        return bolesti;
    }

    @FXML
    public void initialize() {
        appointmentDate.setValue(LocalDate.now());
        ObservableList<Doctor> doctors = userDAO.getDoctorUsers();
        cbDoctorChoice.setItems(doctors);

        cbDoctorChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (isDoctorValid(newVal)) {
                cbDoctorChoice.getStyleClass().removeAll("incorrectField");
                cbDoctorChoice.getStyleClass().add("correctField");
            } else {
                cbDoctorChoice.getStyleClass().removeAll("correctField");
                cbDoctorChoice.getStyleClass().add("incorrectField");
            }
        });

        appointmentDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (isDateValid(appointmentDate.getValue())) {
                appointmentDate.getStyleClass().removeAll("incorrectField");
                appointmentDate.getStyleClass().add("correctField");
            } else {
                appointmentDate.getStyleClass().removeAll("correctField");
                appointmentDate.getStyleClass().add("incorrectField");
            }
        });

        listViewDiseases.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (listViewDiseases.getSelectionModel().getSelectedItem() != null) {
                txtDisease.setText(newVal.toString());
            }
        });
    }

    private boolean checkDoctorChoice() {
        return isDoctorValid(cbDoctorChoice.getSelectionModel().getSelectedItem());
    }

    private boolean checkAppointmentDate() {
        return isDateValid(appointmentDate.getValue());
    }


    private boolean isDoctorValid(Object selectedDoctor) {
        Doctor doctor = (Doctor) selectedDoctor;
        final boolean[] takenDate = {false};
        if(doctor == null) {
            try {
                throw new InvalidDoctorChoice(resourceBundle.getString("invalid.choice"));
            } catch (InvalidDoctorChoice e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return false;
            }
        }
        ObservableList<Doctor> doctors = userDAO.getDoctorUsers();
        LocalDate localDate = appointmentDate.valueProperty().get();
        for(User u: doctors){
            if(u.equals(doctor)){
                doctor.setId(doctor.getId());
                break;
            }
        }
        ObservableList<Appointment> appointments = userDAO.getAppointmentsForDoctor(doctor.getId());
        appointments.forEach(appointment -> {
            if(appointment.getAppointmentDate().equals(localDate)) takenDate[0] = true;
        });
        return(!takenDate[0]);
    }

    private boolean isDateValid(LocalDate value) {
        LocalDate localDate = LocalDate.now();
        if (value == null || value.isBefore(localDate)) {
            try {
                throw new IllegalDateException(resourceBundle.getString("invalid.date"));
            } catch (IllegalDateException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    public void addAction(ActionEvent actionEvent) {
        if (checkAppointmentDate() &&
                checkDoctorChoice() &&
                listViewDiseases.getSelectionModel().getSelectedItem() != null) {
            Doctor doctor = (Doctor) cbDoctorChoice.getSelectionModel().getSelectedItem();
            LocalDate localDate = appointmentDate.getValue();
            Disease disease = new Disease(listViewDiseases.getSelectionModel().getSelectedItem().toString());
            if (diseaseDAO.getIdByName(disease.getName()) == 0)
                diseaseDAO.addDisease(disease, this.patient.getId());
            else
                disease.setId(diseaseDAO.getIdByName(disease.getName()));
            Appointment toAdd = new Appointment(doctor, this.patient, localDate, disease);
            this.patient.addDisease(disease);
            appointmentDAO.addAppointment(toAdd);
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
    }

    public void goBackAction(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public void searchAction(ActionEvent actionEvent) {
        listViewDiseases.getItems().clear();
        String url = "https://clinicaltables.nlm.nih.gov/api/conditions/v3/search?";
        String parameter = "terms=" + fldSearch.getText();
        String result = executeGetMethod(url, parameter);
        int r = result.indexOf("[[") + 1;
        Image loadingImage = new Image("images/loading.gif");
        ImageView loading = new ImageView(loadingImage);
        loading.setFitWidth(15);
        loading.setFitHeight(15);
        ArrayList<String> rez = getResults(r, result);
        new Thread(() -> {
            for (int i = 0; i < rez.size(); i++) {
                btnSearch.setOnMouseClicked((e) -> {
                    Platform.runLater(() -> {
                        btnSearch.setGraphic(loading);
                    });
                });
                Image checkImage = new Image("images/check.png");
                ImageView check = new ImageView(checkImage);
                check.setFitHeight(15);
                check.setFitWidth(15);

                int finalI = i;
                Platform.runLater(() -> {
                    listViewDiseases.getItems().add(rez.get(finalI));
                });
                Platform.runLater(() -> {
                    btnSearch.setGraphic(check);
                });
            }
        }).start();
    }

    private static String executeGetMethod(String urlLink, String urlParameters) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
