package top.haibaraai.secondsKill.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import top.haibaraai.secondsKill.domain.Stock;

import java.util.List;

public interface StockMapper {

    /**
     * 根据主键id查找商品
     * @param id
     * @return
     */
    @Select("SELECT * FROM stock WHERE id = #{id}")
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
            "(`name`, `price`, `count`, `sale`, `create_time`) " +
            "VALUES (#{name}, #{price}, #{count}, #{sale}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(Stock stock);

}
