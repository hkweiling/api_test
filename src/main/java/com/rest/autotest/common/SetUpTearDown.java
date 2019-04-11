package com.rest.autotest.common;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.data.DataBuilder;
import io.restassured.http.Header;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.rest.autotest.common.envSet.property;
import static com.rest.autotest.utils.WritePropertiesUtil.writePropertiesFile;


public class SetUpTearDown {
    public static Header header1=null;
    public static Header header2=null;

    //环境配置
    @BeforeClass
    public void envSetUp() {

        try {
            String system = "env.properties";    //环境由filter配置
            PropertieyConfig property=new PropertieyConfig();
            String appIdValue = property.getValue("appId", "request.properties");
            String appKeyValue = property.getValue("appKey", "request.properties");
            header1=new Header("appId", appIdValue);
            header2=new Header("appKey", appKeyValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void dataClear(){
        Map<String, JSONObject> data= DataBuilder.optdatabuilder();
        data.clear();
    }

    /*
     *创建environment.properties并放到allure-results目录下，测试报告展现
     */
    @AfterSuite
    public void createEnvPropertiesForReport() throws IOException {
        Map<String, String> data = new HashMap<>();
        String system = "env.properties";    //环境由filter配置
        String baseURI = property.getValue("baseURI", system);
        int port = Integer.parseInt(property.getValue("port", system));
        data.put("baseURI", baseURI+":"+port);
        writePropertiesFile(data);
    }
}
