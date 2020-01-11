package pl.edu.utp.wtie.service_station.model;

public class Car {

    private long id;
    private String make;
    private String model;
    private String type;
    private String color;
    private int productionYear;
    private String vin;
    private double mileage;
    private String registrationNumber;

    public Car() {
    }

    // Create
    public Car(String model, String type, String color, int productionYear, String vin, double mileage,
               String registrationNumber) {
        this.model = model;
        this.type = type;
        this.color = color;
        this.productionYear = productionYear;
        this.vin = vin;
        this.mileage = mileage;
        this.registrationNumber = registrationNumber;
    }

    // Update
    public Car(long id, String model, String type, String color, int productionYear, String vin, double mileage,
               String registrationNumber) {
        this.id = id;
        this.model = model;
        this.type = type;
        this.color = color;
        this.productionYear = productionYear;
        this.vin = vin;
        this.mileage = mileage;
        this.registrationNumber = registrationNumber;
    }

    // Read
    public Car(long id, String make, String model, String type, String color, int productionYear, String vin,
               double mileage, String registrationNumber) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.type = type;
        this.color = color;
        this.productionYear = productionYear;
        this.vin = vin;
        this.mileage = mileage;
        this.registrationNumber = registrationNumber;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
