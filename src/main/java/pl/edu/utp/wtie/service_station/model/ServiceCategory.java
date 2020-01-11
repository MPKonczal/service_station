package pl.edu.utp.wtie.service_station.model;

public class ServiceCategory {

    private long id;
    private String category;
    private long idServiceCategory;

    public ServiceCategory() {
    }

    public ServiceCategory(String category, long idServiceCategory) {
        this.category = category;
        this.idServiceCategory = idServiceCategory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getIdServiceCategory() {
        return idServiceCategory;
    }

    public void setIdServiceCategory(long idServiceCategory) {
        this.idServiceCategory = idServiceCategory;
    }
}
