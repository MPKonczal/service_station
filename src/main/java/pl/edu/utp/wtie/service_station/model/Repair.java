package pl.edu.utp.wtie.service_station.model;

public class Repair {

    private long idCustomer;
    private long idCar;
    private long idOrder;
    private String name;
    private String surname;
    private String phoneNumber;
    private String contactPhone;
    private String title;
    private String description;
    private String make;
    private String model;
    private String vin;
    private String registrationNumber;

    public Repair() {
    }

    public Repair(long idOrder, String name, String surname, String phoneNumber, String contactPhone, String title,
                  String description, String make, String model, String vin, String registrationNumber) {
        this.idOrder = idOrder;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.contactPhone = contactPhone;
        this.title = title;
        this.description = description;
        this.make = make;
        this.model = model;
        this.vin = vin;
        this.registrationNumber = registrationNumber;
    }

    public long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public long getIdCar() {
        return idCar;
    }

    public void setIdCar(long idCar) {
        this.idCar = idCar;
    }

    public long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(long idOrder) {
        this.idOrder = idOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
