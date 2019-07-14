package top.haibaraai.secondsKill.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 判断标志
 */
public interface FlagMapper {

    @Select("SELECT flag FROM flag WHERE queue = #{queue} LIMIT 1")
    int selectByQueue(String queue);

    @Insert("INSERT INTO `seconds_kill`.`flag`(`queue`, `flag`) VALUES (#{queue}, #{flag});")
//    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int save(String queue, int flag);

    @Update("UPDATE flag SET flag = #{flag} WHERE queue = #{queue} LIMIT 1")
    int update(String queue, int flag);

}
