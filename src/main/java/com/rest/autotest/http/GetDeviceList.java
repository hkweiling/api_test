package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.rest.autotest.model.Device;
import com.rest.autotest.utils.FileUtil;
import com.rest.autotest.utils.JsonUtil;
import com.rest.autotest.common.envSet;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import com.rest.autotest.utils.TemplateUtil;


import java.util.*;

import static io.restassured.RestAssured.given;

public class GetDeviceList extends TemplateUtil{
    private Logger log = Logger.getLogger(GetDeviceList.class);
    private static envSet envset=new envSet();
    private static final String TEMPLATE = FileUtil.loadResourceFile("模板.json");
    private static final JSONObject TEMPLATE_JO = JSONObject.parseObject(TEMPLATE);

    private static String geturl(String ccuid) {
        String baseurl=envset.setbaseurl();
        String url=baseurl+"/ccu/"+ccuid+"/deviceList";
        return url;
    }

    private static List<Header> getheader() {
        return envset.setheader();
    }

    private static String getccuid(){
        String cculist=GetCcuList.GetCcuList();
        String ccuid=GetCcuList.GetCcuId(cculist);
        return ccuid;
    }

    public static Map<String,String> getccuidmap(){
        String cculist=GetCcuList.GetCcuList();
        Map<String,String> ccuidmap=new HashMap<>();
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
        String ccuid=getccuid();
        String url=geturl(ccuid);
        List<Header> helist=getheader();
        response = given()
                .relaxedHTTPSValidation()
                .contentType("application/json;charset=UTF-8")
                .header(helist.get(0))
                .header(helist.get(1))
                .get(url);
            //System.out.println(response.asString());
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
        List<Header> helist=getheader();
        Map<String,String> ccuidmap=getccuidmap();
        Map<String,String> resmap=new HashMap<>();
        Iterator<Map.Entry<String,String>> iterator= ccuidmap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,String> entry = iterator.next();
            String ccuid = entry.getValue();
            String url=geturl(ccuid);
            response = given()
                    .relaxedHTTPSValidation()
                    .contentType("application/json;charset=UTF-8")
                    .header(helist.get(0))
                    .header(helist.get(1))
                    .get(url);
            String res=response.asString();
            resmap.put(ccuid,res);
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
