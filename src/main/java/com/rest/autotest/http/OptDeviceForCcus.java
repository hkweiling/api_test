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
import java.util.List;


@Title("多个主机下设备操作")
@Listeners
public class OptDeviceForCcus {

    @Test(dataProvider = "dataprovider", dataProviderClass = DataProviders.class,description = "设备操作",groups = "OptDevice")
    public void optDeviceForCcus(String ccuid, List<JSONObject> ccudata) {
        /**
         * @description: 控制当前开发者账号下所有主机设备
         * @param: [ccuid, ccudata]
         * @return: void
         */
        Response response = null;
        for (JSONObject data : ccudata) {
            JSONObject arg = data.getJSONObject("arg");
            int id = data.getInteger("id");
            seturl(ccuid,id);
            response = TestStep.sendrequest(arg);
            assertres(response,id,RestAssured.baseURI,arg);
        }
    }

    @Step("URL拼接")
    public static void seturl(String ccuid,int id){
        String url="";
        String baseurl=envSet.setbaseurl();
        url=baseurl+"/ccu/"+ccuid+"/dev/"+id+"/opt";
        RestAssured.baseURI=url;
    }

    @Step("响应结果断言")
    public static void assertres(Response response,int id,String url,JSONObject arg){
        OptResponseAsserts.optResponseAsserts(response,id,url,arg);
    }

}
