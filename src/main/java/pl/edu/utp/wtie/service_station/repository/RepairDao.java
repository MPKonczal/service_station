package pl.edu.utp.wtie.service_station.repository;

import pl.edu.utp.wtie.service_station.model.Repair;

import java.util.List;

public interface RepairDao {

    List<Repair> showAllRepairs();
}
