package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.repository.ProductionYearDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ProductionYearDaoImpl implements ProductionYearDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductionYearDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Integer> showAllProductionYears() {
        String sql = "SELECT productionYear FROM ProductionYears";
        return parseQueryResults(sql);
    }

    private List<Integer> parseQueryResults(String sql) {
        List<Integer> productionYearList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> productionYearList.add(
                Integer.parseInt(String.valueOf(element.get("productionYear")))));
        return productionYearList;
    }
}
