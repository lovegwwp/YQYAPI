package com.jyss.yqy.config;

import com.jyss.yqy.constant.Constant;

/**
 * Created by lixh on 2018/4/20.
 */
public class ConfigUtil {

    /**
     * 服务号相关信息
     */
    public final static String APPID = "***";//服务号的应用号
    public final static String MCH_ID = "***";//商户号
    public final static String API_KEY = "***";//API密钥
    public final static String SIGN_TYPE = "MD5";//签名加密方式
    public final static String SPBILL_CREATE_IP = "121.40.29.64";//服务器IP
    public final static String NOTIFY_URL = Constant.httpUrl + "YQYAPI/WxNotify.action";
    public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
}
