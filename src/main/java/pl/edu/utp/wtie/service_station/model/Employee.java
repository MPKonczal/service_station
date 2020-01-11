package pl.edu.utp.wtie.service_station.model;

public class Employee {

    private long id;
    private String position;
    private String name;
    private String surname;
    private String pesel;
    private String phoneNumber;
    private String comments;

    public Employee() {
    }

    public Employee(String position, String name, String surname, String pesel, String phoneNumber, String comments) {
        this.position = position;
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.phoneNumber = phoneNumber;
        this.comments = comments;
    }

    public Employee(long id, String position, String name, String surname, String pesel, String phoneNumber,
                    String comments) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.phoneNumber = phoneNumber;
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
