package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.model.Order;
import pl.edu.utp.wtie.service_station.repository.OrderDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl implements OrderDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveOrder(Order order) {
        String sql = "INSERT INTO Orders (issueDate, contactPhone, title, description, comments) " +
                "VALUES (now(), ?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getContactPhone(), order.getTitle(), order.getDescription(), order.getComments());

        String sqlRepair = "INSERT INTO Repairs (Customers_idCustomers, Cars_idCars, Orders_idOrders) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlRepair, findCustomerKey(), findCarKey(), findOrderKey());
    }

    @Override
    public List<Order> showAllOrders() {
        String sql = "SELECT * FROM Orders ORDER BY idOrders";
        return parseQueryResults(sql);
    }

    @Override
    public List<Long> showAllOrderNumbers() {
        String sql = "SELECT idOrders FROM Orders ORDER BY idOrders";
        List<Long> orderNumberList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> orderNumberList.add(Long.parseLong(String.valueOf(element.get("idOrders")))));
        return orderNumberList;
    }

    @Override
    public String showOrder(long id) {
        String sql = "SELECT title FROM Orders WHERE idOrders = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getString("title"), id);
    }

    @Override
    public void updateOrder(Order newOrder) {
        String sql = "UPDATE Orders SET contactPhone = ?, title = ?, comments = ? " +
                "WHERE idOrders = ?";
        jdbcTemplate.update(sql, newOrder.getContactPhone(), newOrder.getTitle(), newOrder.getComments(),
                newOrder.getId());
    }

    private Long findCustomerKey() {
        String sql = "SELECT idCustomers FROM Customers ORDER BY idCustomers DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("idCustomers"));
    }

    private Long findCarKey() {
        String sql = "SELECT idCars FROM Cars ORDER BY idCars DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("idCars"));
    }

    private Long findOrderKey() {
        String sql = "SELECT idOrders FROM Orders ORDER BY idOrders DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("idOrders"));
    }

    private List<Order> parseQueryResults(String sql) {
        List<Order> orderList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> orderList.add(new Order(
                Long.parseLong(String.valueOf(element.get("idOrders"))),
                Timestamp.valueOf(String.valueOf(element.get("issueDate"))),
                String.valueOf(element.get("contactPhone")),
                String.valueOf(element.get("title")),
                String.valueOf(element.get("description")),
                String.valueOf(element.get("comments"))
        )));
        return orderList;
    }
}
