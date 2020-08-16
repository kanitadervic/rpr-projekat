package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.Models.Appointment;

public class AppointmentModificationController {
    public Appointment appointmentModification;

    public AppointmentModificationController(Appointment forModification) {
        this.appointmentModification = forModification;
    }
}
