package ba.unsa.etf.rpr.projekat.DAO;

import ba.unsa.etf.rpr.projekat.Models.*;
import ba.unsa.etf.rpr.projekat.Utilities.IllegalDateException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static ba.unsa.etf.rpr.projekat.Main.*;

public class UserDAO {
    private Connection connection;
    private PreparedStatement getAllUsersQuery, addUserQuery, getAdminQuery, getUserByIdQuery,
            appointmentsForDoctorQuery, appointmentsForPatientQuery;
    private ObservableList<User> users = FXCollections.observableArrayList();
    private SimpleObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private int currentId = 4;
    AppointmentDAO appDAO;

    public UserDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            resetBase();
        } catch (SQLException throwables) {
        }
        try{
            addUserQuery = connection.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
        } catch (SQLException throwables) {
            regenerateBase();
            try {
                addUserQuery = connection.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try{
            getAllUsersQuery = connection.prepareStatement("Select first_name, last_name, email, phone_number, password, gender, birthdate, id from user");
            getAdminQuery = connection.prepareStatement("Select first_name, last_name, email, phone_number, password, gender, birthdate, id from user WHERE admin= ?");
            getUserByIdQuery = connection.prepareStatement("Select first_name, last_name, email, phone_number, password, gender, birthdate, id, admin from user WHERE id= ?");
            appointmentsForPatientQuery = connection.prepareStatement("SELECT appointment_id FROM appointment WHERE patient_id = ?");
            appointmentsForDoctorQuery = connection.prepareStatement("SELECT appointment_id FROM appointment WHERE doctor_id = ?");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

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


    public ArrayList<User> getAllUsers() {
        ArrayList<User> list = new ArrayList<>();
        try {
            ResultSet rs = getAllUsersQuery.executeQuery();
            while (rs.next()) {
                User u = new User(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                u.setId(rs.getInt(8));
                list.add(u);
            }
        } catch (SQLException | IllegalDateException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addUser(User u) {
        try {
            u.setId(currentId);
            addUserQuery.setInt(1, currentId++);
            addUserQuery.setString(2, u.getFirstName());
            addUserQuery.setString(3, u.getLastName());
            addUserQuery.setString(4, u.getEmail());
            addUserQuery.setString(5, u.getPhoneNumber());
            addUserQuery.setString(6, u.getPassword());
            addUserQuery.setString(7, u.getGender());
            addUserQuery.setString(8, u.getDateOfBirthString());
            if (u instanceof Doctor) {
                addUserQuery.setString(9, "admin");
            } else {
                addUserQuery.setString(9, null);
            }

            addUserQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public ObservableList<Doctor> getDoctorUsers() {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        try {
            getAdminQuery.setString(1, "admin");
            ResultSet rs = getAdminQuery.executeQuery();
            while (rs.next()) {
                Doctor d = new Doctor(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                d.setId(rs.getInt(8));
                doctors.add(d);
            }
        } catch (SQLException | IllegalDateException throwables) {
            throwables.printStackTrace();
        }
        return doctors;
    }

    public boolean checkIfDoctor(User u) {
        Doctor d = findDoctorById(u.getId());
        if(d !=null) return true;
        return false;
    }


    public Doctor findDoctorById(int doctorId) {
        Doctor u = new Doctor();
        try {
            getUserByIdQuery.setInt(1, doctorId);
            ResultSet rs = getUserByIdQuery.executeQuery();
            while (rs.next()) {
                u = new Doctor(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                u.setId(rs.getInt(8));
            }
        } catch (SQLException | IllegalDateException throwables) {
            System.out.println("No user was found");
        }
        return u;
    }

    public Patient findPatientById(int patientId) {
        Patient u = new Patient();
        try {
            getUserByIdQuery.setInt(1, patientId);
            ResultSet rs = getUserByIdQuery.executeQuery();
            while (rs.next()) {
                u = new Patient(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                u.setId(rs.getInt(8));
            }
        } catch (SQLException | IllegalDateException throwables) {
            throwables.printStackTrace();
        }

        return u;
    }

    public void removeInstance() {
        if (userDAO != null) {
            try {
                userDAO.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        userDAO = null;
    }

    public static UserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }
    public ObservableList<Appointment> getAppointmentsForDoctor(int id) {
        ObservableList<Appointment> appointmentsForDoctor = FXCollections.observableArrayList();
        try {
//            appointmentsForDoctorQuery = connection.prepareStatement("SELECT appointment_id FROM appointment WHERE doctor_id = ?");
            appointmentsForDoctorQuery.setInt(1, id);
            ResultSet rs = appointmentsForDoctorQuery.executeQuery();
            while (rs.next()) {
                int aId = rs.getInt(1);
                Appointment a = appointmentDAO.getAppointment(aId);
                appointmentsForDoctor.add(a);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsForDoctor;
    }



    public ObservableList<Appointment> getAppointmentsForPatient(int id) {
        ObservableList<Appointment> appointmentsForPatient = FXCollections.observableArrayList();
        try {
            appointmentsForPatientQuery.setInt(1, id);
            ResultSet rs = appointmentsForPatientQuery.executeQuery();
            while (rs.next()) {
                int aId = rs.getInt(1);
                Appointment appointment = appointmentDAO.getAppointment(aId);
                appointmentsForPatient.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsForPatient;
    }

    public Connection getConnection() {
        return connection;
    }
}
