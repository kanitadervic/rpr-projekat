package ba.unsa.etf.rpr.projekat;
import ba.unsa.etf.rpr.projekat.Models.*;
import ba.unsa.etf.rpr.projekat.Utilities.IllegalDateException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class AppointmentTest {
    @Test
    void constructorTest() throws IllegalDateException {
        LocalDate localDate = LocalDate.now();
        LocalDate appointmentDate = LocalDate.of(2021, 3, 23);
        Doctor doctor = new Doctor("Doctor", "Doctory", "doc@someone.com", "123/444-222", "pass1", "M", localDate);
        Patient patient = new Patient("Patient", "Patienty", "pat@someone.com", "333/434-222", "pass1", "F", localDate);
        Disease disease = new Disease("Back pain");
        patient.addDisease(disease);
        Appointment appointment = new Appointment(doctor,patient,appointmentDate,disease);
        assertEquals(appointment.getDoctor().getFirstName(), "Doctor");
        assertEquals(appointment.getPatient().getDiseases().size(),1);
        assertEquals(appointment.getAppointmentDate(), appointmentDate);
    }

    @Test
    void constructorTestException() throws IllegalArgumentException, IllegalDateException {
        LocalDate localDate = LocalDate.now();

        Doctor doctor = new Doctor("Doctor", "Doctory", "doc@someone.com", "123/444-222", "pass1", "M", localDate);
        LocalDate appointmentDate = LocalDate.of(2021, 3, 23);
        Disease disease = new Disease("Back pain");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Appointment(doctor,null,appointmentDate,disease));
        assertEquals(exception.getMessage(),"Parameters cannot be null!");
    }

    @Test
    void testingSetters() throws IllegalArgumentException, IllegalDateException {
        LocalDate localDate = LocalDate.now();
        LocalDate appointmentDate = LocalDate.of(2021, 3, 23);
        Doctor doctor = new Doctor("Doctor", "Doctory", "doc@someone.com", "123/444-222", "pass1", "M", localDate);
        Patient patient = new Patient("Patient", "Patienty", "pat@someone.com", "333/434-222", "pass1", "F", localDate);
        Disease disease = new Disease("Back pain");
        patient.addDisease(disease);
        Appointment appointment = new Appointment(doctor,patient,appointmentDate,disease);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> appointment.setDoctor(null));
        assertEquals("Argument cannot be null!", exception.getMessage());
        appointment.setAppointmentDate(localDate);
        assertEquals(appointment.getAppointmentDate(), localDate);
        appointmentDate = LocalDate.of(2000, 3, 3);
        LocalDate finalAppointmentDate = appointmentDate;
        Throwable throwable = assertThrows(IllegalDateException.class,
                () -> appointment.setAppointmentDate((finalAppointmentDate)));
        assertEquals(throwable.getMessage(),"Appointment cannot be in the past");
    }

    @Test
    void testingEqualsMethod(){
        LocalDate l1 = LocalDate.of(2010, 3, 23);
        LocalDate l2;
        l2 = LocalDate.now();
        assertFalse(l2.equals(l1));
        l1 = LocalDate.now();
        assertTrue(l1.equals(l2));
    }

    @Test
    void testingToStringMethod() throws IllegalDateException {
        LocalDate localDate = LocalDate.now();
        LocalDate appointmentDate = LocalDate.of(2021, 3, 23);
        Doctor doctor = new Doctor("Doctor", "Doctory", "doc@someone.com", "123/444-222", "pass1", "M", localDate);
        Patient patient = new Patient("Patient", "Patienty", "pat@someone.com", "333/434-222", "pass1", "F", localDate);
        Disease disease = new Disease("Back pain");
        patient.addDisease(disease);
        Appointment appointment = new Appointment(doctor,patient,appointmentDate,disease);
        assertEquals(appointment.toString(), "23-3-2021");
    }

    @Test
    void testingDateFormatingFromString() throws IllegalArgumentException, IllegalDateException {
        LocalDate localDate = LocalDate.now();
        String appointmentDate = "111-2-2010";
        Doctor doctor = new Doctor("Doctor", "Doctory", "doc@someone.com", "123/444-222", "pass1", "M", localDate);
        Patient patient = new Patient("Patient", "Patienty", "pat@someone.com", "333/434-222", "pass1", "F", localDate);
        Disease disease = new Disease("Back pain");
        patient.addDisease(disease);
        String finalAppointmentDate = appointmentDate;
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Appointment(doctor,patient, finalAppointmentDate,disease));
        assertEquals(exception.getMessage(), "Date format is invalid!");
        appointmentDate = "11-2-20100";
        String finalAppointmentDate1 = appointmentDate;
        exception = assertThrows(IllegalArgumentException.class,
                () -> new Appointment(doctor,patient, finalAppointmentDate1,disease));
        assertEquals(exception.getMessage(), "Date format is invalid!");
    }
}
