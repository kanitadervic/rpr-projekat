package ba.unsa.etf.rpr.projekat.Models;

import java.time.LocalDate;
import java.util.ArrayList;

public class Patient extends User {
    private ArrayList<Disease> diseases;

    public Patient(String firstName, String lastName, String email, String phoneNumber, String password, String gender, LocalDate dateOfBirth) {
        super(firstName, lastName, email, phoneNumber, password, gender, dateOfBirth);
        diseases = new ArrayList<>();
    }

    public Patient(String firstName, String lastName, String email, String phoneNumber, String password, String gender, String dateOfBirth) {
        super(firstName, lastName, email, phoneNumber, password, gender, dateOfBirth);
        diseases = new ArrayList<>();
    }

    public Patient() {
    }

    public ArrayList<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(ArrayList<Disease> diseases) {
        this.diseases = diseases;
    }

    public void addDisease(Disease d) {
        this.diseases.add(d);
    }
}
