package ba.unsa.etf.rpr.projekat.DAO;

import ba.unsa.etf.rpr.projekat.Models.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import static ba.unsa.etf.rpr.projekat.Main.*;

public class UserDAO {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ObservableList<User> users = FXCollections.observableArrayList();
    private SimpleObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private int currentId = 1;
    AppointmentDAO appDAO;

    public UserDAO() {
        File dbFile = new File("users.db");
        if (!dbFile.exists()) createBase();
    }

    private void createBase() {
        Statement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            //to erase table user completely, i think this will be useful
            try {
                statement = connection.createStatement();
                statement.execute("DROP TABLE user");
            } catch (SQLException throwables) {
            }
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS \"user\" (\n" +
                    "\t\"id\"\tINTEGER NOT NULL,\n" +
                    "\t\"first_name\"\tTEXT NOT NULL,\n" +
                    "\t\"last_name\"\tTEXT NOT NULL,\n" +
                    "\t\"email\"\tTEXT NOT NULL,\n" +
                    "\t\"phone_number\"\tTEXT NOT NULL,\n" +
                    "\t\"username\"\tTEXT NOT NULL,\n" +
                    "\t\"password\"\tTEXT NOT NULL,\n" +
                    "\t\"gender\"\tTEXT NOT NULL,\n" +
                    "\t\"birthdate\"\tTEXT NOT NULL,\n" +
                    "\t\"admin\"\tTEXT,\n" +
                    "\tPRIMARY KEY(\"id\")\n" +
                    ");");
            statement.execute("INSERT INTO user VALUES (1, 'Kanita', 'Dervić', 'kdervic@faks.com', '062/062-062', 'kdervic', 'test', 'F', '23-1-1999', 'admin');");
            statement.execute("INSERT INTO user VALUES (2, 'Sara', 'Sarić', 'ssaric@faks.com', '060/062-0362', 'ssaric', 'test', 'F', '10-10-2003', null);");
            statement.execute("INSERT INTO user VALUES (3, 'Test', 'Testic', 'ttestic@faks.com', '062/062-063', 'ttestic', 'test', 'M', '21-11-1998', null);");
            currentId = 4;
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
                preparedStatement = connection.prepareStatement("SELECT max(id) FROM user");
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                currentId = rs.getInt(1) + 1;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        users = FXCollections.observableArrayList(getAllUsers());
        currentUser.set(null);
    }

    private ArrayList<User> getAllUsers() {
        ArrayList<User> list = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("Select first_name, last_name, email, phone_number, username, password, gender, birthdate, id from user");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User u = new User(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
                u.setId(rs.getInt(9));
                list.add(u);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addUser(User u) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            u.setId(currentId);
            preparedStatement.setInt(1, currentId++);
            preparedStatement.setString(2, u.getFirstName());
            preparedStatement.setString(3, u.getLastName());
            preparedStatement.setString(4, u.getEmail());
            preparedStatement.setString(5, u.getPhoneNumber());
            preparedStatement.setString(6, u.getUserName());
            preparedStatement.setString(7, u.getPassword());
            preparedStatement.setString(8, u.getGender());
            preparedStatement.setString(9, u.getDateOfBirthString());

            if (u instanceof Doctor) {
                preparedStatement.setString(10, "admin");
            } else {
                preparedStatement.setString(10, null);
            }

            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public ObservableList<User> getDoctorUsers() {
        ObservableList<User> doctors = FXCollections.observableArrayList();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("Select first_name, last_name, email, phone_number, username, password, gender, birthdate, id from user WHERE admin= ?");
            preparedStatement.setString(1, "admin");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Doctor d = new Doctor(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
                d.setId(rs.getInt(9));
                doctors.add(d);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return doctors;
    }

    public boolean checkIfDoctor(User u) {
        boolean isDoctor = false;
        int id = u.getId();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("SELECT admin FROM user WHERE id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.getString(1) == null) return false;
            isDoctor = resultSet.getString(1).equals("admin");
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isDoctor;
    }


    public User findUserById(int userId) {
        User u = new User();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("Select first_name, last_name, email, phone_number, username, password, gender, birthdate, id from user WHERE id= ?");
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                u = new User(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
                u.setId(rs.getInt(9));
            }
            connection.close();
        } catch (SQLException throwables) {
            return null;
        }
        return u;
    }

    public ObservableList<Appointment> getAppointmentsForDoctor(int id) {
        ObservableList<Appointment> appointmentsForDoctor = FXCollections.observableArrayList();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("SELECT appointment_id FROM appointment WHERE doctor_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int aId = rs.getInt(1);
                Appointment a = appointmentDAO.getAppointment(aId);
                appointmentsForDoctor.add(a);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsForDoctor;
    }

    public void removeUser(int id) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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

    public ObservableList<Appointment> getAppointmentsForPatient(int id) {
        ObservableList<Appointment> appointmentsForPatient = FXCollections.observableArrayList();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement = connection.prepareStatement("SELECT appointment_id FROM appointment WHERE patient_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int aId = rs.getInt(1);
                Appointment appointment = appointmentDAO.getAppointment(aId);
                appointmentsForPatient.add(appointment);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsForPatient;
    }

}
