package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.asserts.OptResponseAsserts;
import com.rest.autotest.common.envSet;

import com.rest.autotest.data.DataProviders;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.annotations.Description;
import java.util.List;
import static io.restassured.RestAssured.given;


@Title("设备控制")
@Description("灯光窗帘插座操作")
public class Optdevice{

//    private static envSet envset=new envSet();

    private static String geturl(String ccuid) {
        String url=null;
        String baseurl=envSet.setbaseurl();
        url=baseurl+"/ccu/"+ccuid+"/dev/";
        return url;
    }

    private static List<Header> getheader() {
        return envSet.setheader();
    }

    private static String getccuid(){
        String ccuid=null;
        String cculist=GetCcuList.GetCcuList();
        ccuid=GetCcuList.GetCcuId(cculist);
        return ccuid;
    }

    @Test(dataProvider = "dataprovider", dataProviderClass = DataProviders.class)
    @Title("单个主机设备控制")
    @Step("执行用例")
    public void optDevice(int id,JSONObject arg)  {
        /**
         * @description: 控制某台主机下的设备
         * @param: [id, arg]
         * @return: void
         */

        Response response = null;
        String ccuid = getccuid();
        String baseurl=geturl(ccuid);
        String url=baseurl+id+"/opt";
        List<Header> helist=getheader();
        response = given()
                .relaxedHTTPSValidation()
                .contentType("application/json;charset=UTF-8")
                .header(helist.get(0))
                .header(helist.get(1))
                .body(arg)
                .post(url);
        OptResponseAsserts.optResponseAsserts(response,id,url,arg);
    }
}
