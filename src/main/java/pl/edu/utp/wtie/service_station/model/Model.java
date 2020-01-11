package pl.edu.utp.wtie.service_station.model;

public class Model {

    private long id;
    private long idMake;
    private String model;

    public Model() {
    }

    public Model(long idMake, String model) {
        this.idMake = idMake;
        this.model = model;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdMake() {
        return idMake;
    }

    public void setIdMake(long idMake) {
        this.idMake = idMake;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
