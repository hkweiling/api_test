package com.rest.autotest.reports;

import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.common.envSet;
import io.qameta.allure.Step;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;

/**
 * @description:
 * @author:
 * @time: 2019/4/11 15:49
 */
public class TestStep {


    @Step("请求body构造")
    public static JSONObject setbody(int id, String type){
        String argstr="{\"id\": "+id+",\"type\":\""+type+"\"}";
        return JSONObject.parseObject(argstr);
    }

    @Step("请求发送")
    public static Response sendrequest(JSONObject body){
        Response response=null;
        response = given()
                .relaxedHTTPSValidation()
                .contentType("application/json;charset=UTF-8")
                .headers(envSet.setheaders())
                .body(body)
                .post();
        return response;
    }


}
