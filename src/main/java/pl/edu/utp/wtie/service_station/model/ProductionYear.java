package pl.edu.utp.wtie.service_station.model;

public class ProductionYear {

    private long id;
    private String productionYear;

    public ProductionYear() {
    }

    public ProductionYear(String productionYear) {
        this.productionYear = productionYear;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(String productionYear) {
        this.productionYear = productionYear;
    }
}
