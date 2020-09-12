package ba.unsa.etf.rpr.projekat.Models;

import ba.unsa.etf.rpr.projekat.Utilities.IllegalDateException;

import java.time.LocalDate;

import static java.lang.Integer.valueOf;

public class Appointment implements Comparable {
    private Doctor doctor;
    private Patient patient;
    private LocalDate appointmentDate;
    private int id;
    public String patientFirstName;
    public String patientLastName;
    private Disease disease;
    public String diseaseName;

    public Appointment(Doctor doctor, Patient patient, LocalDate appointmentDate, Disease disease) {
        if(doctor == null || patient == null || appointmentDate == null || disease == null){
            throw new IllegalArgumentException("Parameters cannot be null!");
        }
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
        if((day.length() > 2 || day.length()<0) || (month.length() > 2 || month.length()<0) || (year.length() != 4)){
            throw new IllegalArgumentException("Date format is invalid!");
        }
        this.appointmentDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
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
        if(doctor == null) throw new IllegalArgumentException("Argument cannot be null!");
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        if(patient == null) throw new IllegalArgumentException("Argument cannot be null!");
        this.patient = patient;

    }

    public void setId(int currentId) {
        this.id = currentId;
    }

    public int getId() {
        return id;
    }

    public String getAppointmentDateString() {
        return appointmentDate.getDayOfMonth() + "-" + appointmentDate.getMonthValue() + "-" + appointmentDate.getYear();
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
        if(disease == null) throw new IllegalArgumentException("Argument cannot be null!");
        this.disease = disease;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }


    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) throws IllegalDateException {
        if(appointmentDate.isBefore(LocalDate.now())) throw new IllegalDateException("Appointment cannot be in the past");
        if(appointmentDate == null) throw new IllegalArgumentException("Argument cannot be null!");
        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        String[] parts = appointmentDate.split("\\-");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];
        if((day.length() > 2 || day.length()<0) || (month.length() > 2 || month.length()<0) || (year.length() != 4)){
            throw new IllegalArgumentException("Date format is invalid!");
        }
        this.appointmentDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }


    @Override
    public String toString() {
        return (getAppointmentDateString());
    }

    @Override
    public int compareTo(Object o) {
        Appointment appointment = (Appointment) o;
        LocalDate l2 = LocalDate.of((appointment.getAppointmentDate().getYear()), (appointment.getAppointmentDate().getMonth()), (appointment.getAppointmentDate().getDayOfMonth()));
        return this.compareTo(l2);
    }
}
