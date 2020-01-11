package pl.edu.utp.wtie.service_station.model;

public class Service {

    private long id;
    private String serviceCategory;
    private String service;
    private String scopeService;
    private double netPrice;
    private double vatRate;

    public Service() {
    }

    public Service(String serviceCategory, String service, String scopeService, double netPrice, double vatRate) {
        this.serviceCategory = serviceCategory;
        this.service = service;
        this.scopeService = scopeService;
        this.netPrice = netPrice;
        this.vatRate = vatRate;
    }

    public Service(long id, String serviceCategory, String service, String scopeService, double netPrice,
                   double vatRate) {
        this.id = id;
        this.serviceCategory = serviceCategory;
        this.service = service;
        this.scopeService = scopeService;
        this.netPrice = netPrice;
        this.vatRate = vatRate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getScopeService() {
        return scopeService;
    }

    public void setScopeService(String scopeService) {
        this.scopeService = scopeService;
    }

    public double getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(double netPrice) {
        this.netPrice = netPrice;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }
}
