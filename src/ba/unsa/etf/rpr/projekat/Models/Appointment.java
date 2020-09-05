package ba.unsa.etf.rpr.projekat.Models;

import java.time.LocalDate;

import static java.lang.Integer.valueOf;

public class Appointment implements Comparable{
    private Doctor doctor;
    private Patient patient;
    private DateClass appointmentDate;
    private int id;
    public String patientFirstName;
    public String patientLastName;
    private Disease disease;
    public String diseaseName;

    public Appointment(Doctor doctor, Patient patient, DateClass appointmentDate, Disease disease) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDate = appointmentDate;
        this.patientFirstName = patient.getFirstName();
        this.patientLastName = patient.getLastName();
        this.disease = disease;
        this.diseaseName = disease.getName();
    }


    public Appointment(Doctor doctor, Patient patient, String appointmentDate, Disease disease) {
        this.doctor = doctor;
        this.patient = patient;
        String[] parts = appointmentDate.split("\\-");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];
        this.appointmentDate = new DateClass(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
        this.patientFirstName = patient.getFirstName();
        this.patientLastName = patient.getLastName();
        this.disease = disease;
        this.diseaseName = disease.getName();
    }

    public Appointment() {

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

    public String getAppointmentDateString() {
        return appointmentDate.getDay() + "-" + appointmentDate.getMonth() + "-" + appointmentDate.getYear();
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }


    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    @Override
    public String toString() {
        return (getAppointmentDateString());
    }


    @Override
    public int compareTo(Object o) {
        Appointment appointment = (Appointment) o;
        LocalDate l1 = LocalDate.of(Integer.parseInt(this.getAppointmentDate().getYear()), Integer.parseInt(this.getAppointmentDate().getMonth()), Integer.parseInt(this.getAppointmentDate().getDay()));
        LocalDate l2 = LocalDate.of(Integer.parseInt(appointment.getAppointmentDate().getYear()), Integer.parseInt(appointment.getAppointmentDate().getMonth()), Integer.parseInt(appointment.getAppointmentDate().getDay()));
        return l1.compareTo(l2);
    }
}
