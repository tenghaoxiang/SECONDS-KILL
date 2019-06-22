package top.haibaraai.secondsKill.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import top.haibaraai.secondsKill.domain.User;

import java.util.List;

public interface UserMapper {

    /**
     * 根据主键id查询用户
     * @param id
     * @return
     */
    @Select("SELECT * FROM user WHERE id = #{id} limit 1")
    User findById(int id);

    /**
     * 根据微信id查询用户
     * @param id 用户微信id
     * @return
     */
    @Select("SELECT * FROM user WHERE wx_id = #{id} limit 1")
    List<User> findByWxId(int id);

    /**
     * 查询全部用户
     * @return
     */
    @Select("SELECT * FROM user")
    User findAll();

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    @Insert("INSERT INTO `seconds_kill`.`user`" +
            "(`wx_id`, `name`, `head_img`, `sex`, `create_time`, `address`) " +
            "VALUES (#{wxId}, #{name}, #{headImg}, #{sex}, #{createTime}, #{address})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(User user);

}
