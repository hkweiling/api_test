package com.rest.autotest.data;

import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.DataProvider;


import java.lang.reflect.Method;
import java.util.*;

public class DataProviders{

    @DataProvider(name = "dataprovider",parallel = true)
    public static Object[][] data(Method method){
        String methodName = method.getName(); //获取方法名
        Object[][] obs = null;

        if(methodName.equals("optDeviceForCcus")){
            Map<String,List<JSONObject>> datamap=DataBuilder.optdatabuilderforccus();
            Iterator<Map.Entry<String,List<JSONObject>>> it=datamap.entrySet().iterator();
            int j=0;
            while (it.hasNext()){
                List<JSONObject> ccudatas=new ArrayList<>();
                Map.Entry<String,List<JSONObject>> entry = it.next();
                String ccuid = entry.getKey();
                List<JSONObject> datalist=entry.getValue();
                for(JSONObject data : datalist) {
                    JSONObject ccudata=new JSONObject();
                    JSONObject arg = data.getJSONObject("arg");
                    int id = data.getInteger("id");
                    ccudata.put("id",id);
                    ccudata.put("arg",arg);
                    ccudatas.add(ccudata);
                }

                obs = new Object[datamap.size()][2];
                obs[j][0] = ccuid;
                obs[j][1] = ccudatas;
                j++;
            }
            datamap.clear();
        }
        else if(methodName.equals("optDevice")){
            Map<String, JSONObject> data=DataBuilder.optdatabuilder();
            obs = new Object[data.size()][2];
            int j=0;
            for(Map.Entry<String,JSONObject> entry : data.entrySet()){
                JSONObject bodyJson=entry.getValue();
                JSONObject arg=bodyJson.getJSONObject("arg");
                int id=bodyJson.getInteger("id");
                obs[j][0] = id;
                obs[j][1] = arg;
                j++;
            }
            data.clear();
        }
        else if(methodName.equals("getDeviceStatus")){
            List<Map<Integer,String>> data=DataBuilder.statusdatabuilder();
            obs = new Object[data.size()][2];
            int j=0;
            for(int i=0;i<data.size();i++){
                for(Map.Entry<Integer,String> entry:data.get(i).entrySet()){
                    obs[j][0] =entry.getKey();
                    obs[j][1] =entry.getValue();
                    j++;
                }
            }
        }
        else if(methodName.equals("getDeviceStatusForCcus")){
            Map<String,List<Map<Integer,String>>> data=DataBuilder.statusdatabuilderforccus();
            obs = new Object[data.size()][2];
            int j=0;
            Iterator<Map.Entry<String,List<Map<Integer,String>>>> iterator= data.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String,List<Map<Integer,String>>> entry = iterator.next();
                obs[j][0] = entry.getKey();
                obs[j][1] = entry.getValue();
                j++;
            }
        }
        return obs;
    }
}
