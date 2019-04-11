package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;

import com.rest.autotest.asserts.StatusResponseAsserts;
import com.rest.autotest.common.envSet;
import com.rest.autotest.data.DataProviders;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * @description:
 * @author:
 * @time: 2019/3/29 14:04
 */

@Title("查询设备状态")
@Description("查询多个主机下设备状态")
public class GetDeviceStatusForCcus {

    private static String geturl(String ccuid) {
        String url=null;
        String baseurl=envSet.setbaseurl();
        url=baseurl+"/ccu/"+ccuid+"/deviceStatus";
        return url;
    }

    private static List<Header> getheader() {
        return envSet.setheader();
    }


    @Test(dataProvider = "dataprovider", dataProviderClass = DataProviders.class)
    @Title("多个主机设备状态查询")
    @Step("执行用例")
    public static void getDeviceStatusForCcus(String ccuid, List<Map<Integer,String>> arglist){
        /**
         * @description: 查询当前开发者账号下所有主机设备状态
         * @param: [ccuid, arglist]
         * @return: void
         */
        Response response = null;
        String url = seturl(ccuid);
        for(Map<Integer,String> arg:arglist){
            Iterator<Map.Entry<Integer,String>> iterator= arg.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<Integer,String> entry = iterator.next();
                JSONObject body=setbody(entry.getKey(),entry.getValue());
                response = sendrequest(body,url);
                assertres(response,entry.getKey(),url);
            }
        }

    }

    @io.qameta.allure.Step("URL拼接")
    public static String seturl(String ccuid){
        return geturl(ccuid);
    }

    @io.qameta.allure.Step("请求body构造")
    public static JSONObject setbody(int id,String type){
        String argstr="{\"id\": "+id+",\"type\":\""+type+"\"}";
        return JSONObject.parseObject(argstr);
    }

    @io.qameta.allure.Step("请求发送")
    public static Response sendrequest(JSONObject body,String url){
        Response response=null;
        List<Header> helist=getheader();
        response = given()
                .relaxedHTTPSValidation()
                .contentType("application/json;charset=UTF-8")
                .header(helist.get(0))
                .header(helist.get(1))
                .body(body)
                .post(url);
        return response;
    }

    @io.qameta.allure.Step("结果断言")
    public static void assertres(Response response,int id,String url){
        StatusResponseAsserts.statusResponseAsserts(response,id,url);
    }

}
