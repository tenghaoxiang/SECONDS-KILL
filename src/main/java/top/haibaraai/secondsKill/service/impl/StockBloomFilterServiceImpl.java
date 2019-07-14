package top.haibaraai.secondsKill.service.impl;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.haibaraai.secondsKill.service.StockBloomFilterService;
import top.haibaraai.secondsKill.service.StockService;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

@Service
public class StockBloomFilterServiceImpl implements StockBloomFilterService {

    @Autowired
    private StockService stockService;

    private BloomFilter<String> bloomFilter;

    @PostConstruct
    public void initBloomFilter() {
        bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), stockService.countAll() * 1000);
    }

    @Override
    public void add(String id) {
        bloomFilter.put(id);
    }

    @Override
    public boolean isExist(String id) {
        return bloomFilter.mightContain(id);
    }
}
