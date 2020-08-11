package ba.unsa.etf.rpr.projekat.Models;

import java.util.Date;

public class Patient extends User{
    private Disease disease;

    public Patient(String firstName, String lastName, String email, String phoneNumber, String userName, String password, char gender, Date dateOfBirth) {
        super(firstName, lastName, email, phoneNumber, userName, password, gender, dateOfBirth);
    }
}
