package ba.unsa.etf.rpr.projekat.Models;

import ba.unsa.etf.rpr.projekat.Utilities.IllegalDateException;

import java.time.LocalDate;

public class Doctor extends User {

    public Doctor(String firstName, String lastName, String email, String phoneNumber, String password, String gender, LocalDate dateOfBirth) throws IllegalDateException {
        super(firstName, lastName, email, phoneNumber, password, gender, dateOfBirth);
    }

    public Doctor(String firstName, String lastName, String email, String phoneNumber, String password, String gender, String dateOfBirth) throws IllegalDateException {
        super(firstName, lastName, email, phoneNumber, password, gender, dateOfBirth);
    }

    public Doctor() {
    }
}
