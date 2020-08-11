package ba.unsa.etf.rpr.projekat.Models;

import java.util.ArrayList;

public class Disease {
    private String name;
    private ArrayList<String> symptoms;
    private ArrayList<Medicament> medicaments;

    public Disease(String name, ArrayList<String> symptoms, ArrayList<Medicament> medicaments) {
        this.name = name;
        this.symptoms = symptoms;
        this.medicaments = medicaments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<String> symptoms) {
        this.symptoms = symptoms;
    }

    public ArrayList<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(ArrayList<Medicament> medicaments) {
        this.medicaments = medicaments;
    }
}
