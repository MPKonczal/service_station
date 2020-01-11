package pl.edu.utp.wtie.service_station.model;

import java.sql.Timestamp;

public class Order {

    private long id;
    private Timestamp issueDate;
    private String contactPhone;
    private String title;
    private String description;
    private String comments;

    public Order() {
    }

    public Order(long id, String contactPhone, String title, String comments) {
        this.id = id;
        this.contactPhone = contactPhone;
        this.title = title;
        this.comments = comments;
    }

    public Order(String contactPhone, String title, String description, String comments) {
        this.contactPhone = contactPhone;
        this.title = title;
        this.description = description;
        this.comments = comments;
    }

    public Order(long id, Timestamp issueDate, String contactPhone, String title, String description, String comments) {
        this.id = id;
        this.issueDate = issueDate;
        this.contactPhone = contactPhone;
        this.title = title;
        this.description = description;
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Timestamp issueDate) {
        this.issueDate = issueDate;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
