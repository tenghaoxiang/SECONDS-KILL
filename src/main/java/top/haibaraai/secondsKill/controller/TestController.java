package top.haibaraai.secondsKill.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.haibaraai.secondsKill.mapper.UserMapper;

@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private UserMapper userMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/log")
    public void testLog() {
        //将不被记录和输出，控制台默认为info级别
        logger.debug("this is a debug level");
        logger.info("this is a info level");
        logger.warn("this is a warn level");
        logger.error("this is a error level");
    }

    @GetMapping("/sql")
    public void testSql(@RequestParam(value = "id") int id) {
        if (userMapper.findByWxId(id) == null) {
            logger.info("数据库为空");
        }
    }

}
