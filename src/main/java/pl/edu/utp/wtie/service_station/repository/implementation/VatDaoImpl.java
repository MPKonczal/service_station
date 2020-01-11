package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.repository.VatDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class VatDaoImpl implements VatDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public VatDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Double> showAllVatRates() {
        String sql = "SELECT rate FROM Vats";
        return parseQueryResults(sql);
    }

    private List<Double> parseQueryResults(String sql) {
        List<Double> vatRateList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> vatRateList.add(Double.parseDouble(String.valueOf(element.get("rate")))));
        return vatRateList;
    }
}
