package com.rest.autotest.http;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.common.PropertieyConfig;
import com.rest.autotest.common.envSet;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.LinkedHashMap;
import static io.restassured.RestAssured.given;


public class GetCcuList {

    private static Logger log = Logger.getLogger(GetCcuList.class);
    private static String baseurl=envSet.setbaseurl();

    private static void seturl(){
        String url="";
        url=baseurl+"/cculist";
        RestAssured.baseURI=url;
    }

    private static JSONObject getdata(){
        String datastr="{\"pageNo\":0,\"pageSize\":10}";
        return JSONObject.parseObject(datastr);
    }

    private static String getccuname(){
        PropertieyConfig property=new PropertieyConfig();
        String ccuname=null;
        try {
            ccuname = property.getValue("ccuName", "request.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ccuname;
    }

    public  static String getCcuList(){
        /**
         * @description: 获取当前开发者账号的主机列表
         * @param: []
         * @return: java.lang.String
         */
        Response response = null;
        seturl();
        response = given()
                .relaxedHTTPSValidation()
                .contentType("application/json;charset=UTF-8")
                .headers(envSet.setheaders())
                .body(getdata())
                .post();
        return response.asString();
    }

    public static String GetCcuId(){
        /**
         * @description: 获取某台主机的ccuId
         * @param: [res]
         * @return: java.lang.String
         */
        String ccuid="";
        String ccuname=getccuname();
        String res=getCcuList();
        JSONObject Jres=JSONObject.parseObject(res);
        JSONArray Ares=Jres.getJSONArray("data");
        for(int i=0;i<Ares.size();i++){
            JSONObject Jdata=Ares.getJSONObject(i);
            String deviceid=Jdata.getString("deviceId");
            if(deviceid.equals(ccuname)){
                ccuid=Jdata.getString("id");
            }
        }
        return ccuid;
    }

    public static LinkedHashMap<String,String> GetCcuIdList(){
        /**
         * @description: 获取当前开发者账号下所有主机ccuId列表
         * @param: [res]
         * @return: java.util.LinkedHashMap<java.lang.String,java.lang.String>
         */
        LinkedHashMap<String,String> ccuidmap=new LinkedHashMap<>();
        String res=getCcuList();
        JSONObject Jres=JSONObject.parseObject(res);
        JSONArray Ares=Jres.getJSONArray("data");
        for(int i=0;i<Ares.size();i++){
            JSONObject Jdata=Ares.getJSONObject(i);
            String ccuid=Jdata.getString("id");
            String ccuname=Jdata.getString("deviceId");
            ccuidmap.put(ccuname,ccuid);
            }
        return ccuidmap;
    }


}


