package com.rest.autotest.asserts;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.data.DataBuilder;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

import static com.rest.autotest.reports.TestStep.assertRespond;
import static com.rest.autotest.reports.TestStep.requestAndRespondBody;

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

        //log.info(response.asString());
        JSONObject reponseJson = JSONObject.parseObject(response.asString());
        boolean idequalbool=(reponseJson.getInteger("id")==id);
        boolean typeequalbool=((reponseJson.getString("type")).equals(type));
        boolean statusnotnull=(reponseJson.getJSONObject("status")!=null);
        if(response.asString()==null){
            assertResult="操作失败->响应为空";
            log.info("操作失败->响应为空");
        }
        if(idequalbool&typeequalbool&statusnotnull){
            assertResult="操作成功->响应与预期一致"+":"+reponseJson;
            log.info("操作成功->响应与预期一致"+":"+reponseJson);
        }
        else{
            assertResult="操作失败->响应与预期不一致"+":"+reponseJson;
            log.info("操作失败->响应与预期不一致"+":"+reponseJson);
        }
        assertRespond(assertResult);
    }
}
