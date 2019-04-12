package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.asserts.StatusResponseAsserts;
import com.rest.autotest.common.envSet;
import com.rest.autotest.data.DataProviders;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Title;
import io.qameta.allure.Step;


import static com.rest.autotest.reports.TestStep.sendrequest;
import static com.rest.autotest.reports.TestStep.setbody;

/**
 * @description:
 * @author:
 * @time: 2019/3/29 10:54
 */


@Title("查询单个主机下设备状态")
@Listeners
public class GetDeviceStatus {
    private Logger log = Logger.getLogger(GetDeviceList.class);

    private static String getccuid(){
        String cculist=GetCcuList.getCcuList();
        return GetCcuList.GetCcuId(cculist);
    }

    @BeforeClass
    @Step("url拼接")
    public static void seturl(){
        String url="";
        String baseurl=envSet.setbaseurl();
        String ccuid=getccuid();
        url=baseurl+"/ccu/"+ccuid+"/deviceStatus";
        RestAssured.baseURI=url;
    }

    @Test(dataProvider = "dataprovider",dataProviderClass = DataProviders.class,description = "查询设备状态",groups = "GetDeviceStatus")
    public static void getDeviceStatus(int id,String type){
        /**
         * @description:查询单台主机下设备状态
         * @param: []
         * @return: java.lang.String
         */
        Response response=null;
        JSONObject body=setbody(id,type);
        response=sendrequest(body);
        assertres(response,id,RestAssured.baseURI);
    }

    @Step("结果断言")
    public static void assertres(Response response,int id,String url){
        StatusResponseAsserts.statusResponseAsserts(response,id,url);
    }

}
