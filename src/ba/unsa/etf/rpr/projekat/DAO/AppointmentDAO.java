package ba.unsa.etf.rpr.projekat.DAO;

import ba.unsa.etf.rpr.projekat.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.print.Doc;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

import static ba.unsa.etf.rpr.projekat.Main.*;

public class AppointmentDAO {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private int currentId = 1;

    public AppointmentDAO() {
//        File dbFile = new File("users.db");
//        if(!dbFile.exists()) createBase();
        createBase();
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

    private void createBase() {
        Statement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            //to erase table appointment completely, i think this will be useful
            try {
                statement = connection.createStatement();
                statement.execute("DROP TABLE appointment");
            } catch (SQLException throwables) {
            }
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS \"appointment\" (\n" +
                    "\t\"appointment_id\"\tINTEGER NOT NULL,\n" +
                    "\t\"doctor_id\"\tINTEGER NOT NULL,\n" +
                    "\t\"patient_id\"\tINTEGER NOT NULL,\n" +
                    "\t\"appointment_date\"\tTEXT NOT NULL,\n" +
                    "\t\"disease_id\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"appointment_id\")\n" +
                    ");");
            statement.execute("INSERT INTO appointment VALUES (1, 1, 2, '3-9-2021', 1);");
            statement.execute("INSERT INTO appointment VALUES (2, 1, 3, '9-9-2021', 2);");
            statement.execute("INSERT INTO appointment VALUES (3, 1, 2, '23-1-2021', 3);");
            currentId = 4;
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void importData() {
//        File dbFile = new File("users.db");
//        if (!dbFile.exists()) createBase();
//        else {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:users.db");
                preparedStatement = connection.prepareStatement("SELECT max(appointment_id) FROM appointment");
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                currentId = rs.getInt(1) + 1;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

//        }
        appointments = FXCollections.observableArrayList(getAllAppointments());
    }

    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> list = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("Select * from appointment");
            ResultSet rs = preparedStatement.executeQuery();
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
            connection.close();
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
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("INSERT INTO appointment VALUES (?, ?, ?, ?, ?);");
            appointment.setId(currentId);
            preparedStatement.setInt(1, currentId++);
            preparedStatement.setInt(2, appointment.getDoctor().getId());
            preparedStatement.setInt(3, appointment.getPatient().getId());
            preparedStatement.setString(4, appointment.getAppointmentDateString());
            preparedStatement.setInt(5, appointment.getDisease().getId());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        appointments.add(appointment);
    }

    public void removeAppointment(int id) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("DELETE FROM appointment WHERE appointment_id = ?;");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            connection.close();
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
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("UPDATE appointment SET doctor_id = ?, appointment_date = ? WHERE appointment_id = ?");
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            preparedStatement.setInt(3, appointmentId);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Appointment getAppointment(int aId) {
        Appointment appointment = new Appointment();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("SELECT * FROM appointment WHERE appointment_id = ?");
            preparedStatement.setInt(1, aId);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Doctor doctor = userDAO.findDoctorById(rs.getInt(2));
            Patient patient = userDAO.findPatientById(rs.getInt(3));
            String dateString = rs.getString(4);
            int diseaseId = rs.getInt(5);
            appointment.setAppointmentDate(dateString);
//            DateClass date = new DateClass(dateString);
            Disease disease = diseaseDAO.findDiseaseById(diseaseId);
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
//            appointment.setAppointmentDate(date);
            appointment = new Appointment(doctor, patient, dateString, disease);
            appointment.setId(rs.getInt(1));
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointment;
    }
}
