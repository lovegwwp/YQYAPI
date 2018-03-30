package com.jyss.yqy.entity.jsonEntity;

import java.util.Date;

public class UserBean {
	private int id;
	private String uuid;
	private String salt;
	private String pwd;
	private String account;// 账号
	private String nick;// 昵称
	private String status;// -1删除 2禁用 1正常
	private String avatar;// 头像
	private int sex;// 1=男 2=女
	private int age;
	private String realName;// 真实姓名
	private String idCard;// 身份证号
	private int isChuangke;// 0普通会员1代言人2初级代理人 3中级代理人4高级代理人
	private int isAuth;// 1=已提交 2=审核通过3=审核不通过
	private String bCode;// 推广码
	private String token;//
	private String payPwd;// 支付密码
	private float cashScore;// 现金积分
	private float shoppingScore;// 购物积分
	private String provinceId;//
	private String province;//
	private String cityId;//
	private String city;//
	private String areaId;//
	private String area;//
	private Float totalPv;      //分红权
	private Date createdAt;
	private Date lastAccessTime;

	private Float totalAmount;  //首次消费额
	private Float electScore;   //电子券金额
	private Float bdScore;      //报单券金额
	private Integer cjStatus;    //层奖

	private Integer isTransfer;    //1可转，2不可转（账户转账）
	private Float borrow;  //借贷金额

	// private Date createdAt;//
	// private int lastAccessTime;//

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public float getShoppingScore() {
		return shoppingScore;
	}

	public void setShoppingScore(float shoppingScore) {
		this.shoppingScore = shoppingScore;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getId() {
		return id;
	}

	public float getCashScore() {
		return cashScore;
	}

	public void setCashScore(float cashScore) {
		this.cashScore = cashScore;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	// public Date getCreatedAt() {
	// return createdAt;
	// }
	//
	// public void setCreatedAt(Date createdAt) {
	// this.createdAt = createdAt;
	// }
	//
	// public int getLastAccessTime() {
	// return lastAccessTime;
	// }
	//
	// public void setLastAccessTime(int lastAccessTime) {
	// this.lastAccessTime = lastAccessTime;
	// }

	public String getUuid() {
		return uuid;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getIsChuangke() {
		return isChuangke;
	}

	public void setIsChuangke(int isChuangke) {
		this.isChuangke = isChuangke;
	}

	public int getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(int isAuth) {
		this.isAuth = isAuth;
	}

	public String getbCode() {
		return bCode;
	}

	public void setbCode(String bCode) {
		this.bCode = bCode;
	}

	public Float getTotalPv() {
		return totalPv;
	}

	public void setTotalPv(Float totalPv) {
		this.totalPv = totalPv;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Float getElectScore() {
		return electScore;
	}

	public void setElectScore(Float electScore) {
		this.electScore = electScore;
	}

	public Float getBdScore() {
		return bdScore;
	}

	public void setBdScore(Float bdScore) {
		this.bdScore = bdScore;
	}

	public Integer getCjStatus() {
		return cjStatus;
	}

	public void setCjStatus(Integer cjStatus) {
		this.cjStatus = cjStatus;
	}

	public Integer getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(Integer isTransfer) {
		this.isTransfer = isTransfer;
	}

	public Float getBorrow() {
		return borrow;
	}

	public void setBorrow(Float borrow) {
		this.borrow = borrow;
	}
}
