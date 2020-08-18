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

import static ba.unsa.etf.rpr.projekat.Main.appointmentDAO;
import static ba.unsa.etf.rpr.projekat.Main.userDAO;

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
                throwables.printStackTrace();
            }
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS \"appointment\" (\n" +
                    "\t\"appointmentId\"\tINTEGER NOT NULL,\n" +
                    "\t\"doctorId\"\tINTEGER NOT NULL,\n" +
                    "\t\"patientId\"\tINTEGER NOT NULL,\n" +
                    "\t\"appointmentDate\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"appointmentId\")\n" +
                    ");");
            statement.execute("INSERT INTO appointment VALUES (1, 1, 2, '3-9-2021');");
            statement.execute("INSERT INTO appointment VALUES (2, 1, 3, '9-9-2021');");
            statement.execute("INSERT INTO appointment VALUES (3, 1, 2, '23-1-2021');");
            currentId = 2;
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void importData() {
        File dbFile = new File("users.db");
        if (!dbFile.exists()) createBase();
        else {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:users.db");
                preparedStatement = connection.prepareStatement("SELECT max(appointmentId) FROM appointment");
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                currentId = rs.getInt(1) + 1;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        appointments = FXCollections.observableArrayList(getAllAppointments());
    }

    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> list = new ArrayList<>();
        ObservableList<User> doctors = userDAO.getDoctorUsers();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("Select * from appointment");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt(1);
                int doctorId = rs.getInt(2);
                int patientId = rs.getInt(3);
                String appointmentDate = rs.getString(4);
                User doctor = userDAO.findUserById(doctorId);
                User patient = userDAO.findUserById(patientId);
                Appointment appointment = new Appointment(doctor, patient, appointmentDate);
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
            preparedStatement = connection.prepareStatement("INSERT INTO appointment VALUES (?, ?, ?, ?);");
            appointment.setId(currentId);
            preparedStatement.setInt(1, currentId++);
            preparedStatement.setInt(2, appointment.getDoctor().getId());
            preparedStatement.setInt(3, appointment.getPatient().getId());
            preparedStatement.setString(4, appointment.getAppointmentDateString());
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
            preparedStatement = connection.prepareStatement("DELETE FROM appointment WHERE appointmentId = ?;");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
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
            for (Appointment appointment : userDAO.getAppointmentsForDoctor(doctorId)) {
                result += appointment.getPatientFirstName() + " " + appointment.getPatientLastName() + ", " + appointment.getAppointmentDateString() + "\n";
            }
            fileWriter.write(result);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAppointmentDate(int appointmentId, String toString, int doctorId) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("UPDATE appointment SET doctorId = ?, appointmentDate = ? WHERE appointmentId = ?");
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, toString);
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
            preparedStatement = connection.prepareStatement("SELECT * FROM appointment WHERE appointmentId = ?");
            preparedStatement.setInt(1, aId);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            User doctor = userDAO.findUserById(rs.getInt(2));
            User patient = userDAO.findUserById(rs.getInt(3));
            String dateString = rs.getString(4);
            DateClass date = new DateClass(dateString);
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setAppointmentDate(date);
            appointment = new Appointment(doctor, patient, dateString);
            appointment.setId(rs.getInt(1));
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointment;
    }
}
