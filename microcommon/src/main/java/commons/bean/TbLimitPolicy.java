package commons.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TbLimitPolicy {
    private Long id;

    private Long skuId;

    private Long quanty;

    private Long price;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public TbSku getSku() {
        return sku;
    }

    public void setSku(TbSku sku) {
        this.sku = sku;
    }

    private TbSku sku;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getQuanty() {
        return quanty;
    }

    public void setQuanty(Long quanty) {
        this.quanty = quanty;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}