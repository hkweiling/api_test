package com.rest.autotest.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

/**
 * 模板处理
 */
public class TemplateUtil {

    private static final String TEMPLATE = FileUtil.loadResourceFile("模板.json");
    private static final JSONObject TEMPLATE_JO = JSONObject.parseObject(TEMPLATE);

//    public static void main(String[] args) {
//        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> operations = load("KONKE_ZIGBEE_CHOPIN_16A_SOCKET");
//        for (String operation : operations.keySet()) {
//            System.out.println(JsonUtil.toJson(request("KONKE_ZIGBEE_CHOPIN_16A_SOCKET", operation)));
//        }
//    }

    public static LinkedHashMap<String, LinkedHashMap<String, Object>> request(String device, String opt) {
        LinkedHashMap<String, LinkedHashMap<String, Object>> load = load(device, opt);
        LinkedHashMap<String, Object> request = new LinkedHashMap<>();
        request.put("action", opt);
        request.put("actionArg", load.get("request"));
        LinkedHashMap<String, LinkedHashMap<String, Object>> result = new LinkedHashMap<>();
        result.put("request", request);
        result.put("response", load.get("response"));
        return result;
    }

    public static LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>>> load() {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>>> LinkedHashMap = new LinkedHashMap<>();
        for (String device : TEMPLATE_JO.keySet()) {
            if ("desc".equals(device)) {
                continue;
            }
            LinkedHashMap.put(device, load(device));
        }
        return LinkedHashMap;
    }

    public static LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> load(String device) {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> LinkedHashMap = new LinkedHashMap<>();
        JSONObject deviceJo = checkDeviceSupport(device);
        for (String opt : deviceJo.keySet()) {
            if ("desc".equals(opt)) {
                continue;
            }
            LinkedHashMap.put(opt, load(device, opt));
        }
        return LinkedHashMap;
    }

    public static LinkedHashMap<String, LinkedHashMap<String, Object>> load(String device, String opt) {
        JSONObject optJo = checkOptSupport(device, opt);
        LinkedHashMap<String, LinkedHashMap<String, Object>> result = new LinkedHashMap<>();
        JSONArray request = optJo.getJSONArray("request");
        result.put("request", parse(request));
        JSONArray response = optJo.getJSONArray("response");
        result.put("response", parse(response));
        return result;
    }

