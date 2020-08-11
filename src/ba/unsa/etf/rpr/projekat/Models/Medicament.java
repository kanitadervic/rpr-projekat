package ba.unsa.etf.rpr.projekat.Models;

public class Medicament {
    private String name;
    private String medicamentDose;

    public Medicament(String name, String medicamentDose) {
        this.name = name;
        this.medicamentDose = medicamentDose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicamentDose() {
        return medicamentDose;
    }

    public void setMedicamentDose(String medicamentDose) {
        this.medicamentDose = medicamentDose;
    }
}
