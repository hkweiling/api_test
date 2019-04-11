package com.rest.autotest.data;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.rest.autotest.http.GetDeviceList;

import com.rest.autotest.model.Device;
import com.rest.autotest.utils.JsonUtil;
import com.rest.autotest.utils.TemplateUtil;

import java.util.*;

public class DataBuilder {


    public static Map<String, JSONObject> optdatabuilder(){
        /**
         * @description: 单个主机设备操作接口数据构造
         * @param: []
         * @return: java.util.Map<java.lang.String,com.alibaba.fastjson.JSONObject>
         */
        List<Device> devinfo= GetDeviceList.suportdev(GetDeviceList.getdevicelist());
        Map<String, JSONObject> data=new HashMap<>();
        for(int i=0;i<devinfo.size();i++){
            int id=devinfo.get(i).getId();
            String devType=devinfo.get(i).getType();
            LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> operations = TemplateUtil.load(devType);
            int count=0;
            for(String opt:operations.keySet()){
                JSONObject info=new JSONObject();
                LinkedHashMap<String, LinkedHashMap<String, Object>> requestAndResponse = TemplateUtil.request(devType, opt);
                Map<String, Object> request = requestAndResponse.get("request");
                Map<String, Object> response = requestAndResponse.get("response");
                info.put("id",id);
                info.put("arg",request);
                info.put("res",response);
                data.put(devType+"_"+count,info);
                count++;
            }
        }
        return data;
    }

    public static Map<String,List<JSONObject>> optdatabuilderforccus(){
        /**
         * @description: 当前开发者账号下所有主机设备操作数据构造
         * @param: []
         * @return: java.util.LinkedHashMap<java.lang.String,java.util.List<com.alibaba.fastjson.JSONObject>>
         */
        Map<String,List<JSONObject>> datamap=new HashMap<>();
        Map<String,List<Device>> devinfomap=GetDeviceList.suportdevfromccus(GetDeviceList.getdevicelistfromccus());
        Iterator<Map.Entry<String,List<Device>>> it=devinfomap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,List<Device>> entry = it.next();
            String ccuid = entry.getKey();
            List<Device> devinfo=entry.getValue();
            List<JSONObject> data=new ArrayList<>();
            for(int i=0;i<devinfo.size();i++){
                int id=devinfo.get(i).getId();
                String devType=devinfo.get(i).getType();
                LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> operations = TemplateUtil.load(devType);
                int count=0;
                for(String opt:operations.keySet()){
                    JSONObject info=new JSONObject();
                    LinkedHashMap<String, LinkedHashMap<String, Object>> requestAndResponse = TemplateUtil.request(devType, opt);
                    Map<String, Object> request = requestAndResponse.get("request");
                    Map<String, Object> response = requestAndResponse.get("response");
                    info.put("id",id);
                    info.put("type",devType);
                    info.put("arg",request);
                    info.put("res",response);
                    data.add(info);
                    count++;
                }
            }
            if(data.size()!=0){
                datamap.put(ccuid,data);
            }
        }
        return datamap;
    }

    public static List<Map<Integer,String>> statusdatabuilder(){
        /**
         * @description:
         * @param: []
         * @return: java.util.List<com.alibaba.fastjson.JSONObject>
         */
        List<Map<Integer,String>> arglist=new ArrayList<>();
        String res=GetDeviceList.getdevicelist();
        List<Device> devlist= JsonUtil.fromJson(res, new TypeToken<List<Device>>() {
        }.getType());;
        for(Device d:devlist) {
            if (!(d.getType()).equals("UNKOWN")) {
                Map<Integer,String> arg = new HashMap<>();
                arg.put(d.getId(), d.getType());
                arglist.add(arg);
            }
        }
        return arglist;
    }

    public static Map<String,List<Map<Integer,String>>> statusdatabuilderforccus(){
        /**
         * @description:
         * @param: []
         * @return: java.util.Map<java.lang.String,java.util.List<java.util.Map<java.lang.Integer,java.lang.String>>>
         */
        Map<String,List<Map<Integer,String>>> ccusarglist=new HashMap<>();
        Map<String,String> resmap=GetDeviceList.getdevicelistfromccus();
        Iterator<Map.Entry<String,String>> iterator= resmap.entrySet().iterator();
        while (iterator.hasNext()) {
            List<Map<Integer,String>> arglist=new ArrayList<>();
            Map.Entry<String, String> entry = iterator.next();
            String ccuid = entry.getKey();
            String res = entry.getValue();
            List<Device> devlist = JsonUtil.fromJson(res, new TypeToken<List<Device>>() {
            }.getType());
            for (Device d : devlist) {
                if (!(d.getType()).equals("UNKOWN")) {
                    Map<Integer, String> arg = new HashMap<>();
                    arg.put(d.getId(), d.getType());
                    arglist.add(arg);
                }
            }
            ccusarglist.put(ccuid,arglist);
        }

        return ccusarglist;
    }
}
