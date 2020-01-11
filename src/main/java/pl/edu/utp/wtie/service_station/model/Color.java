package pl.edu.utp.wtie.service_station.model;

public class Color {

    private long id;
    private String color;

    public Color() {
    }

    public Color(String color) {
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
