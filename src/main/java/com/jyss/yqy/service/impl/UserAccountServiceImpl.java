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
import com.jyss.yqy.constant.Constant;
import com.jyss.yqy.entity.Page;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.UserAccountService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

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
    public Map<String, Object> getALiPayResult(String uuid, Float payAmount, Float amount){
        Map<String, Object> map = new HashMap<String, Object>();

        //查询常量值
        Xtcl xtcl1 = xtclMapper.getClsValue("fxje_type", "1");        //最低复销金额
        float float1 = Float.parseFloat(xtcl1.getBz_value());       			   //6000
        Xtcl xtcl2 = xtclMapper.getClsValue("dzqbl_type", "1");       //电子券占复销额的最高比例
        float float2 = Float.parseFloat(xtcl2.getBz_value());                      //0.8

        float totalMoney = payAmount + amount;
        if(totalMoney * float2 < amount){
            map.put("code", "-3");
            map.put("status", "false");
            map.put("message", "电子券不能超过使用比例");
            map.put("data", "");
            return map;
        }
        if(totalMoney < float1){
            map.put("code", "-4");
            map.put("status", "false");
            map.put("message", "充值金额不能低于最低复销值");
            map.put("data", "");
            return map;
        }
        List<UserBean> userBeans = userMapper.getUserByUuid(uuid);
        if(userBeans != null && userBeans.size() == 1){
            UserBean userBean = userBeans.get(0);
            if(userBean.getElectScore() < amount){
                map.put("code", "-5");
                map.put("status", "false");
                map.put("message", "电子券余额不足");
                map.put("data", "");
                return map;
            }

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
            AlipayClient alipayClient = new DefaultAlipayClient(config.getURL(), config.getAPP_ID() , config.getAPP_PRIVATE_KEY(),
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
                scoreBalance.setScore(payAmount);
                scoreBalance.setDzScore(amount);
                scoreBalance.setJyScore(userBean.getBdScore() + totalMoney);
                scoreBalance.setOrderSn(outTradeNo);
                scoreBalance.setStatus(0);
                int count = scoreBalanceMapper.insertEntryScore(scoreBalance);
                if(count == 1){
                    map.put("code", "0");
                    map.put("status", "true");
                    map.put("message", "支付成功");
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


    /**
     * 充值结果异步处理
     */
    @Override
    public boolean updateUserBdBalance(String totalAmount, String outTradeNo) {
        List<ScoreBalance> balanceList = scoreBalanceMapper.selectEntryScore(outTradeNo);
        if(balanceList != null && balanceList.size() == 1){
            ScoreBalance scoreBalance = balanceList.get(0);
            float money = Float.parseFloat(totalAmount);
            if(money == scoreBalance.getScore()){
                List<UserBean> userBeans = userMapper.getUserByUuid(scoreBalance.getuUuid());
                if(userBeans != null && userBeans.size() == 1){
                    UserBean userBean = userBeans.get(0);

                    UserBean userBean1 = new UserBean();
                    userBean1.setId(userBean.getId());
                    userBean1.setElectScore(userBean.getElectScore() - scoreBalance.getDzScore());
                    userBean1.setBdScore(userBean.getBdScore() + scoreBalance.getScore() + scoreBalance.getDzScore());
                    userMapper.updateScore(userBean1);

                    //记录电子券使用
                    if(scoreBalance.getDzScore() > 0){
                        ScoreBalance scoreBalance1 = new ScoreBalance();
                        scoreBalance1.setEnd(2);
                        scoreBalance1.setuUuid(userBean.getUuid());
                        scoreBalance1.setCategory(11);
                        scoreBalance1.setType(2);
                        scoreBalance1.setScore(scoreBalance.getDzScore());
                        scoreBalance1.setJyScore(userBean.getElectScore() - scoreBalance.getDzScore());
                        scoreBalance1.setStatus(1);
                        scoreBalanceMapper.addElecScore(scoreBalance1);
                    }

                    ScoreBalance scoreBalance1 = new ScoreBalance();
                    scoreBalance1.setId(scoreBalance.getId());
                    scoreBalance1.setStatus(1);
                    int count = scoreBalanceMapper.updateEntryScore(scoreBalance1);
                    if(count == 1){
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

        if(count1 == 1 && count2 == 1){
            UserBean userBean = new UserBean();
            userBean.setId(uId);
            userBean.setCashScore(cash);
            userBean.setBdScore(bdScore);
            int count3 = userMapper.updateScore(userBean);
            if(count3 == 1){
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
    public Map<String,Object> updateUserScore(UserBean userBean, UserBean userBean1, Float amount, Integer zzType) {

        Map<String, Object> map = new HashMap<String, Object>();

        //查询返现比列和封顶值
        Xtcl xtcl1 = xtclMapper.getClsValue("hzbl_type", "1");        //券转他人手续费比例
        float float1 = Float.parseFloat(xtcl1.getBz_value());       			   //0.03

        if(zzType == 1){
            if(amount <= userBean.getCashScore()){

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

                if(count1 == 1 && count2 == 1 && count3 == 1){
                    UserBean userBean3 = new UserBean();
                    userBean3.setId(userBean.getId());
                    userBean3.setCashScore(userBean.getCashScore() - amount);
                    int count4 = userMapper.updateScore(userBean3);

                    userBean3.setId(userBean1.getId());
                    userBean3.setCashScore(userBean1.getCashScore() + money);
                    int count5 = userMapper.updateScore(userBean3);

                    if(count4 == 1 && count5 == 1){
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
        }else if(zzType == 2){
            if(amount <= userBean.getElectScore()){

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

                if(count1 == 1 && count2 == 1 && count3 == 1){
                    UserBean userBean3 = new UserBean();
                    userBean3.setId(userBean.getId());
                    userBean3.setElectScore(userBean.getElectScore() - amount);
                    int count4 = userMapper.updateScore(userBean3);

                    userBean3.setId(userBean1.getId());
                    userBean3.setElectScore(userBean1.getElectScore() + money);
                    int count5 = userMapper.updateScore(userBean3);

                    if(count4 == 1 && count5 == 1){
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
        }else if(zzType == 3){
            if(amount <= userBean.getBdScore()){

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

                if(count1 == 1 && count2 == 1 && count3 == 1){
                    UserBean userBean3 = new UserBean();
                    userBean3.setId(userBean.getId());
                    userBean3.setBdScore(userBean.getBdScore() - amount);
                    int count4 = userMapper.updateScore(userBean3);

                    userBean3.setId(userBean1.getId());
                    userBean3.setBdScore(userBean1.getBdScore() + money);
                    int count5 = userMapper.updateScore(userBean3);

                    if(count4 == 1 && count5 == 1){
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
        if(zzType == 1){
            list = scoreBalanceMapper.getCashScore(uuid);
        }else if(zzType == 2){
            list = scoreBalanceMapper.getElecScore(uuid);
        }else if(zzType == 3){
            list = scoreBalanceMapper.getEntryScore(uuid);
        }
        PageInfo<ScoreBalance> pageInfo = new PageInfo<ScoreBalance>(list);

        return new Page<>(pageInfo);
    }


}
