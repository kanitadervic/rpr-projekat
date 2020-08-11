package ba.unsa.etf.rpr.projekat.Models;

import java.util.Date;

public class Doctor extends User {
    private Clinic clinic;

    public Doctor(String firstName, String lastName, String email, String phoneNumber, String userName, String password, char gender, Date dateOfBirth, Clinic clinic) {
        super(firstName, lastName, email, phoneNumber, userName, password, gender, dateOfBirth);
        this.clinic = clinic;
    }
}
