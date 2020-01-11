package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.repository.ServiceCategoryDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ServiceCategoryDaoImpl implements ServiceCategoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ServiceCategoryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> showAllServiceCategories() {
        String sql = "SELECT category FROM ServiceCategories WHERE ServiceCategories_idServiceCategories IS NULL";
        return parseQueryResults(sql);
    }

    @Override
    public List<String> showServiceSubcategories(Long idCategory) {
        String sql = "SELECT category FROM ServiceCategories WHERE ServiceCategories_idServiceCategories = " + idCategory + "";
        return parseQueryResults(sql);
    }

    @Override
    public Long findServiceCategoryKey(String category) {
        String sql1 = "SELECT idServiceCategories FROM ServiceCategories WHERE category = ?";
        return jdbcTemplate.queryForObject(sql1, (rs, rowNum) -> rs.getLong("idServiceCategories"), category);
    }

    private List<String> parseQueryResults(String sql) {
        List<String> serviceCategoriesList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> serviceCategoriesList.add(String.valueOf(element.get("category"))));
        return serviceCategoriesList;
    }
}
