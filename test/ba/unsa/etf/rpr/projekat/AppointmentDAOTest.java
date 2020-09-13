package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAO.AppointmentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppointmentDAOTest {
    private AppointmentDAO appointmentDAO = AppointmentDAO.getInstance();

//    @BeforeEach
//    public void resetBase() throws SQLException {
//        appointmentDAO.importData();
//    }

    @Test
    void testGetAppointments() {
        appointmentDAO.removeInstance();
        appointmentDAO = new AppointmentDAO();
        assertEquals(3, appointmentDAO.getAllAppointments().size());
    }

    @Test
    void testRemoveAppointment() {
        assertEquals(3, appointmentDAO.getAllAppointments().size());
        appointmentDAO.removeAppointment(2);
        appointmentDAO.removeAppointment(3);
        assertEquals(1, appointmentDAO.getAllAppointments().size());
        assertNotNull(appointmentDAO.getAllAppointments().get(0));
    }

    @Test
    void testUpdateAppointmentDate() {
        appointmentDAO.updateAppointmentDate(1, "3-3-2051", 1);
        assertEquals(appointmentDAO.getAllAppointments().get(0).getAppointmentDateString(), "3-3-2051");
    }

    @Test
    void testGetAppointmentById() {
//        appointmentDAO.importData();
        appointmentDAO.removeInstance();
        appointmentDAO = new AppointmentDAO();
        assertEquals(appointmentDAO.getAllAppointments().get(2).getId(), 3);
    }


}
