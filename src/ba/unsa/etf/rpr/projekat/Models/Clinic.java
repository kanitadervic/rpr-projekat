package ba.unsa.etf.rpr.projekat.Models;

import java.util.ArrayList;

public class Clinic {
    private String name;
    private Location location;
    private ArrayList<User> doctors = new ArrayList<>();

    public Clinic(String name, Location location, ArrayList<User> doctors) {
        this.name = name;
        this.location = location;
        this.doctors = doctors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<User> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<User> doctors) {
        this.doctors = doctors;
    }
}
