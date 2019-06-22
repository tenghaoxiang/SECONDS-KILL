package top.haibaraai.secondsKill.domain;

/**
 * 返回前端的信息
 */
public class JsonData {

    /**
     * 状态码
     * 0 表示成功
     * -1 表示失败
     */
    private Integer code;

    /**
     * 数据内容
     */
    private Object data;

    /**
     * 描述信息
     */
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonData() {

    }

    public JsonData(Integer code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{code=" + code + ", data=" + data + ", message=" + message + "}";
    }

}
