package com.rest.autotest.http;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.asserts.OptResponseAsserts;
import com.rest.autotest.common.envSet;
import com.rest.autotest.data.DataProviders;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;
import java.util.List;

import static io.restassured.RestAssured.given;

@Title("设备控制")
@Description("灯光窗帘插座操作")
public class OptDeviceForCcus {

    private static String geturl(String ccuid) {
        String url=null;
        String baseurl=envSet.setbaseurl();
        url=baseurl+"/ccu/"+ccuid;
        return url;
    }

    private static List<Header> getheader() {
        return envSet.setheader();
    }


    @Test(dataProvider = "dataprovider", dataProviderClass = DataProviders.class)
    @Title("多个主机设备控制")
    @Step("执行用例")
    public void optDeviceForCcus(String ccuid, List<JSONObject> ccudata) {
        /**
         * @description: 控制当前开发者账号下所有主机设备
         * @param: [ccuid, ccudata]
         * @return: void
         */
        Response response = null;
        List<Header> helist = getheader();
        for (JSONObject data : ccudata) {
            JSONObject arg = data.getJSONObject("arg");
            int id = data.getInteger("id");
            String url = geturl(ccuid) + "/dev/" + id + "/opt";
            response = given()
                    .relaxedHTTPSValidation()
                    .contentType("application/json;charset=UTF-8")
                    .header(helist.get(0))
                    .header(helist.get(1))
                    .body(arg)
                    .post(url);
            OptResponseAsserts.optResponseAsserts(response, id, url, arg);
        }
    }

}
