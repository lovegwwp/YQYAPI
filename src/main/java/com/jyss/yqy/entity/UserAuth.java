package com.jyss.yqy.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class UserAuth implements Serializable{
	private int id;
	private String uUuid;
	private String realName;// 真实姓名
	private String email;// 邮箱
	private String idCard;// 身份证
	private int status;// 0=审核中 1=通过 2=未通过
	private String cardPicture1;// 身份份正图片
	private String cardPicture2;// 身份份正图片
	private String cardPicture3;// 身份份正图片
	//@DateTimeFormat(pattern="yyyy-MM-dd")  
	private Date validityDate;// 身份证有效日期
	private Date createdAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getuUuid() {
		return uUuid;
	}

	public void setuUuid(String uUuid) {
		this.uUuid = uUuid;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int i) {
		this.status = i;
	}

	public String getCardPicture1() {
		return cardPicture1;
	}

	public void setCardPicture1(String cardPicture1) {
		this.cardPicture1 = cardPicture1;
	}

	public String getCardPicture2() {
		return cardPicture2;
	}

	public void setCardPicture2(String cardPicture2) {
		this.cardPicture2 = cardPicture2;
	}

	public String getCardPicture3() {
		return cardPicture3;
	}

	public void setCardPicture3(String cardPicture3) {
		this.cardPicture3 = cardPicture3;
	}

	public Date getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
