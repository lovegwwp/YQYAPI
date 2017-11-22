package com.jyss.yqy.entity;

import java.util.Date;

public class JBonusFdj {
    private Integer id;     

    private Integer uId;     //用户的id

    private String uName;     //用户的真实名字

    private Integer parentId;   //推荐人id

    private Double amount;       //被推荐消费额

    private Double parentMoney;    //推荐人获得的金额

    private Integer status;      //状态   0禁用 1正常

    private String created;       //时间
 
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

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName == null ? null : uName.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getParentMoney() {
        return parentMoney;
    }

    public void setParentMoney(Double parentMoney) {
        this.parentMoney = parentMoney;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}