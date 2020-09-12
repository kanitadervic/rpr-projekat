package ba.unsa.etf.rpr.projekat.DAO;

import ba.unsa.etf.rpr.projekat.Models.Disease;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static ba.unsa.etf.rpr.projekat.Main.diseaseDAO;

public class DiseaseDAO {
    private Connection connection;
    private PreparedStatement getDiseasesQuery, getAllDiseasesQuery, getDiseaseByNameQuery, addDiseaseQuery, diseaseForPatientQuery, getDiseaseByIdQuery;
    private ObservableList<Disease> diseases = FXCollections.observableArrayList();
    private int currentId = 4;

    public void resetBase() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM appointment");
        stmt.executeUpdate("DELETE FROM disease");
        stmt.executeUpdate("DELETE FROM user");
        regenerateBase();
    }

    public void regenerateBase() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream("users.db.sql"));
            String sql = "";
            while (scanner.hasNext()) {
                sql += scanner.nextLine();
                if (sql.charAt(sql.length() - 1) == ';') {
                    try {
                        Statement stmt = connection.createStatement();
                        stmt.execute(sql);
                        sql = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DiseaseDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            getDiseaseByIdQuery = connection.prepareStatement("SELECT * FROM DISEASE WHERE disease_id=?");
            getDiseaseByNameQuery = connection.prepareStatement("SELECT disease_id FROM disease WHERE disease_name = ?");
            addDiseaseQuery = connection.prepareStatement("INSERT INTO disease VALUES (?, ?, ?);");
            diseaseForPatientQuery = connection.prepareStatement("SELECT disease_id, disease_name FROM disease WHERE patient_id = ?");
            getAllDiseasesQuery = connection.prepareStatement("SELECT disease_id FROM disease");
        } catch (SQLException e) {
            e.printStackTrace();
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


    public ObservableList<Disease> getDiseases() {
        return diseases;
    }


    public Disease findDiseaseById(int diseaseId) {
        Disease disease = new Disease();
        try {
            getDiseaseByIdQuery.setInt(1, diseaseId);
            ResultSet rs = getDiseaseByIdQuery.executeQuery();
            while (rs.next()) {
                String diseaseName = rs.getString(3);
                if (diseaseName == null) {
                }
                disease.setName(diseaseName);
                disease.setId(diseaseId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return disease;
    }

    public ArrayList<Disease> getAllDiseases() {
        ArrayList<Disease> list = new ArrayList<>();
        try {
            ResultSet rs = getAllDiseasesQuery.executeQuery();
            while (rs.next()) {
                Disease d = findDiseaseById(rs.getInt(1));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.diseases.setAll(list);
        return list;
    }

    public int getIdByName(String name) {
        int id = 0;
        try {
            getDiseaseByNameQuery.setString(1, name);
            ResultSet rs = getDiseaseByNameQuery.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    public void addDisease(Disease disease, int patientId) {
        try {
            disease.setId(currentId);
            addDiseaseQuery.setInt(1, currentId++);
            addDiseaseQuery.setInt(2, patientId);
            addDiseaseQuery.setString(3, disease.getName());
            addDiseaseQuery.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        diseases.add(disease);
    }

    public ArrayList<Disease> getDiseasesForPatient(int id) {
        ArrayList<Disease> diseases = new ArrayList<>();
        try {
            diseaseForPatientQuery.setInt(1, id);
            ResultSet rs = diseaseForPatientQuery.executeQuery();
            while (rs.next()) {
                int dId = rs.getInt(1);
                String name = rs.getString(2);
                Disease disease = new Disease(name);
                disease.setId(dId);
                diseases.add(disease);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return diseases;
    }

}
