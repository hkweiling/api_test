package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;

import com.rest.autotest.asserts.StatusResponseAsserts;
import com.rest.autotest.common.envSet;
import com.rest.autotest.data.DataProviders;
import com.rest.autotest.reports.TestStep;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Title;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @description:
 * @author:
 * @time: 2019/3/29 14:04
 */

@Title("查询多个个主机下设备状态")
@Listeners
public class GetDeviceStatusForCcus {

    @Test(dataProvider = "dataprovider", dataProviderClass = DataProviders.class,description = "查询设备状态",groups = "GetDeviceStatus")
    public static void getDeviceStatusForCcus(String ccuid, List<Map<Integer,String>> arglist){
        /**
         * @description: 查询当前开发者账号下所有主机设备状态
         * @param: [ccuid, arglist]
         * @return: void
         */
        Response response = null;
        seturl(ccuid);
        for(Map<Integer,String> arg:arglist){
            Iterator<Map.Entry<Integer,String>> iterator= arg.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<Integer,String> entry = iterator.next();
                JSONObject body= TestStep.setbody(entry.getKey(),entry.getValue());
                response = TestStep.sendrequest(body);
                assertres(response,entry.getKey(),RestAssured.baseURI);
            }
        }

    }

    @Step("URL拼接")
    public static void seturl(String ccuid){
        String url="";
        String baseurl=envSet.setbaseurl();
        url=baseurl+"/ccu/"+ccuid+"/deviceStatus";
        RestAssured.baseURI=url;
    }


    @Step("结果断言")
    public static void assertres(Response response,int id,String url){
        StatusResponseAsserts.statusResponseAsserts(response,id,url);
    }

}
