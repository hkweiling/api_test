package com.rest.autotest.asserts;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.reports.TestAttachment;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.Assert;


/**
 * @description:
 * @author:
 * @time: 2019/3/30 10:04
 */
public class StatusResponseAsserts {
    static Logger log = Logger.getLogger(OptResponseAsserts.class);


    public static void statusResponseAsserts(Response response , JSONObject body, String url){
        String assertResult="";
        TestAttachment.requestAndRespondBody(url,body,response.asString());

        JSONObject reponseJson = JSONObject.parseObject(response.asString());
        if(response.asString()==null){
            Assert.assertNull(response.asString());
        }
        if(reponseJson.containsKey("error_code")) {
            Assert.assertFalse(false);
            assertResult="操作失败"+":"+reponseJson;
        }
        else if(reponseJson.containsKey("id")&reponseJson.getInteger("id").equals(body.getInteger("id"))){
            Assert.assertTrue(true);
            assertResult="操作成功"+":"+reponseJson;
        }
        else{
            assertResult=response.asString();
        }
//        log.info(assertResult);
        TestAttachment.assertRespond(assertResult);
    }
}
