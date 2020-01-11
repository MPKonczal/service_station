package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.model.Car;
import pl.edu.utp.wtie.service_station.repository.CarDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CarDaoImpl implements CarDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CarDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveCar(Car car) {
        String sql = "INSERT INTO Cars (Models_idModels, Types_idTypes, Colors_idColors, " +
                "ProductionYears_idProductionYears, vin, mileage, registrationNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, findModelKey(car), findTypeKey(car), findColorKey(car),
                findProductionYearKey(car), car.getVin(), car.getMileage(),
                car.getRegistrationNumber());
    }

    @Override
    public List<Car> showAllCars() {
        String sql = "SELECT idCars, make, model, type, color, productionYear, vin, mileage, registrationNumber " +
                "FROM Cars INNER JOIN Models ON Cars.Models_idModels = Models.idModels " +
                "INNER JOIN (SELECT idModels, make FROM Models " +
                "INNER JOIN Makes ON Models.Makes_idMakes = Makes.idMakes) AS makes ON " +
                "Cars.Models_idModels = makes.idModels " +
                "INNER JOIN Types ON Cars.Types_idTypes = Types.idTypes " +
                "INNER JOIN Colors ON Cars.Colors_idColors = Colors.idColors " +
                "INNER JOIN ProductionYears ON " +
                "Cars.ProductionYears_idProductionYears = ProductionYears.idProductionYears ORDER BY idCars";
        return parseQueryResults(sql);
    }

    @Override
    public void updateCar(Car newCar) {
        String sql = "UPDATE Cars SET Models_idModels = ?, Types_idTypes = ?, Colors_idColors = ?, " +
                "ProductionYears_idProductionYears = ?, vin = ?, mileage = ?, registrationNumber = ? " +
                "WHERE idCars = ?";
        jdbcTemplate.update(sql, findModelKey(newCar), findTypeKey(newCar), findColorKey(newCar),
                findProductionYearKey(newCar), newCar.getVin(), newCar.getMileage(),
                newCar.getRegistrationNumber(), newCar.getId());
    }

    private Long findModelKey(Car car) {
        String sql1 = "SELECT idModels FROM Models WHERE model = ?";
        return jdbcTemplate.queryForObject(sql1, (rs, rowNum) -> rs.getLong("idModels"), car.getModel());
    }

    private Long findTypeKey(Car car) {
        String sql2 = "SELECT idTypes FROM Types WHERE type = ?";
        return jdbcTemplate.queryForObject(sql2, (rs, rowNum) -> rs.getLong("idTypes"), car.getType());
    }

    private Long findColorKey(Car car) {
        String sql3 = "SELECT idColors FROM Colors WHERE color = ?";
        return jdbcTemplate.queryForObject(sql3, (rs, rowNum) -> rs.getLong("idColors"), car.getColor());
    }

    private Long findProductionYearKey(Car car) {
        String sql4 = "SELECT idProductionYears FROM ProductionYears WHERE productionYear = ?";
        return jdbcTemplate.queryForObject(sql4, (rs, rowNum) ->
                rs.getLong("idProductionYears"), car.getProductionYear());
    }

    private List<Car> parseQueryResults(String sql) {
        List<Car> carList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> carList.add(new Car(
                Long.parseLong(String.valueOf(element.get("idCars"))),
                String.valueOf(element.get("make")),
                String.valueOf(element.get("model")),
                String.valueOf(element.get("type")),
                String.valueOf(element.get("color")),
                Integer.parseInt(String.valueOf(element.get("productionYear"))),
                String.valueOf(element.get("vin")),
                Double.parseDouble(String.valueOf(element.get("mileage"))),
                String.valueOf(element.get("registrationNumber"))
        )));
        return carList;
    }
}
