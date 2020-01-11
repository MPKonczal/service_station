package pl.edu.utp.wtie.service_station.repository;

import java.util.List;

public interface ServiceCategoryDao {

    List<String> showAllServiceCategories();

    List<String> showServiceSubcategories(Long idCategory);

    Long findServiceCategoryKey(String category);
}
