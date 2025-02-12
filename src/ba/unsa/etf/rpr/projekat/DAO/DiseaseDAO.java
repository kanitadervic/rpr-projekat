package ba.unsa.etf.rpr.projekat.DAO;

import ba.unsa.etf.rpr.projekat.Models.Disease;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import static ba.unsa.etf.rpr.projekat.Main.*;

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
            }
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS \"disease\" (\n" +
                    "\t\"disease_id\"\tINTEGER NOT NULL,\n" +
                    "\t\"patient_id\"\tINTEGER NOT NULL,\n" +
                    "\t\"disease_name\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"disease_id\")\n" +
                    "\tFOREIGN KEY(\"patient_id\") REFERENCES user\n" +
                    ");");
            statement.execute("INSERT INTO disease VALUES (1, 2, 'Depression');");
            statement.execute("INSERT INTO disease VALUES (2, 2, 'Common cold');");
            statement.execute("INSERT INTO disease VALUES (3, 3, 'Back pain');");

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
//        File dbFile = new File("users.db");
//        if (!dbFile.exists()) createBase();
//        else {
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

//        }
        diseases = FXCollections.observableArrayList(getAllDiseases());
    }

    public ArrayList<Disease> getAllDiseases() {
        ArrayList<Disease> list = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("Select * from disease");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int diseaseId = rs.getInt(1);
                String diseaseName = rs.getString(3);
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


    public Disease findDiseaseById(int diseaseId) {
        Disease disease = new Disease();
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("SELECT * FROM DISEASE WHERE disease_id=?");
            preparedStatement.setInt(1,diseaseId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                String diseaseName = rs.getString(3);
                if(diseaseName == null) {
                }
                disease.setName(diseaseName);
                disease.setId(diseaseId);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return disease;
    }

    public int getIdByName(String name) {
        int id = 0;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("SELECT disease_id FROM disease WHERE disease_name = ?");
            preparedStatement.setString(1,name);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                id = rs.getInt(1);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    public void addDisease(Disease disease, int patientId) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("INSERT INTO disease VALUES (?, ?, ?);");
            disease.setId(currentId);
            preparedStatement.setInt(1, currentId++);
            preparedStatement.setInt(2, patientId);
            preparedStatement.setString(3, disease.getName());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        diseases.add(disease);
    }
    //TODO
    public ArrayList<Disease> getDiseasesForPatient(int id) {
        ArrayList<Disease> diseases = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("SELECT disease_id, disease_name FROM disease WHERE patient_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int dId = rs.getInt(1);
                String name = rs.getString(2);
                Disease disease = new Disease(name);
                disease.setId(dId);
                diseases.add(disease);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return diseases;
    }

}
