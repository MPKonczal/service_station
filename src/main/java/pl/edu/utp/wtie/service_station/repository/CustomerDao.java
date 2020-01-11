package pl.edu.utp.wtie.service_station.repository;

import pl.edu.utp.wtie.service_station.model.Customer;

import java.util.List;

public interface CustomerDao {

    void saveCustomer(Customer customer);

    List<Customer> showAllCustomers();

    void updateCustomer(Customer newCustomer);
}
