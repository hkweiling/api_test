package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.asserts.StatusResponseAsserts;
import com.rest.autotest.common.PropertieyConfig;
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
    static PropertieyConfig property=new PropertieyConfig();
    private static Logger log = Logger.getLogger(GetDeviceList.class);
    private static String baseurl=envSet.setbaseurl();
    private static String ccuid=GetCcuList.GetCcuId();


    @BeforeClass
    @Step("url拼接")
    public static void seturl(){
        String url="";
        url=baseurl+"/ccu/"+ccuid+"/deviceStatus";
        RestAssured.baseURI=url;
    }

    @Title("查询设备状态")
    @Test(dataProvider = "dataprovider",dataProviderClass = DataProviders.class,description = "查询设备状态")
    public static void getDeviceStatus(int id,String type){
        /**
         * @description:查询单台主机下设备状态
         * @param: []
         * @return: java.lang.String
         */
        Response response=null;
        seturl();
        JSONObject body=setbody(id,type);
        response=sendrequest(body);
        assertres(response,body,RestAssured.baseURI);
    }

    @Step("结果断言")
    public static void assertres(Response response,JSONObject body,String url){
        StatusResponseAsserts.statusResponseAsserts(response,body,url);
    }


}
