package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;

import com.rest.autotest.asserts.StatusResponseAsserts;
import com.rest.autotest.common.envSet;
import com.rest.autotest.data.DataProviders;
import com.rest.autotest.reports.TestStep;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Title;


/**
 * @description:
 * @author:
 * @time: 2019/3/29 14:04
 */

@Title("查询多个主机下设备状态")
@Listeners
public class GetDeviceStatusForCcus {
    private static Logger log = Logger.getLogger(GetDeviceList.class);
    private static String baseurl=envSet.setbaseurl();

    @Title("查询设备状态")
    @Test(dataProvider = "dataprovider", dataProviderClass = DataProviders.class,description = "查询设备状态")
    public static void getDeviceStatusForCcus(String ccuid, JSONObject arg){
        /**
         * @description: 查询当前开发者账号下所有主机设备状态
         * @param: [ccuid,arglist]
         * @return: void
         */
        Response response = null;
        seturl(ccuid);
        response = TestStep.sendrequest(arg);
        assertres(response,arg,RestAssured.baseURI);
    }

    @Step("URL拼接")
    public static void seturl(String ccuid){
        String url="";
        url=baseurl+"/ccu/"+ccuid+"/deviceStatus";
        RestAssured.baseURI=url;
    }


    @Step("结果断言")
    public static void assertres(Response response,JSONObject body,String url){
        StatusResponseAsserts.statusResponseAsserts(response,body,url);
    }

}
