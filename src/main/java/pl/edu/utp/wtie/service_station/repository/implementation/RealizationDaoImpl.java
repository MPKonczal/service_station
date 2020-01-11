package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.model.Realization;
import pl.edu.utp.wtie.service_station.repository.RealizationDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class RealizationDaoImpl implements RealizationDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RealizationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveRealization(Realization realization) {
        String sql = "INSERT INTO Realizations (Orders_idOrders, Services_idServices, Employees_idEmployees, " +
                "realizationDate) VALUES (?, ?, ?, now())";
        jdbcTemplate.update(sql, realization.getIdOrder(), findServiceKey(realization), findEmployeeKey(realization));
    }

    @Override
    public Long findServiceKey(Realization realization) {
        String sql1 = "SELECT idServices FROM Services WHERE service = ?";
        return jdbcTemplate.queryForObject(sql1, (rs, rowNum) -> rs.getLong("idServices"), realization.getService());
    }

    @Override
    public Long findEmployeeKey(Realization realization) {
        String sql2 = "SELECT idEmployees FROM Employees WHERE surname = ?";
        return jdbcTemplate.queryForObject(sql2, (rs, rowNum) -> rs.getLong("idEmployees"), realization.getSurnme());
    }

    @Override
    public List<Realization> showAllRealizations() {
        String sql = "SELECT Orders_idOrders, Services_idServices, Employees_idEmployees, realizationDate, " +
                "contactPhone, title, description, category, service, scopeService, netPrice, rate, position, " +
                "name, surname, pesel, phoneNumber FROM Realizations " +
                "INNER JOIN Orders ON Realizations.Orders_idOrders = Orders.idOrders " +
                "INNER JOIN Services ON Realizations.Services_idServices = Services.idServices " +
                "INNER JOIN Employees ON Realizations.Employees_idEmployees = Employees.idEmployees " +
                "INNER JOIN (SELECT idServices, category FROM Services " +
                "INNER JOIN ServiceCategories ON Services.ServiceCategories_idServiceCategories = " +
                "ServiceCategories.idServiceCategories) AS categories ON " +
                "Realizations.Services_idServices = categories.idServices " +
                "INNER JOIN (SELECT idServices, rate FROM Services " +
                "INNER JOIN Vats ON Services.Vats_idVats = Vats.idVats) AS rates ON " +
                "Realizations.Services_idServices = rates.idServices " +
                "INNER JOIN (SELECT idEmployees, position FROM Employees " +
                "INNER JOIN Positions ON Employees.Positions_idPositions = Positions.idPositions) AS positions ON " +
                "Realizations.Employees_idEmployees = positions.idEmployees ORDER BY realizationDate, idOrders";
        return parseQueryResults(sql);
    }

    private List<Realization> parseQueryResults(String sql) {
        List<Realization> realizationList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> realizationList.add(new Realization(
                Long.parseLong(String.valueOf(element.get("Orders_idOrders"))),
                Timestamp.valueOf(String.valueOf(element.get("realizationDate"))),
                String.valueOf(element.get("contactPhone")),
                String.valueOf(element.get("title")),
                String.valueOf(element.get("description")),
                String.valueOf(element.get("category")),
                String.valueOf(element.get("service")),
                String.valueOf(element.get("scopeService")),
                Double.parseDouble(String.valueOf(element.get("netPrice"))),
                Double.parseDouble(String.valueOf(element.get("rate"))),
                String.valueOf(element.get("position")),
                String.valueOf(element.get("name")),
                String.valueOf(element.get("surname")),
                String.valueOf(element.get("pesel")),
                String.valueOf(element.get("phoneNumber"))
        )));
        return realizationList;
    }
}
