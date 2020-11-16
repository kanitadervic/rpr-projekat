package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAO.UserDAO;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.User;
import ba.unsa.etf.rpr.projekat.Utilities.IllegalDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDAOTest {
    private UserDAO dao = UserDAO.getInstance();

    @BeforeEach
//    public void resetujBazu() throws SQLException {
//        dao.resetBase();
//    }

    @Test
    void regenerateFile() {
        UserDAO.removeInstance();
        File dbfile = new File("users.db");
        dbfile.delete();
        this.dao = UserDAO.getInstance();
        ArrayList<User> users = dao.getAllUsers();
        assertEquals("Admin", users.get(0).getFirstName());
        assertEquals("ttestic@faks.com", users.get(2).getEmail());
    }


    @Test
    void checkIfDoctorTest() throws IllegalDateException {
        LocalDate localDate = LocalDate.now();
        User user = new Doctor("Neko", "Nekic", "neko@neko.com", "333/444-222", "pass1", "M", localDate);
        this.dao.addUser(user);
        assertTrue(dao.checkIfDoctor(user));
    }

    @Test
    void findUserByIdTest() throws IllegalDateException {
        ArrayList<User> users = dao.getAllUsers();
        assertEquals(users.get(0).getId(), 1);
        assertEquals(users.get(2).getId(), 3);
    }
}
