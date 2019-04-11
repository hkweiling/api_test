package com.rest.autotest.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rest.autotest.enums.TestMode;
import com.rest.autotest.model.TestCase;

import java.util.List;
import java.util.Map;

public class TestCaseLoadUtil {

    private static final String TEMPLATE = FileUtil.loadResourceFile("模板.json");
    private static final JSONObject TEMPLATE_JO = JSONObject.parseObject(TEMPLATE);

    public static TestCase loadTestCase(String device, String opt, TestMode testMode) {
        /**
         * {
         *       "desc": "调节亮度",
         *       "request": [
         *         {
         *           "desc": "参数:亮度值",
         *           "key": "progress",
         *           "type": "range",
         *           "range": [
         *             0,
         *             100,
         *             1
         *           ]
         *         }
         *       ],
         *       "response": [
         *         {
         *           "desc": "返回值:是否成功",
         *           "key": "success",
         *           "type": "bool",
         *           "bool": true
         *         },
         *         {
         *           "desc": "返回值:错误码",
         *           "key": "errorCode",
         *           "type": "int",
         *           "int": 0
         *         }
         *       ]
         *     }
         */
        JSONObject optJo = checkOptSupport(device, opt);
        TestCase testCase = new TestCase(device, opt);
        load(optJo, testCase.getRequests(), testCase.getResponses(), testMode);
        return testCase;
    }

    private static void load(JSONObject optJo, List<Map<String, Object>> requests, List<Map<String, Object>> responses, TestMode testMode) {
        loadRequests(optJo.getJSONArray("request"), requests, testMode);
        loadResponses(optJo.getJSONArray("response"), responses, testMode);
    }

    private static void loadRequests(JSONArray request, List<Map<String, Object>> requests, TestMode testMode) {
        for (int i = 0; i < request.size(); i++) {
            JSONObject argJo = request.getJSONObject(i);
            loadRequest(argJo, requests, testMode);
        }
    }

    private static void loadRequest(JSONObject argJo, List<Map<String, Object>> requests, TestMode testMode) {

    }

    private static void loadResponses(JSONArray response, List<Map<String, Object>> responses, TestMode testMode) {
        for (int i = 0; i < response.size(); i++) {
            JSONObject argJo = response.getJSONObject(i);
            loadResponse(argJo, responses, testMode);
        }
    }

    private static void loadResponse(JSONObject argJo, List<Map<String, Object>> responses, TestMode testMode) {

    }


    private static JSONObject checkDeviceSupport(String device) {
        if (!TEMPLATE_JO.containsKey(device)) {
            throw new RuntimeException("不支持设备" + device);
        }
        return TEMPLATE_JO.getJSONObject(device);
    }

    private static JSONObject checkOptSupport(String device, String opt) {
        JSONObject deviceJo = checkDeviceSupport(device);
        if (!deviceJo.containsKey(opt)) {
            throw new RuntimeException(device + "不支持操作" + opt);
        }
        return deviceJo.getJSONObject(opt);
    }

}
