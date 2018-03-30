package com.jyss.yqy.service;


import com.jyss.yqy.entity.jsonEntity.UserBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface UserAccountService {

    boolean updateBdScore(@Param("uId")Integer uId,@Param("uuid")String uuid,@Param("amount")Float amount,
                          @Param("cash")Float cash,@Param("bdScore")Float bdScore);

    List<UserBean> getUserByBCode(@Param("bCode")String bCode);

    Map<String,Object> updateUserScore(UserBean userBean, UserBean userBean1,
                                       @Param("amount")Float amount, @Param("zzType")Integer zzType);

}
