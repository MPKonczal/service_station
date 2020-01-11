package pl.edu.utp.wtie.service_station.repository;

import pl.edu.utp.wtie.service_station.model.Order;

import java.util.List;

public interface OrderDao {

    void saveOrder(Order order);

    List<Order> showAllOrders();

    List<Long> showAllOrderNumbers();

    String showOrder(long id);

    void updateOrder(Order newOrder);
}
