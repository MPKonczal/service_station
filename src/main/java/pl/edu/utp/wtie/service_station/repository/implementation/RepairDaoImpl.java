package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.model.Repair;
import pl.edu.utp.wtie.service_station.repository.RepairDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class RepairDaoImpl implements RepairDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RepairDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Repair> showAllRepairs() {
        String sql = "SELECT Customers_idCustomers, Cars_idCars, Orders_idOrders, name, surname, phoneNumber, " +
                "contactPhone, title, description, make, model, vin, registrationNumber FROM Repairs " +
                "INNER JOIN Customers ON Repairs.Customers_idCustomers = Customers.idCustomers " +
                "INNER JOIN Orders ON Repairs.Orders_idOrders = Orders.idOrders " +
                "INNER JOIN Cars ON Repairs.Cars_idCars = Cars.idCars " +
                "INNER JOIN (SELECT idCars, model FROM Cars " +
                "INNER JOIN Models ON Cars.Models_idModels = Models.idModels) AS models ON " +
                "Repairs.Cars_idCars = models.idCars " +
                "INNER JOIN (SELECT idModels, make FROM Models " +
                "INNER JOIN Makes ON Models.Makes_idMakes = Makes.idMakes) AS makes ON " +
                "Cars.Models_idModels = makes.idModels " +
                "ORDER BY Orders_idOrders";
        return parseQueryResults(sql);
    }

    private List<Repair> parseQueryResults(String sql) {
        List<Repair> repairList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> repairList.add(new Repair(
                Long.parseLong(String.valueOf(element.get("Orders_idOrders"))),
                String.valueOf(element.get("name")),
                String.valueOf(element.get("surname")),
                String.valueOf(element.get("phoneNumber")),
                String.valueOf(element.get("contactPhone")),
                String.valueOf(element.get("title")),
                String.valueOf(element.get("description")),
                String.valueOf(element.get("make")),
                String.valueOf(element.get("model")),
                String.valueOf(element.get("vin")),
                String.valueOf(element.get("registrationNumber"))
        )));
        return repairList;
    }
}
