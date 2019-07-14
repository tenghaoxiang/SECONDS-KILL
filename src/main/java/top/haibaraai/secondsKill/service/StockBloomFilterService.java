package top.haibaraai.secondsKill.service;

public interface StockBloomFilterService {

    void add(String id);

    boolean isExist(String id);

}
