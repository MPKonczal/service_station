package pl.edu.utp.wtie.service_station.model;

public class Type {

    private long id;
    private String type;

    public Type() {
    }

    public Type(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
