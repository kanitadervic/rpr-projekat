package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.Models.Disease;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiseaseTest {
    @Test
    void constructorTest() {
        Disease disease = new Disease("Back pain");
        assertEquals("Back pain", disease.getName());
        disease.setName("Corona");
        assertEquals("Corona", disease.getName());
    }

    @Test
    void constructorTestThrowsException() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Disease(""));
        assertEquals(exception.getMessage(), "Parameters are invalid");
    }

    @Test
    void testingSetter() {
        Disease disease = new Disease("Back pain");
        assertEquals("Back pain", disease.getName());
        disease.setName("New disease");
        assertEquals("New disease", disease.getName());
    }

    @Test
    void testingSetterThrowsException() throws IllegalArgumentException {
        Disease disease = new Disease("Back pain");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> disease.setName(""));
        assertEquals(exception.getMessage(), "Parameters are invalid");
    }

}
