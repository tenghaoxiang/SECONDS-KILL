package top.haibaraai.secondsKill.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.haibaraai.secondsKill.domain.JsonData;

public class BasicController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public JsonData success() {
        return new JsonData(0, null, "成功");
    }

    public JsonData success(Object data) {
        return new JsonData(0, data, "成功");
    }

    public JsonData success(Object data, String message) {
        return new JsonData(0, data, message);
    }

    public JsonData error() {
        return new JsonData(-1, null, "失败");
    }

    public JsonData error(Object data) {
        return new JsonData(-1, data, "失败");
    }

    public JsonData error(Object data, String message) {
        return new JsonData(-1, data, message);
    }

}
