package com.jyss.yqy.action;

import com.jyss.yqy.service.JBonusFhjService;
import com.jyss.yqy.service.JRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@Controller
public class JobAction {

    private static final Logger logger = LoggerFactory.getLogger(JobAction.class);

    @Autowired
    private JRecordService recordService;
    @Autowired
    private JBonusFhjService jBonusFhjService;


    /**
     * 计算层奖
     */



    /**
     * 计算量奖
     */
    @RequestMapping("/scj/computeSCJ")
    public void insertJBonusScj() {
        Map<String, String> map = recordService.insertJBonusScj();
        logger.info(map.get("message"));
    }



    /**
     * 计算共享奖
     */



    /**
     * 计算分红奖
     */
    @RequestMapping("/fhj/computeFHJ")
    public void insertJBonusFHj() {
        Map<String, String> map = jBonusFhjService.insertJBonusFhj();
        logger.info(map.get("message"));
    }

}
