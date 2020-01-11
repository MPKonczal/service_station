package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.model.Service;
import pl.edu.utp.wtie.service_station.repository.ServiceDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ServiceDaoImpl implements ServiceDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ServiceDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveService(Service service) {
        String sql = "INSERT INTO Services (ServiceCategories_idServiceCategories, service, scopeService, netPrice, " +
                "Vats_idVats) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, findServiceCategoryKey(service), service.getService(), service.getScopeService(),
                service.getNetPrice(), findVatKey(service));
    }

    @Override
    public List<Service> showAllServices() {
        String sql = "SELECT idServices, category, service, scopeService, netPrice, rate FROM Services " +
                "INNER JOIN ServiceCategories ON " +
                "Services.ServiceCategories_idServiceCategories = ServiceCategories.idServiceCategories " +
                "INNER JOIN Vats ON Services.Vats_idVats = Vats.idVats ORDER BY idServices";
        return parseQueryResults(sql);
    }

    @Override
    public List<String> showServiceNames(String category) {
        String sql = "SELECT service FROM Services WHERE ServiceCategories_idServiceCategories = " +
                findServiceCategoryKey(category);
        List<String> serviceNameList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> serviceNameList.add(String.valueOf(element.get("service"))));
        return serviceNameList;
    }

    @Override
    public void deleteService(long id) {
        String sql = "DELETE FROM Services WHERE idServices = ?";
        jdbcTemplate.update(sql, id);
    }

    private Long findServiceCategoryKey(Service service) {
        String sql = "SELECT idServiceCategories FROM ServiceCategories WHERE category = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("idServiceCategories"),
                service.getServiceCategory());
    }

    private Long findServiceCategoryKey(String category) {
        String sql = "SELECT idServiceCategories FROM ServiceCategories WHERE category = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("idServiceCategories"), category);
    }

    private Long findVatKey(Service service) {
        String sql = "SELECT idVats FROM Vats WHERE rate = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("idVats"), service.getVatRate());
    }

    private List<Service> parseQueryResults(String sql) {
        List<Service> serviceList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> serviceList.add(new Service(
                Long.parseLong(String.valueOf(element.get("idServices"))),
                String.valueOf(element.get("category")),
                String.valueOf(element.get("service")),
                String.valueOf(element.get("scopeService")),
                Double.parseDouble(String.valueOf(element.get("netPrice"))),
                Double.parseDouble(String.valueOf(element.get("rate")))
        )));
        return serviceList;
    }
}
