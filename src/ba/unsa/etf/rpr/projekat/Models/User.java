package ba.unsa.etf.rpr.projekat.Models;


import ba.unsa.etf.rpr.projekat.Utilities.IllegalDateException;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.Objects;

public class User {
    private SimpleStringProperty firstName, lastName, email, phoneNumber, password, gender;
    private LocalDate dateOfBirth;
    private int id;

    public User(String firstName, String lastName, String email, String phoneNumber, String password, String gender, LocalDate dateOfBirth) throws IllegalDateException {
        if (firstName == null || lastName == null || email == null || phoneNumber == null || password == null || gender == null) {
            throw new IllegalArgumentException("Parameters cannot be null!");
        }
        if (dateOfBirth.isAfter(LocalDate.now()))
            throw new IllegalDateException("Date of birth cannot be in the future");
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.password = new SimpleStringProperty(password);
        this.gender = new SimpleStringProperty(gender);
        this.dateOfBirth = dateOfBirth;
    }

    public User(String firstName, String lastName, String email, String phoneNumber, String password, String gender, String date) throws IllegalDateException {
        if (firstName == null || lastName == null || email == null || phoneNumber == null || password == null || gender == null) {
            throw new IllegalArgumentException("Parameters cannot be null!");
        }
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.password = new SimpleStringProperty(password);
        this.gender = new SimpleStringProperty(gender);
        String[] parts = date.split("\\-");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];
        dateOfBirth = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        if (dateOfBirth.isAfter(LocalDate.now()))
            throw new IllegalDateException("Date of birth cannot be in the future");
    }

    public User() {
    }


    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName.equals(null)) throw new IllegalArgumentException("Argument cannot be empty!");
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName.equals("")) throw new IllegalArgumentException("Argument cannot be empty!");
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        if (email.equals("")) throw new IllegalArgumentException("Argument cannot be empty!");
        this.email.set(email);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.equals("")) throw new IllegalArgumentException("Argument cannot be empty!");
        this.phoneNumber.set(phoneNumber);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        if (password.equals("")) throw new IllegalArgumentException("Argument cannot be empty!");
        this.password.set(password);
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender.equals("")) throw new IllegalArgumentException("Argument cannot be empty!");
        this.gender.set(gender);
    }


    public String getDateOfBirthString() {
        return dateOfBirth.getDayOfMonth() + "-" + dateOfBirth.getMonthValue() + "-" + dateOfBirth.getYear();
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) throws IllegalDateException {
        if (dateOfBirth.equals(null)) throw new IllegalArgumentException("Argument cannot be empty!");
        if (dateOfBirth.isAfter(LocalDate.now()))
            throw new IllegalDateException("Date of birth cannot be in the future");
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(password, user.password) &&
                Objects.equals(gender, user.gender) &&
                Objects.equals(dateOfBirth, user.dateOfBirth);
    }


    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, phoneNumber, password, gender, dateOfBirth, id);
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
