package com.rest.autotest.asserts;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.data.DataBuilder;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

import static com.rest.autotest.reports.TestAttachment.requestAndRespondBody;
import static com.rest.autotest.reports.TestAttachment.assertRespond;

import java.util.Map;

public class OptResponseAsserts {
    static Logger log = Logger.getLogger(OptResponseAsserts.class);

    public static void optResponseAsserts(Response response , int id,String url,JSONObject arg){
        Map<String, JSONObject> data= DataBuilder.optdatabuilder();
        String assertResult="";
        JSONObject expres=new JSONObject();
        for(Map.Entry<String,JSONObject> entry : data.entrySet()) {
            JSONObject bodyJson = entry.getValue();
            if (bodyJson.getInteger("id") == id) {
                expres = bodyJson.getJSONObject("res");
            }
        }
        requestAndRespondBody(url,JSONObject.toJSONString(arg),response.asString());

        JSONObject reponseJson = JSONObject.parseObject(response.asString());
        boolean successbool=(reponseJson.getBoolean("success")==expres.getBoolean("success"));
        boolean errorCodebool=(reponseJson.getInteger("errorCode").equals(expres.getInteger("errorCode")));
        if(response.asString()==null){
            assertResult="操作失败->响应为空";
//            log.info("操作失败->响应为空");
        }
        if(successbool&errorCodebool){
            assertResult="操作成功->响应与预期一致";
//            log.info("操作成功->响应与预期一致"+":"+reponseJson+expres);
        }
        else{
            assertResult="操作失败->响应与预期不一致";
//            log.info("操作失败->响应与预期不一致"+":"+reponseJson+expres);
        }
        assertRespond(assertResult);
    }
}
