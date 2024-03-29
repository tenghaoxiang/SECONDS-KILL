package top.haibaraai.secondsKill.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.haibaraai.secondsKill.domain.Stock;

import java.util.List;

public interface StockMapper {

    /**
     * 根据主键id查找商品
     * @param id 商品id
     * @return
     */
    @Select("SELECT * FROM stock WHERE id = #{id} limit 1")
    Stock findById(int id);

    /**
     * 查找全部商品信息
     * @return
     */
    @Select("SELECT * FROM stock")
    List<Stock> findAll();

    /**
     * 保存商品信息
     * @param stock
     * @return
     */
    @Insert("INSERT INTO `seconds_kill`.`stock`" +
            "(`name`, `price`, `count`, `sale`, `create_time`, `cover_img`) " +
            "VALUES (#{name}, #{price}, #{count}, #{sale}, #{createTime}, #{cover_img})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(Stock stock);

    /**
     * 根据商品id降低库存
     * @param id 商品id
     * @return
     */
    @Update("UPDATE stock SET count = count - 1, sale = sale + 1 WHERE id = #{id} limit 1")
    int decrease(int id);

    /**
     * 根据商品id降低n件库存
     * @param id 商品id
     * @param amount 商品数量
     * @return
     */
    @Update("UPDATE stock SET count = count - #{amount}, sale = sale + #{amount} WHERE id = #{id} limit 1")
    int delAmount(int id, int amount);

    /**
     * 查询参加秒杀的商品总数量
     * @return
     */
    @Select("SELECT COUNT(1) FROM stock")
    int countAll();

}
