package top.haibaraai.secondsKill.domain;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = 5673245838934709L;

    /**
     * 数据库主键id
     */
    private Integer id;

    /**
     * 微信id
     */
    private String wxId;

    /**
     * 名字
     */
    private String name;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 性别
     * 0 未定义
     * 1 男性
     * 2 女性
     */
    private String sex;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 地址
     */
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
