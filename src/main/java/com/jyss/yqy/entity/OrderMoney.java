package com.jyss.yqy.entity;


import java.io.Serializable;
import java.util.Date;

public class OrderMoney implements Serializable{

    private Integer id;
    private Integer uId;     //用户id
    private String orderSn;    //订单号
    private Integer type;     //1=收入 2=支出
    private Float score;
    private Float jyScore;
    private Integer payType;     //1=支付宝，2=微信，3=线下pos机
    private String detail;      //描述
    private Integer status;
    private Date createTime;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Float getJyScore() {
        return jyScore;
    }

    public void setJyScore(Float jyScore) {
        this.jyScore = jyScore;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
