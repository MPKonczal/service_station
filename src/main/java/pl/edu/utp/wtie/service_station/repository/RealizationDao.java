package pl.edu.utp.wtie.service_station.repository;

import pl.edu.utp.wtie.service_station.model.Realization;

import java.util.List;

public interface RealizationDao {

    void saveRealization(Realization realization);

    List<Realization> showAllRealizations();

    Long findServiceKey(Realization realization);

    Long findEmployeeKey(Realization realization);
}
