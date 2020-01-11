package pl.edu.utp.wtie.service_station.model;

public class Position {

    private long id;
    private String position;

    public Position() {
    }

    public Position(String position) {
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
