package com.jyss.yqy.config;

import java.io.InputStream;

import com.github.wxpay.sdk.WXPayConfig;

public class WXPayConfigImpl implements WXPayConfig {

	private String appId;// appID
	private String mchID;// 商户ID
	private String key;// app key

	public WXPayConfigImpl() throws Exception {
		appId = "wxb658222012acb162";
		mchID = "1489938682";
		key = "RYchinapkszmst20142017QQ10131055";
	}

	// appID
	@Override
	public String getAppID() {
		// TODO Auto-generated method stub
		return appId;
	}

	/**
	 * 获取商户证书内容
	 * 
	 * @return 商户证书内容
	 */
	@Override
	public InputStream getCertStream() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * HTTP(S) 连接超时时间，单位毫秒
	 * 
	 * @return
	 */
	@Override
	public int getHttpConnectTimeoutMs() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * HTTP(S) 读数据超时时间，单位毫秒
	 * 
	 * @return
	 */
	@Override
	public int getHttpReadTimeoutMs() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 获取 API 密钥
	 * 
	 * @return API密钥
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	// 商户ID
	@Override
	public String getMchID() {
		// TODO Auto-generated method stub
		return mchID;
	}

}
