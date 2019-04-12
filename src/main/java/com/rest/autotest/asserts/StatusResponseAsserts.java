package com.rest.autotest.asserts;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.data.DataBuilder;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

import static com.rest.autotest.reports.TestAttachment.assertRespond;
import static com.rest.autotest.reports.TestAttachment.requestAndRespondBody;

/**
 * @description:
 * @author:
 * @time: 2019/3/30 10:04
 */
public class StatusResponseAsserts {
    static Logger log = Logger.getLogger(OptResponseAsserts.class);

    private static JSONObject getarg(Integer id,String type){
        String argstr="{\"id\": "+id+",\"type\":\""+type+"\"}";
        return JSONObject.parseObject(argstr);
    }

    public static void statusResponseAsserts(Response response , int id, String url){
        List<Map<Integer,String>> data= DataBuilder.statusdatabuilder();
        String assertResult="";
        String type="";
        for (Map<Integer,String> datao:data) {
            for(Map.Entry<Integer,String> entry:datao.entrySet()){
                if(entry.getKey()==id){
                    type=entry.getValue();
                }
            }
        }
        JSONObject arg=getarg(id,type);
        requestAndRespondBody(url,JSONObject.toJSONString(arg),response.asString());

        JSONObject reponseJson = JSONObject.parseObject(response.asString());
        if(response.asString()==null){
            Assert.assertNull(response.asString());
        }
        if(reponseJson.containsKey("error_code")) {
            Assert.assertFalse(false);
            assertResult="操作失败"+":"+reponseJson;
        }
        else if(reponseJson.containsKey("id")){
            Assert.assertTrue(true);
            assertResult="操作成功"+":"+reponseJson;
        }
        else{
            assertResult=response.asString();
        }
        assertRespond(assertResult);
    }
}
