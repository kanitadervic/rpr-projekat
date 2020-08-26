package ba.unsa.etf.rpr.projekat.Models;

import java.util.ArrayList;

public class Patient extends User{
    private ArrayList<Disease> diseases;

    public Patient(String firstName, String lastName, String email, String phoneNumber, String userName, String password, String gender, DateClass dateOfBirth) {
        super(firstName, lastName, email, phoneNumber, userName, password, gender, dateOfBirth);
        diseases = new ArrayList<>();
    }

    public Patient(String firstName, String lastName, String email, String phoneNumber, String userName, String password, String gender, String dateOfBirth) {
        super(firstName, lastName, email, phoneNumber, userName, password, gender, dateOfBirth);
        diseases = new ArrayList<>();
    }

    public Patient() {

    }

    @Override
    public ArrayList<Disease> getDiseases() {
        return diseases;
    }

    @Override
    public void setDiseases(ArrayList<Disease> diseases) {
        this.diseases = diseases;
    }

    public void addDisease(Disease d){
        this.diseases.add(d);
    }
}
