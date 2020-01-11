package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.repository.ColorDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ColorDaoImpl implements ColorDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ColorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> showAllColors() {
        String sql = "SELECT color FROM Colors";
        return parseQueryResults(sql);
    }

    private List<String> parseQueryResults(String sql) {
        List<String> colorList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> colorList.add(String.valueOf(element.get("color"))));
        return colorList;
    }
}
