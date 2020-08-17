package ba.unsa.etf.rpr.projekat.Models;

import java.time.LocalDate;
import java.util.Objects;

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

    public DateClass(String dateString) {
        String[] parts = dateString.split("\\-");
        this.day = parts[0];
        this.month = parts[1];
        this.year = parts[2];
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
        return getDay() + "-" + getMonth() + "-" + getYear();
    }

    @Override
    public String toString() {
        return getAppointmentDateOutput();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateClass dateClass = (DateClass) o;
        return Objects.equals(year, dateClass.year) &&
                Objects.equals(month, dateClass.month) &&
                Objects.equals(day, dateClass.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }
}
