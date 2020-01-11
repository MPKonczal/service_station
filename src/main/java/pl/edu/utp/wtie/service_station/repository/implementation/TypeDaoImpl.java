package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.repository.TypeDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TypeDaoImpl implements TypeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TypeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> showAllTypes() {
        String sql = "SELECT type FROM Types";
        return parseQueryResults(sql);
    }

    private List<String> parseQueryResults(String sql) {
        List<String> typeList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> typeList.add(String.valueOf(element.get("type"))));
        return typeList;
    }
}
