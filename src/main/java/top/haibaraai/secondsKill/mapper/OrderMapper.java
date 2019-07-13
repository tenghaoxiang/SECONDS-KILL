package top.haibaraai.secondsKill.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import top.haibaraai.secondsKill.domain.Order;

import java.util.List;

public interface OrderMapper {

    /**
     * 根据主键id查找订单
     * @param id
     * @return
     */
    @Select("SELECT * FROM order WHERE id = #{id} limit 1")
    Order findById(int id);

    /**
     * 查找全部订单
     * @return
     */
    @Select("SELECT * FROM order")
    List<Order> findAll();

    /**
     * 保存订单信息
     * @param order
     * @return
     */
    @Insert("INSERT INTO `seconds_kill`.`order`" +
            "(`user_id`, `stock_id`, `address`, `price`, `status`, `create_time`, `finish_time`) " +
            "VALUES (#{userId}, #{stockId}, #{address}, #{price}, #{status}, #{createTime}, #{finishTime});")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int save(Order order);

    /**
     * 通过用户id和商品id查找订单
     * @param userId
     * @param stockId
     * @return
     */
    @Select("SELECT * FROM order WHERE id = #{stockId} AND user_id = #{userId} LIMIT 1")
    Order findByUserAndStock(int userId, int stockId);

}
