package ba.unsa.etf.rpr.projekat.Models;

public class InvalidDoctorChoice extends Throwable {
    public InvalidDoctorChoice(String message) {
        super(message);
    }
}
