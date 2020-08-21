package ba.unsa.etf.rpr.projekat.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Character.isLetter;
import static java.lang.Character.isWhitespace;

public class DiseaseController {
    public Button btnChoose;
    public Button btnSearch;
    public Button btnCancel;
    public ListView listViewDiseases;
    public TextField fldSearch;
    private ArrayList<String> bolesti = new ArrayList<>();

    public ArrayList<String> getResults(int startIndex, String result) {
        StringBuilder slovo = new StringBuilder();
        for (int j = startIndex; j < result.length(); j++) {
            if (isLetter(result.charAt(j)) || isWhitespace(result.charAt(j)) || result.charAt(j) == '-') {
                slovo.append(result.charAt(j));
            } else if (result.charAt(j) == ']' && result.charAt(j + 1) == ',') {
                if (!bolesti.contains(String.valueOf(slovo))) {
                    listViewDiseases.getItems().add(String.valueOf(slovo));
                    bolesti.add(String.valueOf(slovo));
                }
                slovo = new StringBuilder();
            } else if (result.charAt(j) == ']' && result.charAt(j + 1) == ']') {
                break;
            }
        }
        return bolesti;
    }

    public void searchAction(ActionEvent actionEvent) {
        String url = "https://clinicaltables.nlm.nih.gov/api/conditions/v3/search?";
        String parameter = "terms=" + fldSearch.getText();
        String result = executeGetMethod(url, parameter);
        int r = result.indexOf("[[") + 1;

        Image slika = new Image("images/loading.gif");
        ImageView loading = new ImageView(slika);
        loading.setFitWidth(20);
        loading.setFitHeight(20);
        ArrayList<String> rez = getResults(r, result);
        new Thread(() -> {
            for (int i = 0; i < rez.size(); i++) {
                btnSearch.setOnMouseClicked((e) -> {
                    Platform.runLater(() -> {
                        btnSearch.setGraphic(loading);
                    });
                });
                btnChoose.setOnMouseClicked((e) -> {
                    if (listViewDiseases.getSelectionModel().getSelectedItem() == null) {
                        System.out.println("nish");
                    } else {
                        System.out.println(listViewDiseases.getSelectionModel().getSelectedItem().toString());
                    }
                });
                Image image = new Image("images/check.png");
                ImageView check = new ImageView(image);
                check.setFitHeight(20);
                check.setFitWidth(20);

                int finalI = i;
                Platform.runLater(() -> {
                    listViewDiseases.getItems().add(bolesti.get(finalI));
                });
                Platform.runLater(() -> {
                    btnSearch.setGraphic(check);
                });
            }
        }).start();
    }

    public void searchClicked(ActionEvent actionEvent) {
        String url = "https://clinicaltables.nlm.nih.gov/api/conditions/v3/search?";
        String parameter = "terms=depression";
        String result = executeGetMethod(url, parameter);
        System.out.println(result);
        int r = result.indexOf("[[");
        r++;
        System.out.println(r);
        ArrayList<String> bolesti = new ArrayList<>();
        StringBuilder slovo = new StringBuilder();
        for (int i = r; i < result.length(); i++) {
            if (isLetter(result.charAt(i)) || isWhitespace(result.charAt(i)) || result.charAt(i) == '-') {
                slovo.append(result.charAt(i));
            } else if (result.charAt(i) == ']' && result.charAt(i + 1) == ',') {
                bolesti.add(String.valueOf(slovo));
                slovo = new StringBuilder();
            } else if (result.charAt(i) == ']' && result.charAt(i + 1) == ']') {
                break;
            }

        }
        bolesti.stream().forEach(System.out::println);
    }

    public static String executeGetMethod(String urlLink, String urlParameters) {
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
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
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


    public void chooseAction(ActionEvent actionEvent) {
    }

    public void cancelAction(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
}
