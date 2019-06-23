package top.haibaraai.secondsKill.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.haibaraai.secondsKill.domain.JsonData;
import top.haibaraai.secondsKill.domain.Order;
import top.haibaraai.secondsKill.domain.Stock;
import top.haibaraai.secondsKill.domain.User;
import top.haibaraai.secondsKill.service.OrderService;
import top.haibaraai.secondsKill.service.StockService;
import top.haibaraai.secondsKill.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/stock")
@RestController
public class StockController extends BasicController {

    @Autowired
    private StockService stockService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * 增加商品
     * @param name 商品名字
     * @param price 商品价格
     * @param count 商品数量
     * @param coverImg 商品封面
     * @return
     */
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

    /**
     * 获取全部商品列表
     * @param page
     * @param size
     * @return
     */
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

    /**
     * 下单接口
     * @param token 解析用户
     * @param id 商品id
     * @return
     */
    @GetMapping("/decrease")
    public JsonData decrease(@RequestParam(value = "token") String token,
                             @RequestParam(value = "id") int id) {
        User user = userService.findById(1);
        Stock stock = stockService.findById(id);
        if (stock.getCount() >= 1) {
            logger.info("==============开始秒杀==============");
            stockService.decrease(id);
            Order order = new Order();
            order.setUserId(user.getId());
            order.setStockId(id);
            order.setAddress(user.getAddress());
            order.setPrice(stock.getPrice());
            order.setStatus(1);
            order.setCreateTime(new Date());
            order.setFinishTime(new Date());
            orderService.save(order);
            logger.info("==============秒杀成功==============");
        } else {
            logger.info("==============库存不足==============");
            return error("null", "库存不足!");
        }
        return success(null, "秒杀成功!");
    }

}
