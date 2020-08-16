package ba.unsa.etf.rpr.projekat.Models;

import java.time.LocalDate;

public class DateClass {
    private String year;
    private String month;
    private String day;

    public DateClass(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateClass(int day, int month, int year) {
        this.day = String.valueOf(day);
        this.month = String.valueOf(month);
        this.year = String.valueOf(year);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getAppointmentDateOutput() {
        return getDay() + "/" + getMonth() + "/" + getYear();
    }

    @Override
    public String toString() {
        return getAppointmentDateOutput();
    }
}
