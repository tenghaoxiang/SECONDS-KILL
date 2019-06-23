package top.haibaraai.secondsKill.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.haibaraai.secondsKill.domain.JsonData;
import top.haibaraai.secondsKill.domain.Stock;
import top.haibaraai.secondsKill.service.StockService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/stock")
@RestController
public class StockController extends BasicController {

    @Autowired
    private StockService stockService;

    @PutMapping("/add")
    public JsonData addStock(@RequestParam(value = "name") String name,
                             @RequestParam(value = "price") double price,
                             @RequestParam(value = "count") int count,
                             @RequestParam(value = "cover_img") String coverImg) {
        logger.info("===========开始插入商品信息============");
        Stock stock = new Stock();
        stock.setName(name);
        stock.setPrice(price);
        stock.setCount(count);
        stock.setSale(0);
        stock.setCoverImg(coverImg);
        stock.setCreateTime(new Date());
        int rows = stockService.save(stock);
        logger.info("===========插入成功============");
        return success(rows);
    }

    @GetMapping("/list")
    public Object addStock(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        List<Stock> stockList = stockService.findAll();
        PageInfo<Stock> pageInfo = new PageInfo<>(stockList);
        Map<String, Object> data = new HashMap<>();
        data.put("total_size",pageInfo.getTotal());//总条数
        data.put("total_page",pageInfo.getPages());//总页数
        data.put("current_page",page);//当前页
        data.put("data",pageInfo.getList());//数据
        return data;
    }

}
