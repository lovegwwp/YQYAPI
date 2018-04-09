package com.jyss.yqy.action;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jyss.yqy.config.AliConfig;
import com.jyss.yqy.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class PayAction {

    @Autowired
    private UserAccountService userAccountService;

    private Logger logger = LoggerFactory.getLogger(PayAction.class);

    /**
     * 支付宝充值,服务端验证异步通知信息
     */
    @RequestMapping(value = "/AliNotify",method = RequestMethod.POST)
    public String updateUserBalance(HttpServletRequest request){
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            logger.info("支付宝回调信息key: " + name + ", value: " + valueStr);
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            AliConfig config = new AliConfig();
            boolean flag = AlipaySignature.rsaCheckV1(params, config.getALIPAY_PUBLIC_KEY(), "utf-8","RSA2");
            if(flag){
                String tradeStatus = request.getParameter("trade_status");
                if(tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED") ){
                    //验签成功,对支付结果中的业务内容进行1\2\3\4二次校验
                    String outTradeNo = request.getParameter("out_trade_no");
                    String totalAmount = request.getParameter("total_amount");
                    String sellerId = request.getParameter("seller_id");
                    String appId = request.getParameter("app_id");
                    if(config.getAPP_ID().equals(appId) && config.getSELLER_ID().equals(sellerId)){
                        //自己业务处理
                        Boolean balance = userAccountService.updateUserBdBalance(totalAmount, outTradeNo);
                        if(balance){
                            logger.info("支付宝服务端验证异步通知信息成功！");
                            return "success";
                        }
                    }
                }
            }
            logger.info("支付宝服务端验证异步通知信息失败！");
            return "failure";          // 验签失败

        } catch (AlipayApiException e) {
            logger.info("支付宝服务端验签发生异常！");
            return "failure";          // 验签发生异常,则直接返回失败
        }

    }



}
