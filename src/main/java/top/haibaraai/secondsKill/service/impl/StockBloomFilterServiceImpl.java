package top.haibaraai.secondsKill.service.impl;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.haibaraai.secondsKill.service.StockBloomFilterService;
import top.haibaraai.secondsKill.service.StockService;

import javax.annotation.PostConstruct;

@Service
public class StockBloomFilterServiceImpl implements StockBloomFilterService {

    @Autowired
    private StockService stockService;

    private BloomFilter<Integer> bloomFilter;

    @PostConstruct
    public void initBloomFilter() {
        bloomFilter = BloomFilter.create(Funnels.integerFunnel(), stockService.countAll());
    }

    @Override
    public void add(int id) {
        bloomFilter.put(id);
    }

    @Override
    public boolean isExist(int id) {
        return bloomFilter.mightContain(id);
    }
}
