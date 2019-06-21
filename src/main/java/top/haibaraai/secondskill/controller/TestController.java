package top.haibaraai.secondskill.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/log")
    public void testLog() {
        //将不被记录和输出，控制台默认为info级别
        logger.debug("this is a debug level");
        logger.info("this is a info level");
        logger.warn("this is a warn level");
        logger.error("this is a error level");
    }

}
