package com.jyss.yqy.entity;

import java.util.Date;

public class User {
	private int id;
	private String uuid;
	private String account;// 账号
	private String pwd;// 密码
	private String salt;// 盐值
	private String nick;// 昵称
	private String status;// -1删除 2禁用 1正常
	private float integral;// 积分
	private float currency;// 账户余额
	private String avatar;// 头像
	private String rUuid1;// 推荐人
	private String rUuid2;
	private String rUuid3;
	private int provinceId;// 区域信息
	private String province;
	private int cityId;
	private String city;
	private int areaId;
	private String area;
	private int sex;// 1=男 2=女
	private int age;
	private String realName;// 真实姓名
	private String uid;
	private String payPwd;// 支付密码
	private int isChuangke;// 0普通会员1代言人2初级代理人 3中级代理人4高级代理人
	private int isAuth;// 1=已提交 2=审核通过
	private int from;// 1=APP 2=WAP
	private int isPay;// 1=已支付
	private Date birthDate;// 出生日期
	private Date createdAt;
	private Date lastLoginTime;
	private Date lastModifyTime;
	private String qrCode;// 分享二维码
	private String code;// 推广码
	private float money;// 赚取的金额
	private float chuangkeMoney;// 创客总金额
	private int chuangkeR1;// 我的一级创客数量
	private int chuangkeR2;// 我的二级创客数量
	private int chuangkeR3;// 我的三级创客数量
	private float chuangkeR1Money;// 一级创客赚取的佣金
	private float chuangkeR2Money;// 二级创客赚取的佣金
	private float chuangkeR3Money;// 三级创客赚取的佣金
	private String dUuid1;// 一级代言人
	private String dUuid2;// 二级代言人
	private String dUuid3;// 三级代言人
	private int dR1;// 我的一级代言人数量
	private int dR2;// 我的二级代言人数量
	private int dR3;// 我的三级代言人数量
	private float dR1Money;
	private float dR2Money;
	private float dR3Money;
	private float dMoney;// 代言人赚取的总额
	private float dRMoney;// 总销售额
	private float rShopMoney;
	private String aliAccount;// 支付宝账号
	private String aliName;// 支付宝姓名

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

	public float getIntegral() {
		return integral;
	}

	public void setIntegral(float integral) {
		this.integral = integral;
	}

	public float getCurrency() {
		return currency;
	}

	public void setCurrency(float currency) {
		this.currency = currency;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getrUuid1() {
		return rUuid1;
	}

	public void setrUuid1(String rUuid1) {
		this.rUuid1 = rUuid1;
	}

	public String getrUuid2() {
		return rUuid2;
	}

	public void setrUuid2(String rUuid2) {
		this.rUuid2 = rUuid2;
	}

	public String getrUuid3() {
		return rUuid3;
	}

	public void setrUuid3(String rUuid3) {
		this.rUuid3 = rUuid3;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
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

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public float getChuangkeMoney() {
		return chuangkeMoney;
	}

	public void setChuangkeMoney(float chuangkeMoney) {
		this.chuangkeMoney = chuangkeMoney;
	}

	public int getChuangkeR1() {
		return chuangkeR1;
	}

	public void setChuangkeR1(int chuangkeR1) {
		this.chuangkeR1 = chuangkeR1;
	}

	public int getChuangkeR2() {
		return chuangkeR2;
	}

	public void setChuangkeR2(int chuangkeR2) {
		this.chuangkeR2 = chuangkeR2;
	}

	public int getChuangkeR3() {
		return chuangkeR3;
	}

	public void setChuangkeR3(int chuangkeR3) {
		this.chuangkeR3 = chuangkeR3;
	}

	public float getChuangkeR1Money() {
		return chuangkeR1Money;
	}

	public void setChuangkeR1Money(float chuangkeR1Money) {
		this.chuangkeR1Money = chuangkeR1Money;
	}

	public float getChuangkeR2Money() {
		return chuangkeR2Money;
	}

	public void setChuangkeR2Money(float chuangkeR2Money) {
		this.chuangkeR2Money = chuangkeR2Money;
	}

	public float getChuangkeR3Money() {
		return chuangkeR3Money;
	}

	public void setChuangkeR3Money(float chuangkeR3Money) {
		this.chuangkeR3Money = chuangkeR3Money;
	}

	public String getdUuid1() {
		return dUuid1;
	}

	public void setdUuid1(String dUuid1) {
		this.dUuid1 = dUuid1;
	}

	public String getdUuid2() {
		return dUuid2;
	}

	public void setdUuid2(String dUuid2) {
		this.dUuid2 = dUuid2;
	}

	public String getdUuid3() {
		return dUuid3;
	}

	public void setdUuid3(String dUuid3) {
		this.dUuid3 = dUuid3;
	}

	public int getdR1() {
		return dR1;
	}

	public void setdR1(int dR1) {
		this.dR1 = dR1;
	}

	public int getdR2() {
		return dR2;
	}

	public void setdR2(int dR2) {
		this.dR2 = dR2;
	}

	public int getdR3() {
		return dR3;
	}

	public void setdR3(int dR3) {
		this.dR3 = dR3;
	}

	public float getdR1Money() {
		return dR1Money;
	}

	public void setdR1Money(float dR1Money) {
		this.dR1Money = dR1Money;
	}

	public float getdR2Money() {
		return dR2Money;
	}

	public void setdR2Money(float dR2Money) {
		this.dR2Money = dR2Money;
	}

	public float getdR3Money() {
		return dR3Money;
	}

	public void setdR3Money(float dR3Money) {
		this.dR3Money = dR3Money;
	}

	public float getdMoney() {
		return dMoney;
	}

	public void setdMoney(float dMoney) {
		this.dMoney = dMoney;
	}

	public float getdRMoney() {
		return dRMoney;
	}

	public void setdRMoney(float dRMoney) {
		this.dRMoney = dRMoney;
	}

	public float getrShopMoney() {
		return rShopMoney;
	}

	public void setrShopMoney(float rShopMoney) {
		this.rShopMoney = rShopMoney;
	}

	public String getAliAccount() {
		return aliAccount;
	}

	public void setAliAccount(String aliAccount) {
		this.aliAccount = aliAccount;
	}

	public String getAliName() {
		return aliName;
	}

	public void setAliName(String aliName) {
		this.aliName = aliName;
	}

}
