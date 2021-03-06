package com.jyss.yqy.action;

import com.jyss.yqy.constant.Constant;
import com.jyss.yqy.entity.Page;
import com.jyss.yqy.entity.ScoreBalance;
import com.jyss.yqy.entity.UMobileLogin;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.entity.jsonEntity.UserBean;
import com.jyss.yqy.service.UMobileLoginService;
import com.jyss.yqy.service.UserAccountService;
import com.jyss.yqy.service.UserService;
import com.jyss.yqy.service.XtclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
@RequestMapping("/account")
public class UserAccountAction {
	@Autowired
	private UserService userService;
	@Autowired
	private UMobileLoginService uMobileLoginService;
	@Autowired
    private UserAccountService userAccountService;
	@Autowired
    private XtclService xtclService;


    /**
     * 查询比例
     */
    @RequestMapping("/proportion")
    @ResponseBody
    public Map<String, Object> getXtclBy() {
        Map<String, Object> map = new HashMap<>();
        float ratio = 0.8f;
        Xtcl xtcl = xtclService.getClsValue("dzqbl_type","1");        //电子券占复销额的最高比例
        ratio = Float.parseFloat(xtcl.getBz_value());       			           //0.8

        map.put("code", "0");
        map.put("status", "true");
        map.put("message", "查询成功！");
        map.put("data", ratio);
        return map;

    }


    /**
     * 充值报单券         zfType: 1=支付宝，2=微信
     */
    @RequestMapping("/topup")
    @ResponseBody
    public Map<String, Object> insertBdScore(@RequestParam("token") String token,@RequestParam("bzType") String bzType,
                                             @RequestParam("bzId") String bzId,@RequestParam("zfType") Integer zfType) {
        Map<String, Object> map = new HashMap<>();
        List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1) {
            UMobileLogin uMobileLogin = loginList.get(0);
            if(zfType == 1){
                Map<String, Object> result = userAccountService.getALiPayResult(uMobileLogin.getuUuid(), bzType, bzId);
                return result;

            }else if(zfType == 2){
                Map<String, Object> result = userAccountService.geWxPayResult(uMobileLogin.getuUuid(), bzType, bzId);
                return result;
            }

        }
        map.put("code", "-2");
        map.put("status", "false");
        map.put("message", "请重新登陆");
        map.put("data", "");
        return map;

    }



    /**
     * 股券转报单券
     */
    @RequestMapping("/change")
    @ResponseBody
    public Map<String, Object> updateBdScore(@RequestParam("token") String token,
                                             @RequestParam("amount") Float amount,
                                             @RequestParam("password") String password) {
        Map<String, Object> map = new HashMap<>();
        List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1) {
            UMobileLogin uMobileLogin = loginList.get(0);
            List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
            if(list != null && list.size() == 1){
                UserBean userBean = list.get(0);
                String psw = DigestUtils.md5DigestAsHex(password.getBytes());
                if(userBean.getPayPwd().equals(psw)){
                    if(amount <= userBean.getCashScore()){
                        float cash = userBean.getCashScore() - amount;
                        float bdScore = userBean.getBdScore() + amount;
                        boolean flag = userAccountService.updateBdScore(userBean.getId(),
                                userBean.getUuid(), amount, cash, bdScore);
                        if(flag){
                            map.put("code", "0");
                            map.put("status", "true");
                            map.put("message", "操作成功");
                            map.put("data", "");
                            return map;
                        }
                        map.put("code", "-1");
                        map.put("status", "false");
                        map.put("message", "操作失败");
                        map.put("data", "");
                        return map;
                    }
                    map.put("code", "-3");
                    map.put("status", "false");
                    map.put("message", "转账金额不能大于可用金额");
                    map.put("data", "");
                    return map;
                }
                map.put("code", "-4");
                map.put("status", "false");
                map.put("message", "支付密码错误");
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
     * 根据推荐码查询用户
     */
    @RequestMapping("/userInfo")
    @ResponseBody
    public Map<String,Object> selectUserBy(@RequestParam("bCode")String bCode){
        Map<String, Object> map = new HashMap<>();
        List<UserBean> list = userAccountService.getUserByBCode(bCode);
        if(list != null && list.size() == 1){
            UserBean userBean = list.get(0);
            if(!userBean.getAvatar().equals("")){
                userBean.setAvatar(Constant.httpUrl + userBean.getAvatar());
            }
            map.put("code", "0");
            map.put("status", "true");
            map.put("message", "查询成功");
            map.put("data", userBean);
            return map;
        }
        map.put("code", "-1");
        map.put("status", "false");
        map.put("message", "查询失败");
        map.put("data", "");
        return map;
    }



    /**
     * 券转账他人   zzType: 1=股券，2=电子券，3=报单券
     */
    @RequestMapping("/giving")
    @ResponseBody
    public Map<String,Object> updateUserScore(@RequestParam("token") String token,@RequestParam("amount") Float amount,
                                              @RequestParam("password") String password,@RequestParam("uuid")String uuid,
                                              @RequestParam("zzType")Integer zzType){

        Map<String, Object> map = new HashMap<>();
        List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1) {
            UMobileLogin uMobileLogin = loginList.get(0);
            //查询自己
            List<UserBean> list = userService.getUserByUuid(uMobileLogin.getuUuid());
            if(uMobileLogin.getuUuid().equals(uuid)){
                map.put("code", "-7");
                map.put("status", "false");
                map.put("message", "不能自己转自己");
                map.put("data", "");
                return map;
            }
            if(list != null && list.size() == 1){
                UserBean userBean = list.get(0);
                String psw = DigestUtils.md5DigestAsHex(password.getBytes());
                //查询他人
                List<UserBean> list1 = userService.getUserByUuid(uuid);
                if(list1 != null && list1.size() == 1){
                    UserBean userBean1 = list1.get(0);
                    if(userBean1.getIsTransfer() == 1 && userBean.getIsTransfer() == 1 && userBean.getBorrow() == 0){

                        if(userBean.getPayPwd().equals(psw)){
                            Map<String, Object> map1 = userAccountService.updateUserScore(userBean,
                                    userBean1, amount, zzType);
                            return map1;

                        }
                        map.put("code", "-4");
                        map.put("status", "false");
                        map.put("message", "支付密码错误");
                        map.put("data", "");
                        return map;
                    }
                    map.put("code", "-6");
                    map.put("status", "false");
                    map.put("message", "您不可转账");
                    map.put("data", "");
                    return map;
                }
                map.put("code", "-5");
                map.put("status", "false");
                map.put("message", "转账账户不存在");
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
     * 查询转账记录     zzType: 1=股券，2=电子券，3=报单券
     */
    @RequestMapping("/transInfo")
    @ResponseBody
    public Map<String,Object> getScoreBalance(@RequestParam("token") String token,@RequestParam("zzType")Integer zzType,
                                              @RequestParam(value = "page", required = true) Integer page,
                                              @RequestParam(value = "limit", required = true) Integer limit){
        Map<String, Object> map = new HashMap<>();

        List<UMobileLogin> loginList = uMobileLoginService.findUserByToken(token);
        if (loginList != null && loginList.size() == 1) {
            UMobileLogin uMobileLogin = loginList.get(0);

            Page<ScoreBalance> result = userAccountService.getScoreBalance(uMobileLogin.getuUuid(), zzType, page, limit);

            map.put("code", "0");
            map.put("status", "true");
            map.put("message", "查询成功");
            map.put("data", result);
            return map;

        }
        map.put("code", "-2");
        map.put("status", "false");
        map.put("message", "请重新登陆");
        map.put("data", "");
        return map;

    }




}
