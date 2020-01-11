package pl.edu.utp.wtie.service_station.model;

import java.sql.Timestamp;

public class Realization {

    private long idOrder;
    private long idService;
    private long idEmployee;
    private Timestamp realizationDate;
    private String contactPhone;
    private String title;
    private String description;
    private String category;
    private String service;
    private String scopeService;
    private double netPrice;
    private double vatRate;
    private String position;
    private String name;
    private String surnme;
    private String pesel;
    private String phoneNumber;
    private double grossPrice;

    public Realization() {
    }

    public Realization(long idOrder, String title, String category, String service, String position, String surnme) {
        this.idOrder = idOrder;
        this.title = title;
        this.category = category;
        this.service = service;
        this.position = position;
        this.surnme = surnme;
    }

    public Realization(long idOrder, Timestamp realizationDate, String contactPhone, String title, String description,
                       String category, String service, String scopeService, double netPrice, double vatRate,
                       String position, String name, String surnme, String pesel, String phoneNumber) {
        this.idOrder = idOrder;
        this.realizationDate = realizationDate;
        this.contactPhone = contactPhone;
        this.title = title;
        this.description = description;
        this.category = category;
        this.service = service;
        this.scopeService = scopeService;
        this.netPrice = netPrice;
        this.vatRate = vatRate;
        this.position = position;
        this.name = name;
        this.surnme = surnme;
        this.pesel = pesel;
        this.phoneNumber = phoneNumber;
    }

    public long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(long idOrder) {
        this.idOrder = idOrder;
    }

    public long getIdService() {
        return idService;
    }

    public void setIdService(long idService) {
        this.idService = idService;
    }

    public long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Timestamp getRealizationDate() {
        return realizationDate;
    }

    public void setRealizationDate(Timestamp realizationDate) {
        this.realizationDate = realizationDate;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnme() {
        return surnme;
    }

    public void setSurnme(String surnme) {
        this.surnme = surnme;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getGrossPrice() {
        return getVatRate() * getNetPrice() + getNetPrice();
    }

    public void setGrossPrice(double grossPrice) {
        this.grossPrice = grossPrice;
    }
}
