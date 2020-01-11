package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.repository.ModelDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ModelDaoImpl implements ModelDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ModelDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> showModels(String make) {
        String sql = "SELECT model, make FROM Models INNER JOIN Makes ON Models.Makes_idMakes = Makes.idMakes " +
                "WHERE make IN('" + make + "')";
        return parseQueryResults(sql);
    }

    private List<String> parseQueryResults(String sql) {
        List<String> modelList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> modelList.add(String.valueOf(element.get("model"))));
        return modelList;
    }
}
