package com.jyss.yqy.service.impl;

import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
            if(amount > userBean.getCashScore()){

                //减股券记录
                ScoreBalance score = new ScoreBalance();
                score.setEnd(2);
                score.setuUuid(userBean.getUuid());
                score.setCategory(9);
                score.setType(2);
                score.setScore(amount);
                score.setJyScore(userBean.getCashScore() - amount);
                score.setStatus(1);
                int count1 = scoreBalanceMapper.addCashScore(score);

                //加股券记录
                float money = amount * (1 - float1);
                score.setuUuid(userBean1.getUuid());
                score.setType(1);
                score.setScore(money);
                score.setJyScore(userBean1.getCashScore() + money);
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
            if(amount > userBean.getElectScore()){

                //减电子券记录
                ScoreBalance score = new ScoreBalance();
                score.setEnd(2);
                score.setuUuid(userBean.getUuid());
                score.setCategory(9);
                score.setType(2);
                score.setScore(amount);
                score.setJyScore(userBean.getElectScore() - amount);
                score.setStatus(1);
                int count1 = scoreBalanceMapper.addElecScore(score);

                //加电子券记录
                float money = amount * (1 - float1);
                score.setuUuid(userBean1.getUuid());
                score.setType(1);
                score.setScore(money);
                score.setJyScore(userBean1.getElectScore() + money);
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
            if(amount > userBean.getBdScore()){

                //减报单券记录
                ScoreBalance score = new ScoreBalance();
                score.setEnd(2);
                score.setuUuid(userBean.getUuid());
                score.setCategory(9);
                score.setType(2);
                score.setScore(amount);
                score.setJyScore(userBean.getBdScore() - amount);
                score.setStatus(1);
                int count1 = scoreBalanceMapper.addEntryScore(score);

                //加报单券记录
                float money = amount * (1 - float1);
                score.setuUuid(userBean1.getUuid());
                score.setType(1);
                score.setScore(money);
                score.setJyScore(userBean1.getBdScore() + money);
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


}
