package pl.edu.utp.wtie.service_station.repository;

import pl.edu.utp.wtie.service_station.model.Car;

import java.util.List;

public interface CarDao {

    void saveCar(Car car);

    List<Car> showAllCars();

    void updateCar(Car newCar);
}
