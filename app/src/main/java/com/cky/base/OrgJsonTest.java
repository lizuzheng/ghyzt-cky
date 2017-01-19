package com.cky.base;

import java.text.ParseException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 使用json-lib构造和解析Json数据
 * 
 * @author Alexia
 * @date 2013/5/23
 * 
 */
public class OrgJsonTest {

    /**
     * 构造Json数据
     * 
     * @return
     * @throws JSONException
     */
    public static String BuildJson(Map<String,String> map) throws JSONException {

        // JSON格式数据解析对象
        JSONObject jo = new JSONObject();

        // 将Map转换为JSONArray数据
        JSONArray ja = new JSONArray();
        ja.put(map);

        System.out.println(ja.toString());

        return jo.toString();

    }

    /**
     * 解析Json数据
     * 
     * @param jsonString
     *            Json数据字符串
     * @throws JSONException
     * @throws ParseException
     */
    public static void ParseJson(String jsonString) throws JSONException,
            ParseException {

        JSONObject jo = new JSONObject(jsonString);
        JSONArray ja = jo.getJSONArray("map");

        System.out.println("\n将Json数据解析为Map：");
        System.out.println("name: " + ja.getJSONObject(0).getString("name")
                + " sex: " + ja.getJSONObject(0).getString("sex") + " age: "
                + ja.getJSONObject(0).getInt("age"));



    }



}