package pl.edu.utp.wtie.service_station.model;

public class Make {

    private long id;
    private String make;

    public Make() {
    }

    public Make(String make) {
        this.make = make;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }
}
