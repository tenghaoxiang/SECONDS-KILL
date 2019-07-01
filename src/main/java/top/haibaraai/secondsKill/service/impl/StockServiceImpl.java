package top.haibaraai.secondsKill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.haibaraai.secondsKill.domain.Stock;
import top.haibaraai.secondsKill.mapper.StockMapper;
import top.haibaraai.secondsKill.service.StockService;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public Stock findById(int id) {
        return stockMapper.findById(id);
    }

    @Override
    public List<Stock> findAll() {
        return stockMapper.findAll();
    }

    @Override
    public int save(Stock stock) {
        return stockMapper.save(stock);
    }

    @Override
    public int decrease(int id) {
        return stockMapper.decrease(id);
    }

    @Override
    public int delAmount(int id, int amount) {
        return stockMapper.delAmount(id, amount);
    }

    @Override
    public int countAll() {
        return stockMapper.countAll();
    }

}
