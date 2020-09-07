package ba.unsa.etf.rpr.projekat.Models;

import java.util.ArrayList;

public class Clinic {
    private String name;
    private ArrayList<Doctor> doctors = new ArrayList<>();

    public Clinic(String name, ArrayList<Doctor> doctors) {
        this.name = name;
        this.doctors = doctors;
    }

    public void addDoctor(Doctor d){
        doctors.add(d);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }
}
