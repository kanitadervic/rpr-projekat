package ba.unsa.etf.rpr.projekat.DAO;

import ba.unsa.etf.rpr.projekat.Models.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class UserDAO {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ObservableList<User> users = FXCollections.observableArrayList();
    private SimpleObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private int currentId = 1;

    public UserDAO(){
        File dbFile = new File("users.db");
        if(!dbFile.exists()) createBase();
    }

    private void createBase() {
        Statement statement = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            statement = connection.createStatement();
            statement.execute("CREATE TABLE \"user\" (\n" +
                    "\t\"id\"\tINTEGER NOT NULL,\n" +
                    "\t\"firstName\"\tTEXT NOT NULL,\n" +
                    "\t\"lastName\"\tTEXT NOT NULL,\n" +
                    "\t\"email\"\tTEXT NOT NULL,\n" +
                    "\t\"phoneNumber\"\tTEXT NOT NULL,\n" +
                    "\t\"username\"\tTEXT NOT NULL,\n" +
                    "\t\"password\"\tTEXT NOT NULL,\n" +
                    "\t\"gender\"\tTEXT NOT NULL,\n" +
                    "\t\"birthdate\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"id\")\n" +
                    ");");
            statement.execute("INSERT INTO user VALUES (1, 'Kanita', 'Dervić', 'kdervic', '32343', 'kdervic', 'test', 'F', '23-1-1999');");
            currentId = 2;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void importData() {
        File dbFile  =new File("users.db");
        if(!dbFile.exists()) createBase();
        else{
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:users.db");
                preparedStatement = connection.prepareStatement("SELECT max(id) FROM user");
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                currentId = rs.getInt(1) +1;
                connection.close();
            }
            catch (SQLException e) {
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
            preparedStatement = connection.prepareStatement("Select firstName, lastName, email, phoneNumber, username, password, gender, birthdate, id from user");
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
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            preparedStatement =connection.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            u.setId(currentId);
            preparedStatement.setInt(1,currentId++);
            preparedStatement.setString(2,u.getFirstName());
            preparedStatement.setString(3,u.getLastName());
            preparedStatement.setString(4,u.getEmail());
            preparedStatement.setString(5,u.getPhoneNumber());
            preparedStatement.setString(6,u.getUserName());
            preparedStatement.setString(7,u.getPassword());
            preparedStatement.setString(8,u.getGender());
            preparedStatement.setString(9,u.getDateOfBirthString());

            preparedStatement.executeUpdate();
            connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public ObservableList<User> getUsers() {
        return users;
    }
}
