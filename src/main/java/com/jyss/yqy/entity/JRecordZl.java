package com.jyss.yqy.entity;


import java.io.Serializable;
import java.util.Date;

public class JRecordZl implements Serializable{

    private Integer id;
    private Integer uId;     //
    private Integer zjUid;   //
    private String zjCode;
    private String zjName;
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

    public Integer getZjUid() {
        return zjUid;
    }

    public void setZjUid(Integer zjUid) {
        this.zjUid = zjUid;
    }

    public String getZjCode() {
        return zjCode;
    }

    public void setZjCode(String zjCode) {
        this.zjCode = zjCode;
    }

    public String getZjName() {
        return zjName;
    }

    public void setZjName(String zjName) {
        this.zjName = zjName;
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
