package com.jyss.yqy.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.config.AliConfig;
import com.jyss.yqy.config.ConfigUtil;
import com.jyss.yqy.config.PayCommonUtil;
import com.jyss.yqy.config.XMLUtil;
import com.jyss.yqy.constant.Constant;
import com.jyss.yqy.entity.Page;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.UserAccountService;
import org.apache.ibatis.annotations.Param;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ScoreBalanceMapper scoreBalanceMapper;
    @Autowired
    private XtclMapper xtclMapper;


    /**
     * 充值报单券
     */
    @Override
    public Map<String, Object> getALiPayResult(String uuid, String bzType, String bzId) {
        Map<String, Object> map = new HashMap<String, Object>();

        //后台报单券总池余额
        Xtcl xtcl1 = xtclMapper.getClsValue("bdqzc_type", "1");        //后台报单券总池余额
        double double1 = Double.parseDouble(xtcl1.getBz_value());                   //20000000
        Xtcl xtcl2 = xtclMapper.getClsValue(bzType, bzId);                          //报单券充值套餐
        if (xtcl1 == null || xtcl2 == null) {
            map.put("code", "-1");
            map.put("status", "false");
            map.put("message", "支付异常");
            map.put("data", "");
            return map;
        }
        double payAmount = Double.parseDouble(xtcl2.getBz_value());                 //

        if (payAmount < double1) {

            List<UserBean> userBeans = userMapper.getUserByUuid(uuid);
            if (userBeans != null && userBeans.size() == 1) {
                UserBean userBean = userBeans.get(0);

                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
                String outTradeNo = sdf.format(new Date()) + "O" + userBean.getId()
                        + "r" + (long) (Math.random() * 1000L);
                String subject = "易起云报单券充值";
                String totalAmount = payAmount + "";
                // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
                String body = "报单券充值" + payAmount + "元";

                String timeoutExpress = "30m";   // 支付超时，定义为30分钟
                String notifyUrl = Constant.httpUrl + "YQYAPI/AliNotify.action";

                AliConfig config = new AliConfig();

                //实例化客户端
                AlipayClient alipayClient = new DefaultAlipayClient(config.getURL(), config.getAPP_ID(), config.getAPP_PRIVATE_KEY(),
                        "json", "UTF-8", config.getALIPAY_PUBLIC_KEY(), config.getSIGN_TYPE());

                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(body);
                model.setSubject(subject);
                model.setOutTradeNo(outTradeNo);
                model.setTimeoutExpress(timeoutExpress);
                model.setTotalAmount(totalAmount);
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(notifyUrl);
                try {
                    //这里和普通的接口调用不同，使用的是sdkExecute
                    AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                    //System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。

                    //创建订单
                    ScoreBalance scoreBalance = new ScoreBalance();
                    scoreBalance.setEnd(2);
                    scoreBalance.setuUuid(uuid);
                    scoreBalance.setCategory(11);
                    scoreBalance.setSecoCate(1);
                    scoreBalance.setType(1);
                    scoreBalance.setScore((float) payAmount);
                    scoreBalance.setJyScore((float) (userBean.getBdScore() + payAmount));
                    scoreBalance.setOrderSn(outTradeNo);
                    scoreBalance.setStatus(0);
                    int count = scoreBalanceMapper.insertEntryScore(scoreBalance);
                    if (count == 1) {
                        map.put("code", "0");
                        map.put("status", "true");
                        map.put("message", "下单成功");
                        map.put("data", response.getBody());
                        return map;
                    }
                } catch (AlipayApiException e) {
                    map.put("code", "-1");
                    map.put("status", "false");
                    map.put("message", "支付异常");
                    map.put("data", "");
                    return map;
                }
            }
            map.put("code", "-2");
            map.put("status", "false");
            map.put("message", "请重新登陆");
            map.put("data", "");
            return map;
        }
        map.put("code", "-3");
        map.put("status", "false");
        map.put("message", "无剩余报单券可购买");
        map.put("data", "");
        return map;

    }

    ////微信支付
    @Override
    public Map<String, Object> geWxPayResult(@Param("zzType") String uuid, @Param("bzType") String bzType, @Param("bzId") String bzId) {
        Map<String, Object> map = new HashMap<String, Object>();

        //后台报单券总池余额
        Xtcl xtcl1 = xtclMapper.getClsValue("bdqzc_type", "1");//后台报单券总池余额
        //报单券剩余额度
        double double1 = Double.parseDouble(xtcl1.getBz_value());                   //20000000
        Xtcl xtcl2 = xtclMapper.getClsValue(bzType, bzId);                          //报单券充值套餐
        if (xtcl1 == null || xtcl2 == null) {
            map.put("code", "-1");
            map.put("status", "false");
            map.put("message", "支付异常");
            map.put("data", "");
            return map;
        }
        ////支付金额
        double payAmount = Double.parseDouble(xtcl2.getBz_value());                 //

        if (payAmount < double1) {

            List<UserBean> userBeans = userMapper.getUserByUuid(uuid);
            if (userBeans != null && userBeans.size() == 1) {
                UserBean userBean = userBeans.get(0);

                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
                String outTradeNo = sdf.format(new Date()) + "O" + userBean.getId()
                        + "r" + (long) (Math.random() * 1000L);
                String subject = "易起云报单券充值";
                String totalAmount = payAmount + "";
                // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
                String body = "报单券充值" + payAmount + "元";

                String timeoutExpress = "30m";   // 支付超时，定义为30分钟
                String notifyUrl = Constant.httpUrl + "YQYAPI/AliNotify.action";

                Map<String, Object> resultMap = new HashMap<String, Object>();
                if (payAmount <= 0) {
                    map.put("code", "-1");
                    map.put("status", "false");
                    map.put("message", "付款金额错误");
                    map.put("data", "");
                    return map;
                }

                SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
                parameters.put("appid", ConfigUtil.APPID);
                parameters.put("mch_id", ConfigUtil.MCH_ID);
                parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
                parameters.put("body", subject);
                parameters.put("out_trade_no", outTradeNo); //订单id
                parameters.put("fee_type", "CNY");
                parameters.put("total_fee", String.valueOf(payAmount * 100));
                parameters.put("spbill_create_ip", ConfigUtil.SPBILL_CREATE_IP);
                parameters.put("notify_url", ConfigUtil.NOTIFY_URL);
                parameters.put("trade_type", "APP");
                //设置签名
                String sign = PayCommonUtil.createSign("UTF-8", parameters);
                parameters.put("sign", sign);
                //封装请求参数结束
                String requestXML = PayCommonUtil.getRequestXml(parameters);
                //调用统一下单接口
                String result = PayCommonUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL, "POST", requestXML);
                System.out.println("\n" + result);
                String returnCoede = "";
                SortedMap<Object, Object> parameterMap2 = new TreeMap<Object, Object>();
                try {
                    /**统一下单接口返回正常的prepay_id，再按签名规范重新生成签名后，将数据传输给APP。参与签名的字段名为appId，partnerId，prepayId，nonceStr，timeStamp，package。注意：package的值格式为Sign=WXPay**/
                    Map<String, String> rem = XMLUtil.doXMLParse(result);
                    parameterMap2.put("appid", ConfigUtil.APPID);
                    parameterMap2.put("partnerid", ConfigUtil.MCH_ID);
                    parameterMap2.put("prepayid", rem.get("prepay_id"));
                    parameterMap2.put("package2", "Sign=WXPay");
                    parameterMap2.put("noncestr", PayCommonUtil.CreateNoncestr());
                    //本来生成的时间戳是13位，但是ios必须是10位，所以截取了一下
                    parameterMap2.put("timestamp", Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0, 10)));
                    String sign2 = PayCommonUtil.createSign("UTF-8", parameterMap2);
                    parameterMap2.put("sign", sign2);
                    returnCoede = rem.get("return_code");
                   /* resultMap.put("code","200");
                    resultMap.put("msg",parameterMap2);*/
                } catch (JDOMException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //  return resultMap;
                if (returnCoede.equals("SUCCESS")) {

                    //创建订单
                    ScoreBalance scoreBalance = new ScoreBalance();
                    scoreBalance.setEnd(2);
                    scoreBalance.setuUuid(uuid);
                    scoreBalance.setCategory(11);
                    scoreBalance.setSecoCate(1);
                    scoreBalance.setType(1);
                    scoreBalance.setScore((float) payAmount);
                    scoreBalance.setJyScore((float) (userBean.getBdScore() + payAmount));
                    scoreBalance.setOrderSn(outTradeNo);
                    scoreBalance.setStatus(0);
                    int count = scoreBalanceMapper.insertEntryScore(scoreBalance);
                    if (count == 1) {
                        map.put("code", "0");
                        map.put("status", "true");
                        map.put("message", "下单成功");
                        map.put("data", parameterMap2);
                        return map;
                    }
                }
                map.put("code", "-3");
                map.put("status", "false");
                map.put("message", "下单失败！");
                map.put("data", "");
                return map;

            }
            map.put("code", "-2");
            map.put("status", "false");
            map.put("message", "请重新登陆");
            map.put("data", "");
            return map;
        }
        map.put("code", "-3");
        map.put("status", "false");
        map.put("message", "无剩余报单券可购买");
        map.put("data", "");
        return map;
    }


    /**
     * 充值结果异步处理
     */
    @Override
    public boolean updateUserBdBalance(String totalAmount, String outTradeNo) {
        //后台报单券总池余额
        Xtcl xtcl1 = xtclMapper.getClsValue("bdqzc_type", "1");        //后台报单券总池余额
        //double double1 = Double.parseDouble(xtcl1.getBz_value());                   //20000000

        BigDecimal bigDecimal1 = new BigDecimal(xtcl1.getBz_value());
        BigDecimal bigDecimal2 = new BigDecimal(totalAmount);

        List<ScoreBalance> balanceList = scoreBalanceMapper.selectEntryScore(outTradeNo);
        if (balanceList != null && balanceList.size() == 1) {
            ScoreBalance scoreBalance = balanceList.get(0);
            float money = Float.parseFloat(totalAmount);
            if (money == scoreBalance.getScore()) {
                List<UserBean> userBeans = userMapper.getUserByUuid(scoreBalance.getuUuid());
                if (userBeans != null && userBeans.size() == 1) {
                    UserBean userBean = userBeans.get(0);
                    //修改报单券
                    UserBean userBean1 = new UserBean();
                    userBean1.setId(userBean.getId());
                    userBean1.setBdScore(userBean.getBdScore() + scoreBalance.getScore());
                    userMapper.updateScore(userBean1);
                    //修改充值状态
                    ScoreBalance scoreBalance1 = new ScoreBalance();
                    scoreBalance1.setId(scoreBalance.getId());
                    scoreBalance1.setStatus(1);
                    int count = scoreBalanceMapper.updateEntryScore(scoreBalance1);
                    if (count == 1) {
                        //减总池
                        BigDecimal bigDecimal3 = bigDecimal1.subtract(bigDecimal2);
                        String syMoney = bigDecimal3.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

                        Xtcl xtcl = new Xtcl();
                        xtcl.setId(xtcl1.getId());
                        xtcl.setBz_value(syMoney);
                        xtclMapper.updateCl(xtcl);

                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 股券转报单券
     */
    @Override
    public boolean updateBdScore(Integer uId, String uuid, Float amount, Float cash, Float bdScore) {

        //减股券记录
        ScoreBalance score1 = new ScoreBalance();
        score1.setEnd(2);
        score1.setuUuid(uuid);
        score1.setCategory(9);
        score1.setType(2);
        score1.setScore(amount);
        score1.setJyScore(cash);
        score1.setStatus(1);
        int count1 = scoreBalanceMapper.addCashScore(score1);

        //加报单券记录
        score1.setType(1);
        score1.setJyScore(bdScore);
        int count2 = scoreBalanceMapper.addEntryScore(score1);

        if (count1 == 1 && count2 == 1) {
            UserBean userBean = new UserBean();
            userBean.setId(uId);
            userBean.setCashScore(cash);
            userBean.setBdScore(bdScore);
            int count3 = userMapper.updateScore(userBean);
            if (count3 == 1) {
                return true;
            }
        }
        return false;
    }


    /**
     * 根据推荐码查询用户
     */
    @Override
    public List<UserBean> getUserByBCode(String bCode) {
        return userMapper.getUserByBCode(bCode);
    }


    /**
     * 券转账他人     zzType: 1=股券，2=电子券，3=报单券
     */
    @Override
    public Map<String, Object> updateUserScore(UserBean userBean, UserBean userBean1, Float amount, Integer zzType) {

        Map<String, Object> map = new HashMap<String, Object>();

        //查询返现比列和封顶值
        Xtcl xtcl1 = xtclMapper.getClsValue("hzbl_type", "1");        //券转他人手续费比例
        float float1 = Float.parseFloat(xtcl1.getBz_value());                   //0.03

        if (zzType == 1) {
            if (amount <= userBean.getCashScore()) {

                //减股券记录
                ScoreBalance score = new ScoreBalance();
                score.setEnd(2);
                score.setuUuid(userBean.getUuid());
                score.setCategory(9);
                score.setType(2);
                score.setScore(amount);
                score.setJyScore(userBean.getCashScore() - amount);
                score.setStatus(1);
                score.setZzCode(userBean1.getbCode());
                int count1 = scoreBalanceMapper.addCashScore(score);

                //加股券记录
                float money = amount * (1 - float1);
                score.setuUuid(userBean1.getUuid());
                score.setType(1);
                score.setScore(money);
                score.setJyScore(userBean1.getCashScore() + money);
                score.setZzCode(userBean.getbCode());
                int count2 = scoreBalanceMapper.addCashScore(score);

                //手续费记录
                score.setuUuid(userBean1.getUuid());
                score.setSecoCate(3);
                score.setType(2);
                score.setScore(amount * float1);
                score.setJyScore(0f);
                score.setStatus(1);
                int count3 = scoreBalanceMapper.addScoreDetails(score);

                if (count1 == 1 && count2 == 1 && count3 == 1) {
                    UserBean userBean3 = new UserBean();
                    userBean3.setId(userBean.getId());
                    userBean3.setCashScore(userBean.getCashScore() - amount);
                    int count4 = userMapper.updateScore(userBean3);

                    userBean3.setId(userBean1.getId());
                    userBean3.setCashScore(userBean1.getCashScore() + money);
                    int count5 = userMapper.updateScore(userBean3);

                    if (count4 == 1 && count5 == 1) {
                        map.put("code", "0");
                        map.put("status", "true");
                        map.put("message", "转账成功");
                        map.put("data", "");
                        return map;
                    }
                }
                map.put("code", "-1");
                map.put("status", "false");
                map.put("message", "转账失败");
                map.put("data", "");
                return map;
            }
        } else if (zzType == 2) {
            if (amount <= userBean.getElectScore()) {

                //减电子券记录
                ScoreBalance score = new ScoreBalance();
                score.setEnd(2);
                score.setuUuid(userBean.getUuid());
                score.setCategory(9);
                score.setType(2);
                score.setScore(amount);
                score.setJyScore(userBean.getElectScore() - amount);
                score.setStatus(1);
                score.setZzCode(userBean1.getbCode());
                int count1 = scoreBalanceMapper.addElecScore(score);

                //加电子券记录
                float money = amount * (1 - float1);
                score.setuUuid(userBean1.getUuid());
                score.setType(1);
                score.setScore(money);
                score.setJyScore(userBean1.getElectScore() + money);
                score.setZzCode(userBean.getbCode());
                int count2 = scoreBalanceMapper.addElecScore(score);

                //手续费记录
                score.setuUuid(userBean1.getUuid());
                score.setSecoCate(3);
                score.setType(2);
                score.setScore(amount * float1);
                score.setJyScore(0f);
                score.setStatus(1);
                int count3 = scoreBalanceMapper.addScoreDetails(score);

                if (count1 == 1 && count2 == 1 && count3 == 1) {
                    UserBean userBean3 = new UserBean();
                    userBean3.setId(userBean.getId());
                    userBean3.setElectScore(userBean.getElectScore() - amount);
                    int count4 = userMapper.updateScore(userBean3);

                    userBean3.setId(userBean1.getId());
                    userBean3.setElectScore(userBean1.getElectScore() + money);
                    int count5 = userMapper.updateScore(userBean3);

                    if (count4 == 1 && count5 == 1) {
                        map.put("code", "0");
                        map.put("status", "true");
                        map.put("message", "转账成功");
                        map.put("data", "");
                        return map;
                    }
                }
                map.put("code", "-1");
                map.put("status", "false");
                map.put("message", "转账失败");
                map.put("data", "");
                return map;
            }
        } else if (zzType == 3) {
            if (amount <= userBean.getBdScore()) {

                //减报单券记录
                ScoreBalance score = new ScoreBalance();
                score.setEnd(2);
                score.setuUuid(userBean.getUuid());
                score.setCategory(9);
                score.setType(2);
                score.setScore(amount);
                score.setJyScore(userBean.getBdScore() - amount);
                score.setStatus(1);
                score.setZzCode(userBean1.getbCode());
                int count1 = scoreBalanceMapper.addEntryScore(score);

                //加报单券记录
                float money = amount * (1 - float1);
                score.setuUuid(userBean1.getUuid());
                score.setType(1);
                score.setScore(money);
                score.setJyScore(userBean1.getBdScore() + money);
                score.setZzCode(userBean.getbCode());
                int count2 = scoreBalanceMapper.addEntryScore(score);

                //手续费记录
                score.setuUuid(userBean1.getUuid());
                score.setSecoCate(3);
                score.setType(2);
                score.setScore(amount * float1);
                score.setJyScore(0f);
                score.setStatus(1);
                int count3 = scoreBalanceMapper.addScoreDetails(score);

                if (count1 == 1 && count2 == 1 && count3 == 1) {
                    UserBean userBean3 = new UserBean();
                    userBean3.setId(userBean.getId());
                    userBean3.setBdScore(userBean.getBdScore() - amount);
                    int count4 = userMapper.updateScore(userBean3);

                    userBean3.setId(userBean1.getId());
                    userBean3.setBdScore(userBean1.getBdScore() + money);
                    int count5 = userMapper.updateScore(userBean3);

                    if (count4 == 1 && count5 == 1) {
                        map.put("code", "0");
                        map.put("status", "true");
                        map.put("message", "转账成功");
                        map.put("data", "");
                        return map;
                    }
                }
                map.put("code", "-1");
                map.put("status", "false");
                map.put("message", "转账失败");
                map.put("data", "");
                return map;
            }
        }
        map.put("code", "-3");
        map.put("status", "false");
        map.put("message", "转账金额不能大于可用金额");
        map.put("data", "");
        return map;
    }


    /**
     * 查询转账记录     zzType: 1=股券，2=电子券，3=报单券
     */
    @Override
    public Page<ScoreBalance> getScoreBalance(String uuid, Integer zzType, Integer page, Integer limit) {

        List<ScoreBalance> list = new ArrayList<ScoreBalance>();
        PageHelper.startPage(page, limit);
        if (zzType == 1) {
            list = scoreBalanceMapper.getCashScore(uuid);
        } else if (zzType == 2) {
            list = scoreBalanceMapper.getElecScore(uuid);
        } else if (zzType == 3) {
            list = scoreBalanceMapper.getEntryScore(uuid);
        }
        PageInfo<ScoreBalance> pageInfo = new PageInfo<ScoreBalance>(list);

        return new Page<>(pageInfo);
    }


}
