package com.jyss.yqy.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class JobAction {

    private static final Logger logger = LoggerFactory.getLogger(JobAction.class);



    /**
     * 计算层奖
     */
    @RequestMapping("/scj/computeSCJ")
    @ResponseBody
    public void insertJBonusScj() {
        //Map<String, String> map = recordService.insertJBonusScj();
        //logger.info(map.get("message"));
    }


    /**
     * 计算量奖
     */



    /**
     * 计算共享奖
     */



    /**
     * 计算分红奖
     */

}
