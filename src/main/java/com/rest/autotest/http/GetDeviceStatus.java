package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.asserts.StatusResponseAsserts;
import com.rest.autotest.common.envSet;
import com.rest.autotest.data.DataProviders;
import io.qameta.allure.Description;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Title;
import io.qameta.allure.Step;


import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * @description:
 * @author:
 * @time: 2019/3/29 10:54
 */


@Title("查询单个主机下设备状态")
@Listeners
public class GetDeviceStatus {
    private Logger log = Logger.getLogger(GetDeviceList.class);

    private static String geturl(String ccuid) {
        String url=null;
        String baseurl=envSet.setbaseurl();
        url=baseurl+"/ccu/"+ccuid+"/deviceStatus";
        return url;
    }

    private static List<Header> getheader() {
        return envSet.setheader();
    }

    private static String getccuid(){
        String cculist=GetCcuList.GetCcuList();
        return GetCcuList.GetCcuId(cculist);
    }


    @Test(dataProvider = "dataprovider", dataProviderClass = DataProviders.class,description = "查询设备状态")
    public static void getDeviceStatus(int id,String type){
        /**
         * @description:查询单台主机下设备状态
         * @param: []
         * @return: java.lang.String
         */
        Response response=null;
        String url=seturl();
        JSONObject body=setbody(id, type);
        response=sendrequest(body,url);
        assertres(response,id,url);
    }

    @Step("URL拼接")
    public static String seturl(){
        String ccuid=getccuid();
        return geturl(ccuid);
    }

    @Step("请求body构造")
    public static JSONObject setbody(int id,String type){
        String argstr="{\"id\": "+id+",\"type\":\""+type+"\"}";
        return JSONObject.parseObject(argstr);
    }

    @Step("请求发送")
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

    @Step("结果断言")
    public static void assertres(Response response,int id,String url){
        StatusResponseAsserts.statusResponseAsserts(response,id,url);
    }

}
