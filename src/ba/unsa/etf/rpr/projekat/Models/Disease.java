package ba.unsa.etf.rpr.projekat.Models;

public class Disease {
    private String name;
    private int id;

    public Disease(String name) {
        if(name.equals("")){
            throw new IllegalArgumentException("Parameters are invalid");
        }
        this.name = name;
    }

    public Disease() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.equals("")){
            throw new IllegalArgumentException("Parameters are invalid");
        }
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
