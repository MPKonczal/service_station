package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.repository.MakeDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class MakeDaoImpl implements MakeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MakeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> showAllMakes() {
        String sql = "SELECT make FROM Makes";
        return parseQueryResults(sql);
    }

    private List<String> parseQueryResults(String sql) {
        List<String> makeList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> makeList.add(String.valueOf(element.get("make"))));
        return makeList;
    }
}
