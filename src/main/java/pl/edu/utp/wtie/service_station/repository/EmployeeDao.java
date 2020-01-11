package pl.edu.utp.wtie.service_station.repository;

import pl.edu.utp.wtie.service_station.model.Employee;

import java.util.List;

public interface EmployeeDao {

    void saveEmployee(Employee employee);

    List<Employee> showAllEmployees();

    List<String> showSurnames(String position);

    void updateEmployee(Employee newEmployee);

    void deleteEmployee(long id);
}
