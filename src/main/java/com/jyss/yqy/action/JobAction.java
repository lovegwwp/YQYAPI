package com.jyss.yqy.action;

import com.jyss.yqy.service.JBonusCjService;
import com.jyss.yqy.service.JBonusFhjService;
import com.jyss.yqy.service.JBonusGxjService;
import com.jyss.yqy.service.JRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
public class JobAction {

    private static final Logger logger = LoggerFactory.getLogger(JobAction.class);

    @Autowired
    private JRecordService recordService;
    @Autowired
    private JBonusFhjService jBonusFhjService;
    @Autowired
    private JBonusCjService jBonusCjService;
    @Autowired
    private JBonusGxjService jBonusGxjService;



    /**
     * 计算层奖
     */
    @RequestMapping("/cj/computeCJ")
    @ResponseBody
    public void insertJBonusCj() {
        Map<String, String> map = jBonusCjService.insertJBonusCj();
        logger.info(map.get("message"));
    }


    /**
     * 计算量奖（市场奖）
     */
    @RequestMapping("/scj/computeSCJ")
    @ResponseBody
    public void insertJBonusScj() {
        Map<String, String> map = recordService.insertJBonusScj();
        logger.info(map.get("message"));
    }


    /**
     * 计算共享奖
     */
    @RequestMapping("/gxj/computeGXJ")
    @ResponseBody
    public void insertJBonusGxj() {
        Map<String, String> map = jBonusGxjService.insertJBonusGxj();
        logger.info(map.get("message"));
    }


    /**
     * 计算分红奖
     */
    @RequestMapping("/fhj/computeFHJ")
    @ResponseBody
    public void insertJBonusFhj() {
        Map<String, String> map = jBonusFhjService.insertJBonusFhj();
        logger.info(map.get("message"));
    }


    /**
     * 扣除借贷金额
     */
    @RequestMapping("/deduct/computeBorrow")
    @ResponseBody
    public void updateUserBorrow() {
        Map<String, String> map = jBonusFhjService.updateUserBorrow();
        logger.info(map.get("message"));
    }




}
