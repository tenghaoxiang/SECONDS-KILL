package top.haibaraai.secondsKill.service;

import top.haibaraai.secondsKill.domain.Order;

import java.util.List;

public interface OrderService {

    Order findById(int id);

    List<Order> findAll();

    int save(Order order);

    Order findByUserAndStock(int userId, int stockId);

}