    private static LinkedHashMap<String, Object> parse(JSONArray jsonArray) {
        LinkedHashMap<String, Object> LinkedHashMap = new LinkedHashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            LinkedHashMap.putAll(parse(jsonArray.getJSONObject(i)));
        }
        return LinkedHashMap;
    }

    /**
     * {
     * "desc": "返回值:是否成功",
     * "key": "success",
     * "type": "bool",
     * "bool": true
     * }
     */
    private static LinkedHashMap<String, Object> parse(JSONObject jsonObject) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        String key = jsonObject.getString("key");
        String type = jsonObject.getString("type");
        switch (type) {
            case "int":
            case "str":
                result.put(key, jsonObject.getInteger(type));
                break;
            case "array":
                result.put(key, arr(jsonObject.getJSONObject(type)));
                break;
            case "obj":
                result.put(key, parse(jsonObject.getJSONObject(type)));
                break;
            case "range":
                String rangeType = jsonObject.containsKey("rangeType") ? jsonObject.getString("rangeType") : null;
                result.put(key, range(jsonObject.getString(type), rangeType));
                break;
            case "enum":
                result.put(key, enums(jsonObject.getJSONObject(type)));
                break;
            case "bool":
                if (jsonObject.containsKey(type)) {
                    result.put(key, jsonObject.getBoolean(type));
                } else {
                    result.put(key, new Random().nextBoolean());
                }
                break;
            case "date":
                result.put(key, date(jsonObject.getJSONObject(type)));
                break;
            default:
                break;
        }
        return result;
    }

    private static Object parseNonKey(JSONObject jsonObject) {
        String type = jsonObject.getString("type");
        switch (type) {
            case "int":
            case "str":
                return jsonObject.getInteger(type);
            case "array":
                return arr(jsonObject.getJSONObject(type));
            case "obj":
                return parse(jsonObject.getJSONObject(type));
            case "range":
                String rangeType = jsonObject.containsKey("rangeType") ? jsonObject.getString("rangeType") : null;
                return range(jsonObject.getString(type), rangeType);
            case "bool":
                if (jsonObject.containsKey(type)) {
                    return jsonObject.getBoolean(type);
                } else {
                    return new Random().nextBoolean();
                }
            default:
                throw new RuntimeException("不支持的类型" + type);
        }
    }

    /**
     * {
     * "desc": "参数:开始日期",
     * "key": "beginDay",
     * "type": "date",
     * "date": {
     * "format": "yyyy-MM-dd",
     * "range": "[5,10,1]"
     * }
     * }
     */
    private static Object date(JSONObject dateJo) {
        String format = dateJo.getString("format");
        String range = dateJo.getString("range");
        int val = (int) range(range);
        LocalDateTime localDateTime = LocalDateTime.now();
        if (format.endsWith("s")) {//秒
            localDateTime = localDateTime.plusSeconds(val);
        } else if (format.endsWith("m")) {//分
            localDateTime = localDateTime.plusMinutes(val);
        } else if (format.endsWith("H")) {//时
            localDateTime = localDateTime.plusHours(val);
        } else if (format.endsWith("d")) {//日
            localDateTime = localDateTime.plusDays(val);
        } else if (format.endsWith("M")) {//月
            localDateTime = localDateTime.plusMonths(val);
        } else if (format.endsWith("y")) {//年
            localDateTime = localDateTime.plusYears(val);
        } else {
            throw new RuntimeException("不支持的时间格式" + format);
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    private static Object enums(JSONObject enumJo) {
        String type = enumJo.getString("type");
        String content = enumJo.getString(type);
        String[] split;
        switch (type) {
            case "int":
                content = content.substring(1, content.length() - 1);
                split = content.split(",");
                return Integer.parseInt(split[new Random().nextInt(split.length)]);
            case "str":
                content = content.substring(2, content.length() - 2);
                split = content.split("\",\"");
                return split[new Random().nextInt(split.length)];
            default:
                throw new RuntimeException("不支持的枚举内部类型" + type);
        }
    }

    private static Object range(String range, String rangeType) {
        Object rangeRet = range(range);
        //如果没有指定类型，默认根据起始值与步长计算得出，可能是int可能是double
        if (StringUtils.isBlank(rangeType) || "int".equals(rangeType) || "double".equals(rangeType)) {
            return rangeRet;
        }
        //目前支持指定rangeType为字符串格式
        return String.valueOf(rangeRet);
    }

    /**
     * @param range [0,100,1],(0,100,1),[0,100,1),(0,100,1]
     */
    private static Object range(String range) {
        String start = range.substring(0, 1);
        String end = range.substring(range.length() - 1);
        range = range.substring(1, range.length() - 1);
        String[] split = range.split(",");
        String startVar = split[0];
        String endVar = split[1];
        String stepVar = split[2];
        String finalType = "int";
        int decimalPlaces = 0;
        //根据开始值与步长，选择小数点位数多的数字的小数点位数作为最终结果的小数点位数
        if (startVar.contains(".") || stepVar.contains(".")) {
            finalType = "double";
            decimalPlaces = Math.max(decimalSize(startVar), decimalSize(stepVar));
        }
        double min = Double.parseDouble(startVar);
        double max = Double.parseDouble(endVar);
        double step = Double.parseDouble(stepVar);
        BigDecimal b = new BigDecimal(step);
        step = b.doubleValue();
        int parts = (int) ((max - min) / step);
        double val;
        if (start.equals("[") && end.equals("]")) {
            val = min + new Random().nextInt(parts + 1) * step;
        } else if (start.equals("[") && end.equals(")")) {
            val = min + new Random().nextInt(parts) * step;
        } else if (start.equals("(") && end.equals("]")) {
            val = min + step + new Random().nextInt(parts) * step;
        } else {
            val = min + step + new Random().nextInt(parts - 1) * step;
        }
        if ("int".equals(finalType)) {
            return (int) val;
        }
        BigDecimal bg = new BigDecimal(val);
        return bg.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static Object arr(JSONObject jsonObject) {
        List<Object> list = new ArrayList<>();
        int len = jsonObject.getInteger("len");
        for (int i = 0; i < len; i++) {
            list.add(parseNonKey(jsonObject));
        }
        return list;
    }

    public static JSONObject checkDeviceSupport(String device) {
        if (!TEMPLATE_JO.containsKey(device)) {
            throw new RuntimeException("不支持设备" + device);
        }
        return TEMPLATE_JO.getJSONObject(device);
    }

    public static JSONObject checkOptSupport(String device, String opt) {
        JSONObject deviceJo = checkDeviceSupport(device);
        if (!deviceJo.containsKey(opt)) {
            throw new RuntimeException(device + "不支持操作" + opt);
        }
        return deviceJo.getJSONObject(opt);
    }

    private static int decimalSize(String data) {
        if (data.contains(".")) {
            return data.length() - data.indexOf(".") - 1;
        }
        return 0;
    }

    //    private static List<LinkedHashMap<String, Object>> parseList(String device, String opt, JSONArray jsonArray) {
    //        System.out.println("///////////////////////////////////////////////////");
    //        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
    //        for (int i = 0; i < jsonArray.size(); i++) {
    //            JSONObject jo = jsonArray.getJSONObject(i);
    //            System.out.println("> " + jo.toJSONString());
    //        }
    //        return result;
    //    }
}
