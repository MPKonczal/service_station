package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.repository.PositionDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PositionDaoImpl implements PositionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PositionDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> showAllPositions() {
        String sql = "SELECT position FROM Positions";
        return parseQueryResults(sql);
    }

    private List<String> parseQueryResults(String sql) {
        List<String> positionList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> positionList.add(String.valueOf(element.get("position"))));
        return positionList;
    }
}
