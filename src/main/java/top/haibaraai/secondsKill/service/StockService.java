package top.haibaraai.secondsKill.service;

import top.haibaraai.secondsKill.domain.Stock;

import java.util.List;

public interface StockService {

    Stock findById(int id);

    List<Stock> findAll();

    int save(Stock stock);

    int decrease(int id);

    int delAmount(int id, int amount);

}
