package ba.unsa.etf.rpr.projekat.Models;

import java.util.Date;

public class Appointment {
    private Doctor doctor;
    private Patient patient;
    private DateClass appointmentDate;
    private int id;

    public Appointment(Doctor doctor, Patient patient, DateClass appointmentDate) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDate = appointmentDate;
    }

    public Appointment(User doctor, User patient, String appointmentDate) {
        String[] parts = appointmentDate.split("\\-");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];
        this.appointmentDate = new DateClass(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public DateClass getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(DateClass appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setId(int currentId) {
        this.id = currentId;
    }

    public int getId() {
        return id;
    }

    public String getAppointmentDateString(){
        return appointmentDate.getDay() + "-" + appointmentDate.getMonth() + "-" + appointmentDate.getYear();
    }
}
