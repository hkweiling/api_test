package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.asserts.OptResponseAsserts;
import com.rest.autotest.common.envSet;

import com.rest.autotest.data.DataProviders;
import com.rest.autotest.reports.TestStep;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Title;


@Title("单个主机下设备操作")
@Listeners
public class Optdevice{
    private static String baseurl=envSet.setbaseurl();
    private static String ccuid=GetCcuList.GetCcuId();

    @Title("设备操作")
    @Test(dataProvider = "dataprovider", dataProviderClass = DataProviders.class,description = "设备操作")
    public void optDevice(int id,JSONObject arg)  {
        /**
         * @description: 控制某台主机下的设备
         * @param: [id, arg]
         * @return: void
         */

        Response response = null;
        seturl(id);
        response= TestStep.sendrequest(arg);
        assertres(response,id,RestAssured.baseURI,arg);
    }

    @Step("URL拼接")
    public static void seturl(int id){
        String url="";
        url=baseurl+"/ccu/"+ccuid+"/dev/"+id+"/opt";
        RestAssured.baseURI=url;
    }

    @Step("响应结果断言")
    public static void assertres(Response response,int id,String url,JSONObject arg){
        OptResponseAsserts.optResponseAsserts(response,id,url,arg);
    }
}
