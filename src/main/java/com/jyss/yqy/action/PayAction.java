package com.jyss.yqy.action;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jyss.yqy.config.AliConfig;
import com.jyss.yqy.config.ConfigUtil;
import com.jyss.yqy.config.PayCommonUtil;
import com.jyss.yqy.config.XMLUtil;
import com.jyss.yqy.service.UserAccountService;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

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

    /*@RequestMapping(value = "/AliNotify",method = RequestMethod.POST)
    public String updateUserBalance(@RequestParam("totalAmount")String totalAmount,@RequestParam("outTradeNo")String outTradeNo){
        Boolean balance = userAccountService.updateUserBdBalance(totalAmount, outTradeNo);
        if(balance){

            return "success";
        }
        return "false";

    }*/

    /**
     * 微信异步通知

     */
    @RequestMapping(value ="/WxNotify",method = RequestMethod.POST)
    public void wxNotify(HttpServletRequest request,HttpServletResponse response) throws IOException, JDOMException {
        //读取参数
        InputStream inputStream ;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s ;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null){
            sb.append(s);
        }
        in.close();
        inputStream.close();
        //解析xml成map
        Map<String, String> m = new HashMap<String, String>();
        m = XMLUtil.doXMLParse(sb.toString());
        for(Object keyValue : m.keySet()){
            System.out.println(keyValue+"="+m.get(keyValue));
        }
        //过滤空 设置 TreeMap
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);

            String v = "";
            if(null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }

        //判断签名是否正确
        String resXml = "";
        if(PayCommonUtil.isTenpaySign("UTF-8", packageParams)) {
            if("SUCCESS".equals((String)packageParams.get("result_code"))){
                // 这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String)packageParams.get("mch_id"); //商户号
                String openid = (String)packageParams.get("openid");  //用户标识
                String out_trade_no = (String)packageParams.get("out_trade_no"); //商户订单号
                String total_fee = (String)packageParams.get("total_fee");
                String transaction_id = (String)packageParams.get("transaction_id"); //微信支付订单号

                if(!ConfigUtil.MCH_ID.equals(mch_id)){
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                }else{
                        //订单状态的修改。根据实际业务逻辑执行
                        //自己业务处理
                        Boolean balance = userAccountService.updateUserBdBalance(total_fee, out_trade_no);
                        if(balance){
                            logger.info("微信服务端验证异步通知信息成功！");
                        }else{
                            logger.info("微信服务端验证异步通知信息失败！");
                        }

                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    }

            }else {
                logger.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }


        } else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();

    }

}
