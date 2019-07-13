package top.haibaraai.secondsKill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.haibaraai.secondsKill.domain.Order;
import top.haibaraai.secondsKill.mapper.OrderMapper;
import top.haibaraai.secondsKill.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order findById(int id) {
        return orderMapper.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderMapper.findAll();
    }

    @Override
    public int save(Order order) {
        return orderMapper.save(order);
    }

    @Override
    public Order findByUserAndStock(int userId, int stockId) {
        return orderMapper.findByUserAndStock(userId, stockId);
    }
}
