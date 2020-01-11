package pl.edu.utp.wtie.service_station.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.edu.utp.wtie.service_station.model.Employee;
import pl.edu.utp.wtie.service_station.repository.EmployeeDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveEmployee(Employee employee) {
        String sql = "INSERT INTO Employees (Positions_idPositions, name, surname, pesel, phoneNumber, comments) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, findPositionKey(employee), employee.getName(), employee.getSurname(),
                employee.getPesel(), employee.getPhoneNumber(), employee.getComments());
    }

    @Override
    public List<Employee> showAllEmployees() {
        String sql = "SELECT idEmployees, position, name, surname, pesel, phoneNumber, comments " +
                "FROM Employees INNER JOIN Positions ON Employees.Positions_idPositions = Positions.idPositions " +
                "ORDER BY idEmployees";
        return parseQueryResults(sql);
    }

    @Override
    public List<String> showSurnames(String position) {
        String sql = "SELECT surname FROM Employees WHERE Positions_idPositions = " +
                findPositionKey(position);
        List<String> surnameList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> surnameList.add(String.valueOf(element.get("surname"))));
        return surnameList;
    }

    @Override
    public void updateEmployee(Employee newEmployee) {
        String sql = "UPDATE Employees SET Positions_idPositions = ?, name = ?, surname = ?, pesel = ?, " +
                "phoneNumber = ?, comments = ? WHERE idEmployees = ?";
        jdbcTemplate.update(sql, findPositionKey(newEmployee), newEmployee.getName(), newEmployee.getSurname(),
                newEmployee.getPesel(), newEmployee.getPhoneNumber(), newEmployee.getComments(), newEmployee.getId());
    }

    @Override
    public void deleteEmployee(long id) {
        String sql = "DELETE FROM Employees WHERE idEmployees = ?";
        jdbcTemplate.update(sql, id);
    }

    private Long findPositionKey(Employee employee) {
        String sql = "SELECT idPositions FROM Positions WHERE position = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("idPositions"), employee.getPosition());
    }

    private Long findPositionKey(String position) {
        String sql = "SELECT idPositions FROM Positions WHERE position = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("idPositions"), position);
    }

    private List<Employee> parseQueryResults(String sql) {
        List<Employee> employeeList = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(element -> employeeList.add(new Employee(
                Long.parseLong(String.valueOf(element.get("idEmployees"))),
                String.valueOf(element.get("position")),
                String.valueOf(element.get("name")),
                String.valueOf(element.get("surname")),
                String.valueOf(element.get("pesel")),
                String.valueOf(element.get("phoneNumber")),
                String.valueOf(element.get("comments"))
        )));
        return employeeList;
    }
}
