package pl.edu.utp.wtie.service_station.model;

public class Customer {

    private long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String companyName;
    private String nip;
    private String comments;

    public Customer() {
    }

    public Customer(String name, String surname, String phoneNumber, String companyName, String nip, String comments) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.nip = nip;
        this.comments = comments;
    }

    public Customer(long id, String name, String surname, String phoneNumber, String companyName, String nip,
                    String comments) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.nip = nip;
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
