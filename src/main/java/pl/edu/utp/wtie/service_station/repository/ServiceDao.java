package pl.edu.utp.wtie.service_station.repository;

import pl.edu.utp.wtie.service_station.model.Service;

import java.util.List;

public interface ServiceDao {

    void saveService(Service service);

    List<Service> showAllServices();

    List<String> showServiceNames(String category);

    void deleteService(long id);
}
