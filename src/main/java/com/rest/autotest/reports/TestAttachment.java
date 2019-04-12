package com.rest.autotest.reports;

import com.alibaba.fastjson.JSONObject;
import io.qameta.allure.Attachment;

/*
 *测试步骤，测试报告中展现
 */
public class TestAttachment {

    public static void requestAndRespondBody(String URL, String Body,String Respond){
        String request=requestBody(URL,Body);
        String response=respondBody(Respond);
    }

    @Attachment(value = "请求报文", type = "text/plain")
    private static String requestBody(String URL, String body) {
        String str=null;
        //格式化json串
        boolean prettyFormat = true; //格式化输出
        JSONObject jsonObject = JSONObject.parseObject(body);
        str = JSONObject.toJSONString(jsonObject,prettyFormat);

        //报告展现请求报文
        return URL+"\n"+str;
    }

    @Attachment(value = "响应报文", type = "text/plain")
    private static String respondBody(String respond) {
        String str=null;
        //格式化json串
        boolean prettyFormat = true; //格式化输出
        JSONObject jsonObject = JSONObject.parseObject(respond);
        str = JSONObject.toJSONString(jsonObject,prettyFormat);

        //报告展现响应报文
        return str;
    }


    @Attachment(value = "响应报文断言结果", type = "text/plain")
    public static String assertRespond(String assertResult){
        //报告展现断言结果
        return assertResult;
    }
}
