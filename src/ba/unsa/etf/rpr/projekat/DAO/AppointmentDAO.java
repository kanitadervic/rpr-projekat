package ba.unsa.etf.rpr.projekat.DAO;

import ba.unsa.etf.rpr.projekat.Models.Appointment;
import ba.unsa.etf.rpr.projekat.Models.Disease;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static ba.unsa.etf.rpr.projekat.Main.*;

public class AppointmentDAO {
    private Connection connection;
    private PreparedStatement getAllAppointmentsQuery, addAppointmentQuery, deleteAppointmentQuery,
            updateAppointmentQuery, getAppointmentQuery;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
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

    public AppointmentDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            getAllAppointmentsQuery = connection.prepareStatement("SELECT * FROM appointment");
            addAppointmentQuery = connection.prepareStatement("INSERT INTO appointment VALUES (?, ?, ?, ?, ?)");
            deleteAppointmentQuery = connection.prepareStatement("DELETE FROM appointment WHERE appointment_id = ?");
            updateAppointmentQuery = connection.prepareStatement("UPDATE appointment SET doctor_id = ?, appointment_date = ? WHERE appointment_id = ?");
            getAppointmentQuery = connection.prepareStatement("SELECT * FROM appointment WHERE appointment_id = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeInstance() {
        if (appointmentDAO != null) {
            try {
                appointmentDAO.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        appointmentDAO = null;
    }

    public static AppointmentDAO getInstance() {
        if (appointmentDAO == null) appointmentDAO = new AppointmentDAO();
        return appointmentDAO;
    }


    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> list = new ArrayList<>();
        try {
            ResultSet rs = getAllAppointmentsQuery.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt(1);
                int doctorId = rs.getInt(2);
                int patientId = rs.getInt(3);
                String appointmentDate = rs.getString(4);
                int diseaseId = rs.getInt(5);
                Doctor doctor = userDAO.findDoctorById(doctorId);
                Patient patient = userDAO.findPatientById(patientId);
                Disease disease = diseaseDAO.findDiseaseById(diseaseId);
                Appointment appointment = new Appointment(doctor, patient, appointmentDate, disease);
                appointment.setId(appointmentId);
                list.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        try {
            appointment.setId(currentId);
            addAppointmentQuery.setInt(1, currentId++);
            addAppointmentQuery.setInt(2, appointment.getDoctor().getId());
            addAppointmentQuery.setInt(3, appointment.getPatient().getId());
            addAppointmentQuery.setString(4, appointment.getAppointmentDateString());
            addAppointmentQuery.setInt(5, appointment.getDisease().getId());
            addAppointmentQuery.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        appointments.add(appointment);
    }

    public void removeAppointment(int id) {
        try {
            deleteAppointmentQuery.setInt(1, id);
            deleteAppointmentQuery.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void writeFileForDoctor(File selectedFile, int doctorId) {
        try {
            if (selectedFile == null) {
                return;
            }
            FileWriter fileWriter = new FileWriter(selectedFile);
            String result = "";
            ObservableList<Appointment> appointmentsForDoctor = userDAO.getAppointmentsForDoctor(doctorId);
            for (Appointment appointment : appointmentsForDoctor) {
                result += appointment.getPatientFirstName() + " " + appointment.getPatientLastName() + ", " + appointment.getAppointmentDateString() + "\n";
            }
            fileWriter.write(result);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAppointmentDate(int appointmentId, String appointmentDate, int doctorId) {
        try {
            updateAppointmentQuery.setInt(1, doctorId);
            updateAppointmentQuery.setString(2, appointmentDate);
            updateAppointmentQuery.setInt(3, appointmentId);
            updateAppointmentQuery.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Appointment getAppointment(int aId) {
        Appointment appointment = new Appointment();
        try {
            getAppointmentQuery.setInt(1, aId);
            ResultSet rs = getAppointmentQuery.executeQuery();
            if(!rs.next()) return null;
            Doctor doctor = userDAO.findDoctorById(rs.getInt(2));
            Patient patient = userDAO.findPatientById(rs.getInt(3));
            String dateString = rs.getString(4);
            int diseaseId = rs.getInt(5);
            appointment.setAppointmentDate(dateString);
            Disease disease = diseaseDAO.findDiseaseById(diseaseId);
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment = new Appointment(doctor, patient, dateString, disease);
            appointment.setId(rs.getInt(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointment;
    }
}
