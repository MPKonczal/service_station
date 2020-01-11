package pl.edu.utp.wtie.service_station.model;

public class Vat {

    private long id;
    private String type;
    private double rate;

    public Vat() {
    }

    public Vat(String type, double rate) {
        this.type = type;
        this.rate = rate;
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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
