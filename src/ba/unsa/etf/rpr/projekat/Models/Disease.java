package ba.unsa.etf.rpr.projekat.Models;

import java.util.ArrayList;

public class Disease {
    private String name;
    private int id;

    public Disease(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
