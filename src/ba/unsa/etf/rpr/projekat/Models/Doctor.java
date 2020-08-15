package ba.unsa.etf.rpr.projekat.Models;

import java.time.LocalDate;
import java.util.Date;

public class Doctor extends User {

    public Doctor(String firstName, String lastName, String email, String phoneNumber, String userName, String password, String gender, DateOfBirth dateOfBirth) {
        super(firstName, lastName, email, phoneNumber, userName, password, gender, dateOfBirth);
    }

    public Doctor(String firstName, String lastName, String email, String phoneNumber, String userName, String password, String gender, String dateOfBirth) {
        super(firstName, lastName, email, phoneNumber, userName, password, gender, dateOfBirth);
    }

    public Doctor() {

    }
}
