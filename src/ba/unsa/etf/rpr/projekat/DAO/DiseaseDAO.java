package ba.unsa.etf.rpr.projekat.DAO;

import ba.unsa.etf.rpr.projekat.Models.Appointment;
import ba.unsa.etf.rpr.projekat.Models.Disease;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import static ba.unsa.etf.rpr.projekat.Main.diseaseDAO;
import static ba.unsa.etf.rpr.projekat.Main.userDAO;

public class DiseaseDAO {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ObservableList<Disease> diseases = FXCollections.observableArrayList();
    private int currentId = 1;

    public DiseaseDAO() {
        createBase();
    }

    private void createBase() {
        Statement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            //to erase table appointment completely, i think this will be useful
            try {
                statement = connection.createStatement();
                statement.execute("DROP TABLE disease");
            } catch (SQLException throwables) {
                System.out.println("Table already exists");
            }
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS \"disease\" (\n" +
                    "\t\"disease_id\"\tINTEGER NOT NULL,\n" +
                    "\t\"patient_id\"\tINTEGER NOT NULL,\n" +
                    "\t\"disease_name\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"disease_id\")\n" +
                    ");");
            statement.execute("INSERT INTO disease VALUES (1, 2, 'Depression');");
            statement.execute("INSERT INTO disease VALUES (2, 2, 'Common cold');");
            statement.execute("INSERT INTO disease VALUES (3, 2, 'Back pain');");
            currentId = 4;
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeInstance() {
        if (diseaseDAO != null) {
            try {
                diseaseDAO.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        diseaseDAO = null;
    }

    public static DiseaseDAO getInstance() {
        if (diseaseDAO == null) diseaseDAO = new DiseaseDAO();
        return diseaseDAO;
    }

    public void importData() {
        File dbFile = new File("users.db");
        if (!dbFile.exists()) createBase();
        else {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:users.db");
                preparedStatement = connection.prepareStatement("SELECT max(disease_id) FROM disease");
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                currentId = rs.getInt(1) + 1;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        diseases = FXCollections.observableArrayList(getAllDiseases());
    }

    private ArrayList<Disease> getAllDiseases() {
        ArrayList<Disease> list = new ArrayList<>();
        ObservableList<User> doctors = userDAO.getDoctorUsers();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("Select * from disease");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int diseaseId = rs.getInt(1);
                int patientId = rs.getInt(2);
                String diseaseName = rs.getString(3);
                User patient = userDAO.findUserById(patientId);
                Disease disease = new Disease(diseaseName);
                disease.setId(diseaseId);
                list.add(disease);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<Disease> getDiseases() {
        return diseases;
    }


}
