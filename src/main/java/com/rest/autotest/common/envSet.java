package com.rest.autotest.common;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.data.DataBuilder;
import io.restassured.http.Header;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rest.autotest.utils.WritePropertiesUtil.writePropertiesFile;


public class envSet {
    static PropertieyConfig property=new PropertieyConfig();
    static Logger log = Logger.getLogger(envSet.class);


    public static String setbaseurl(){
        String url = null;
        try {
            String system = "env.properties";    //环境由filter配置
            String baseURI = property.getValue("baseURI", system);
            String basepath = property.getValue("basePath", system);
            int port = Integer.parseInt(property.getValue("port", system));
            url=baseURI+":"+port+basepath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static List<Header> setheader(){
        List<Header> list=new ArrayList<>();
        try {
            String appIdValue = property.getValue("appId", "request.properties");
            String appKeyValue = property.getValue("appKey", "request.properties");
            Header header1=new Header("appId", appIdValue);
            Header header2=new Header("appKey", appKeyValue);
            list.add(header1);
            list.add(header2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @AfterClass
    public void dataClear(){
        /**
         * @description: data数据清除
         * @param: []
         * @return: void
         */
        Map<String, JSONObject> data= DataBuilder.optdatabuilder();
        data.clear();
    }


    @AfterSuite
    public void createEnvPropertiesForReport() throws IOException {
        /**
         * @description: 创建environment.properties并放到allure-results目录下，测试报告展现
         * @param: []
         * @return: void
         */
        Map<String, String> data = new HashMap<>();
        String system = "env.properties";    //环境由filter配置
        String baseURI = property.getValue("baseURI", system);
        int port = Integer.parseInt(property.getValue("port", system));
        data.put("baseURI", baseURI+":"+port);
        writePropertiesFile(data);
    }

}
