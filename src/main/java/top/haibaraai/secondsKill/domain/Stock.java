package top.haibaraai.secondsKill.domain;

import java.io.Serializable;
import java.util.Date;

public class Stock implements Serializable {

    private static final long serialVersionUID = 376497450923734L;

    /**
     * 商品id
     */
    private Integer id;

    /**
     * 商品名字
     */
    private String name;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 商品库存量
     */
    private  Integer count;

    /**
     * 商品销售量
     */
    private Integer sale;

    /**
     * 商品创建时间
     */
    private Date createTime;

    /**
     * 商品封面
     */
    private String coverImg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }
}
