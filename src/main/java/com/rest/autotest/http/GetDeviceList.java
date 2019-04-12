package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.rest.autotest.model.Device;
import com.rest.autotest.utils.FileUtil;
import com.rest.autotest.utils.JsonUtil;
import com.rest.autotest.common.envSet;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import com.rest.autotest.utils.TemplateUtil;


import java.util.*;

import static io.restassured.RestAssured.given;

public class GetDeviceList extends TemplateUtil{
    private Logger log = Logger.getLogger(GetDeviceList.class);
    private static final String TEMPLATE = FileUtil.loadResourceFile("模板.json");
    private static final JSONObject TEMPLATE_JO = JSONObject.parseObject(TEMPLATE);

    private static void geturl(String ccuid) {
        String url="";
        String baseurl=envSet.setbaseurl();
        url=baseurl+"/ccu/"+ccuid+"/deviceList";
        RestAssured.baseURI=url;
    }

    private static String getccuid(){
        String cculist=GetCcuList.getCcuList();
        return GetCcuList.GetCcuId(cculist);
    }

    private static Map<String,String> getccuidmap(){
        Map<String,String> ccuidmap=new HashMap<>();
        String cculist=GetCcuList.getCcuList();
        ccuidmap=GetCcuList.GetCcuIdList(cculist);
        return ccuidmap;
    }

    public static String getdevicelist(){
        /**
         * @description: 获取某台主机下的设备列表
         * @param: []
         * @return: java.lang.String
         */
        Response response = null;
        geturl(getccuid());
        response = given()
                .relaxedHTTPSValidation()
                .contentType("application/json;charset=UTF-8")
                .headers(envSet.setheaders())
                .get();
        return response.asString();
    }

    public static List<Device> suportdev(String res){
        /**
         * @description: 根据json模板解析可操作设备
         * @param: [res]
         * @return: java.util.List<com.rest.autotest.model.Device>
         */
        List<Device> resdev=JsonUtil.fromJson(res, new TypeToken<List<Device>>() {
        }.getType());
        List<Device> suportdev=resdev;
        Iterator<Device> device = suportdev.iterator();
        while(device.hasNext()){
            Device dev = device.next();
            String type = dev.getType();
            if(!(TEMPLATE_JO.keySet().contains(type))){
                device.remove();
            }
        }
        return suportdev;
    }

    public static Map<String,String> getdevicelistfromccus(){
        /**
         * @description: 获取当前开发者账号下所有主机设备列表
         * @param: []
         * @return: java.util.LinkedHashMap<java.lang.String,java.lang.String>
         */
        Response response = null;
        Map<String,String> ccuidmap=getccuidmap();
        Map<String,String> resmap=new HashMap<>();
        Iterator<Map.Entry<String,String>> iterator= ccuidmap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,String> entry = iterator.next();
            geturl(entry.getValue());
            response = given()
                    .relaxedHTTPSValidation()
                    .contentType("application/json;charset=UTF-8")
                    .headers(envSet.setheaders())
                    .get();
            String res=response.asString();
            resmap.put(entry.getValue(),res);
        }


        //System.out.println(response.asString());
        return resmap;
    }

    public static Map<String,List<Device>> suportdevfromccus(Map<String,String> resmap){
        /**
         * @description: 根据json模板解析可操作设备
         * @param: [resmap]
         * @return: java.util.LinkedHashMap<java.lang.String,java.util.List<com.rest.autotest.model.Device>>
         */
        Map<String,List<Device>> suportdevmap=new HashMap<>();
        Iterator<Map.Entry<String,String>> iterator= resmap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String ccuid = entry.getKey();
            String res = entry.getValue();
            List<Device> resdev = JsonUtil.fromJson(res, new TypeToken<List<Device>>() {
            }.getType());
            List<Device> suportdev = resdev;
            Iterator<Device> device = suportdev.iterator();
            while (device.hasNext()) {
                Device dev = device.next();
                String type = dev.getType();
                if (!(TEMPLATE_JO.keySet().contains(type))) {
                    device.remove();
                }
            }
            suportdevmap.put(ccuid,suportdev);
        }
        return suportdevmap;
    }
}
