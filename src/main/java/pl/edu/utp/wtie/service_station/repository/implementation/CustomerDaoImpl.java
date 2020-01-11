package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.model.Customer;
import pl.edu.utp.wtie.service_station.repository.CustomerDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerDaoImpl implements CustomerDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveCustomer(Customer customer) {
        String sqlCustomer = "INSERT INTO Customers (name, surname, phoneNumber, companyName, nip, comments) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlCustomer, customer.getName(), customer.getSurname(), customer.getPhoneNumber(),
                customer.getCompanyName(), customer.getNip(), customer.getComments());
    }

    @Override
    public List<Customer> showAllCustomers() {
        String sql = "SELECT * FROM Customers ORDER BY idCustomers";
        return parseQueryResults(sql);
    }

    @Override
    public void updateCustomer(Customer newCustomer) {
        String sql = "UPDATE Customers SET name = ?, surname = ?, phoneNumber = ?, companyName = ?, nip = ?, " +
                "comments = ? WHERE idCustomers = ?";
        jdbcTemplate.update(sql, newCustomer.getName(), newCustomer.getSurname(), newCustomer.getPhoneNumber(),
                newCustomer.getCompanyName(), newCustomer.getNip(), newCustomer.getComments(), newCustomer.getId());
    }

    private List<Customer> parseQueryResults(String sql) {
        List<Customer> customerList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> customerList.add(new Customer(
                Long.parseLong(String.valueOf(element.get("idCustomers"))),
                String.valueOf(element.get("name")),
                String.valueOf(element.get("surname")),
                String.valueOf(element.get("phoneNumber")),
                String.valueOf(element.get("companyName")),
                String.valueOf(element.get("nip")),
                String.valueOf(element.get("comments"))
        )));
        return customerList;
    }
}
