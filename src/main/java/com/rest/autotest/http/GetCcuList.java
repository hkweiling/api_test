package com.rest.autotest.http;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.common.PropertieyConfig;
import com.rest.autotest.common.envSet;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;


public class GetCcuList {

    private Logger log = Logger.getLogger(GetCcuList.class);
    private static envSet envset=new envSet();


    private static String geturl(){
        String baseurl=envset.setbaseurl();
        String url=baseurl+"/cculist";
        return url;
    }

    private static List<Header> getheader() {
        return envset.setheader();
    }

    private static JSONObject getdata(){
        String datastr="{\"pageNo\":0,\"pageSize\":10}";
        JSONObject data=JSONObject.parseObject(datastr);
        return data;
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

    public  static String GetCcuList(){
        /**
         * @description: 获取当前开发者账号的主机列表
         * @param: []
         * @return: java.lang.String
         */
        Response response = null;
        String url=geturl();
        List<Header> helist=getheader();
        JSONObject arg=getdata();
        response = given()
                .relaxedHTTPSValidation()
                .contentType("application/json;charset=UTF-8")
                .header(helist.get(0))
                .header(helist.get(1))
                .body(arg)
                .post(url);
        return response.asString();
    }

    public static String GetCcuId(String res){
        /**
         * @description: 获取某台主机的ccuId
         * @param: [res]
         * @return: java.lang.String
         */
        String ccuid=null;
        String ccuname=getccuname();
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

    public static LinkedHashMap<String,String> GetCcuIdList(String res){
        /**
         * @description: 获取当前开发者账号下所有主机ccuId列表
         * @param: [res]
         * @return: java.util.LinkedHashMap<java.lang.String,java.lang.String>
         */
        LinkedHashMap<String,String> ccuidmap=new LinkedHashMap<>();
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


