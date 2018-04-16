package com.jyss.yqy.service.impl;

import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.mapper.ScoreBalanceMapper;
import com.jyss.yqy.mapper.UserMapper;
import com.jyss.yqy.mapper.XtclMapper;
import com.jyss.yqy.service.JBonusFPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class JBonusFPServiceImpl implements JBonusFPService {

    @Autowired
    private XtclMapper xtclMapper;
    @Autowired
    private ScoreBalanceMapper scoreBalanceMapper;
    @Autowired
    private UserMapper userMapper;


    /**
     * 奖金分配接口
     */
    @Override
    public boolean insertScoreBalance(int id,float money,int category){

        Xtcl xtcl1 = xtclMapper.getClsValue("dtjjbl_type", "1");      //奖金税费比例
        float float1 = Float.parseFloat(xtcl1.getBz_value());        			   //0.05
        Xtcl xtcl2 = xtclMapper.getClsValue("dtjjbl_type", "2");      //奖金平台管理费比例
        float float2 = Float.parseFloat(xtcl2.getBz_value());                      //0.05
        Xtcl xtcl3 = xtclMapper.getClsValue("dtjjbl_type", "3");      //奖金电子券比例
        float float3 = Float.parseFloat(xtcl3.getBz_value());                      //0.2
        Xtcl xtcl4 = xtclMapper.getClsValue("dtjjbl_type", "4");      //奖金商城消费券比例
        float float4 = Float.parseFloat(xtcl4.getBz_value());                      //0.2
        Xtcl xtcl5 = xtclMapper.getClsValue("dtjjbl_type", "5");      //奖金股券比例
        float float5 = Float.parseFloat(xtcl5.getBz_value());                      //0.5

        List<UserBean> userList = userMapper.getUserScoreById(id);
        if(userList != null && userList.size()>0){
            UserBean userBean = userList.get(0);
            Float totalPv = userBean.getTotalPv();
            if(category == 7){     //共享奖不扣分红权,不用考虑totalPv >0

                //添加股券
                ScoreBalance score1 = new ScoreBalance();
                score1.setEnd(2);
                score1.setuUuid(userBean.getUuid());
                score1.setCategory(category);
                score1.setType(1);
                score1.setScore(money * float5);
                score1.setJyScore(money * float5 + userBean.getCashScore());
                score1.setStatus(1);
                int count1 = scoreBalanceMapper.addCashScore(score1);

                //添加消费券
                score1.setScore(money * float4);
                score1.setJyScore(money * float4 + userBean.getShoppingScore());
                int count2 = scoreBalanceMapper.addShoppingScore(score1);

                //添加电子券
                score1.setScore(money * float3);
                score1.setJyScore(money * float3 + userBean.getElectScore());
                int count3 = scoreBalanceMapper.addElecScore(score1);

                //添加税
                score1.setType(2);     //支出税
                score1.setScore(money * float1);
                score1.setJyScore(0.0f);
                score1.setSecoCate(1);
                int count4 = scoreBalanceMapper.addScoreDetails(score1);

                //添加平台管理费
                score1.setType(2);      //支出平台管理费
                score1.setScore(money * float2);
                score1.setJyScore(0.0f);
                score1.setSecoCate(2);
                int count5 = scoreBalanceMapper.addScoreDetails(score1);

                if(count1 == 1 && count2 == 1 && count3 == 1 && count4 == 1 && count5 == 1){
                    UserBean userBean2 = new UserBean();
                    userBean2.setId(id);
                    userBean2.setCashScore(money * float5 + userBean.getCashScore());
                    userBean2.setShoppingScore(money * float4 + userBean.getShoppingScore());
                    userBean2.setElectScore(money * float3 + userBean.getElectScore());
                    int count6 = userMapper.updateScore(userBean2);
                    if(count6 == 1){
                        return true;
                    }
                }
            }
            if(totalPv > 0){
                float money1 = money <= totalPv ? money : totalPv;     //判断是否大于totalPv

                //添加股券
                ScoreBalance score1 = new ScoreBalance();
                score1.setEnd(2);
                score1.setuUuid(userBean.getUuid());
                score1.setCategory(category);
                score1.setType(1);
                score1.setScore(money1 * float5);
                score1.setJyScore(money1 * float5 + userBean.getCashScore());
                score1.setStatus(1);
                int count1 = scoreBalanceMapper.addCashScore(score1);

                //添加消费券
                score1.setScore(money1 * float4);
                score1.setJyScore(money1 * float4 + userBean.getShoppingScore());
                int count2 = scoreBalanceMapper.addShoppingScore(score1);

                //添加电子券
                score1.setScore(money1 * float3);
                score1.setJyScore(money1 * float3 + userBean.getElectScore());
                int count3 = scoreBalanceMapper.addElecScore(score1);

                //添加税
                score1.setType(2);     //支出税
                score1.setScore(money1 * float1);
                score1.setJyScore(0.0f);
                score1.setSecoCate(1);
                int count4 = scoreBalanceMapper.addScoreDetails(score1);

                //添加平台管理费
                score1.setType(2);      //支出平台管理费
                score1.setScore(money1 * float2);
                score1.setJyScore(0.0f);
                score1.setSecoCate(2);
                int count5 = scoreBalanceMapper.addScoreDetails(score1);

                if(count1 == 1 && count2 == 1 && count3 == 1 && count4 == 1 && count5 == 1){
                    UserBean userBean2 = new UserBean();
                    userBean2.setId(id);
                    userBean2.setCashScore(money1 * float5 + userBean.getCashScore());
                    userBean2.setShoppingScore(money1 * float4 + userBean.getShoppingScore());
                    userBean2.setElectScore(money1 * float3 + userBean.getElectScore());
                    userBean2.setTotalPv(totalPv - money1);
                    int count6 = userMapper.updateScore(userBean2);
                    if(count6 == 1){
                        return true;
                    }
                }
            }
        }
        return false;
    }




}
