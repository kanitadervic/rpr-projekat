package ba.unsa.etf.rpr.projekat;
import ba.unsa.etf.rpr.projekat.Models.Doctor;
import ba.unsa.etf.rpr.projekat.Models.Patient;
import ba.unsa.etf.rpr.projekat.Models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class UserTest {
    @Test
    void constructorTest() throws IllegalArgumentException{
        LocalDate localDate = LocalDate.now();
        User user = new User("Neko", "Nekic", "neko@neko.com", "333/444-222", "pass1", "M", localDate);
        assertAll(
                () -> assertEquals("Neko", user.getFirstName()),
                () -> assertEquals("Nekic", user.getLastName()),
                () -> assertEquals("333/444-222", user.getPhoneNumber()),
                () -> assertEquals("neko@neko.com", user.getEmail()),
                () -> assertEquals("pass1", user.getPassword()),
                () -> assertEquals("M", user.getGender()),
                () -> assertEquals(localDate, user.getDateOfBirth())
        );
    }

    @Test
    void constructorTestThrowsException() throws IllegalArgumentException {
        LocalDate localDate = LocalDate.now();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new User("Neko", "Nekic", null, "333/444-222", "pass1", "M", localDate));
        assertEquals("Parameters cannot be null!", exception.getMessage());
    }

    @Test
    void constructorTestThrowsException2() throws IllegalArgumentException {
        LocalDate localDate = LocalDate.now();
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new User("Neko", null, "email@email.com", "333/444-222", "pass1", "M", localDate));
        assertEquals("Parameters cannot be null!", exception.getMessage());
    }

    @Test
    void testingSetters() throws IllegalArgumentException {
        LocalDate localDate = LocalDate.now();
        User user = new User("Neko", "Nekic", "neko@neko.com", "333/444-222", "pass1", "M", localDate);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> user.setEmail(""));
        assertEquals("Argument cannot be empty!", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class,
                () -> user.setPhoneNumber(""));
        assertEquals("Argument cannot be empty!", exception.getMessage());
        user.setFirstName("Drake");
        assertEquals(user.getFirstName(), "Drake");
    }

    @Test
    void testingDoctorUser() {
        LocalDate localDate = LocalDate.now();
        User user = new Doctor("Neko", "Nekic", "neko@neko.com", "333/444-222", "pass1", "M", localDate);
        assert(user instanceof Doctor);
    }

    @Test
    void testingDoctorUserException() throws IllegalArgumentException {
        LocalDate localDate = LocalDate.now();
        User user = new Doctor("Neko", "Nekic", "neko@neko.com", "333/444-222", "pass1", "M", localDate);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> user.setPhoneNumber(""));
        assertEquals("Argument cannot be empty!", exception.getMessage());
    }

    @Test
    void testingPatientUser() {
        LocalDate localDate = LocalDate.now();
        User user = new Patient("Neko", "Nekic", "neko@neko.com", "333/444-222", "pass1", "M", localDate);
        assert(user instanceof Patient);
    }

    @Test
    void testingPatientUserException() throws IllegalArgumentException {
        LocalDate localDate = LocalDate.now();
        User user = new Patient("Neko", "Nekic", "neko@neko.com", "333/444-222", "pass1", "M", localDate);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> user.setLastName(""));
        assertEquals("Argument cannot be empty!", exception.getMessage());
    }
}
