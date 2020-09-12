package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAO.DiseaseDAO;
import ba.unsa.etf.rpr.projekat.Models.Disease;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiseaseDAOTest {
    private DiseaseDAO diseaseDAO = DiseaseDAO.getInstance();

    @BeforeEach
    public void resetBase() throws SQLException {
        diseaseDAO.resetBase();
    }

    @Test
    void testGetDiseaseById() {
        ArrayList<Disease> diseases = diseaseDAO.getAllDiseases();
        int id = diseases.get(0).getId();
        assertEquals(1, id);
        id = diseases.get(2).getId();
        assertEquals(3, id);
    }

    @Test
    void testGetDiseaseIdByName() {
        int id = diseaseDAO.getIdByName("Depression");
        assertEquals(1, id);
        id = diseaseDAO.getIdByName("Back pain");
        assertEquals(3, id);
    }

    @Test
    void testAddDisease() {
        assertEquals(diseaseDAO.getAllDiseases().size(), 3);
        Disease disease = new Disease("Corona");
        diseaseDAO.addDisease(disease, 1);
        assertEquals(4, diseaseDAO.getAllDiseases().size());
    }

    @Test
    void testGetDiseasesForPatient() {
        ArrayList<Disease> diseases = diseaseDAO.getDiseasesForPatient(2);
        assertEquals(2, diseases.size());
        diseaseDAO.addDisease(new Disease("Corona"), 2);
        assertEquals(3, diseaseDAO.getDiseasesForPatient(2).size());
    }

}
