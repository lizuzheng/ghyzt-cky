package com.cky.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.cky.model.LandUseModel;

/**
 * 根据返回的值获取到json
 * 
 * @author lzz
 * 
 */
public class LouCengJson {

	private static Map<String, List<LandUseModel>> map = LandUse.getLandList();

	public static String getJson(List<String> list) {

		if (list != null && list.size() > 0) {

			String start = "{";

			for (int i = 0; i < list.size(); i++) {
				String code = getCode(list.get(i));
				int louceg = i + 1;
				String str = louceg + "";

				start += '"' + str + '"' + ":" + '"' + code + '"';
				if (i != list.size() - 1) {
					start += ',';
				}
			}
			return start += "}";

		}

		return "";
	}

	/**
	 * 根据名称获取到相应的code
	 * 
	 * @param value
	 * @return
	 */
	public static String getCode(String value) {

		for (String s : map.keySet()) {
			List<LandUseModel> list = map.get(s);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					LandUseModel model = list.get(i);
					if (value.equals(model.getValue())) {
						return model.getCode();

					}
				}
			}
		}
		return "0";
	}

	public static String getValue(String code)
	{
		for (String s : map.keySet()) {
			List<LandUseModel> list = map.get(s);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					LandUseModel model = list.get(i);
					if (code.equals(model.getCode())) {
						return model.getValue();

					}
				}
			}
		}
		return "";

	}
	
	
	public static List<String> getList(String json) {
		if(json==null||json.equalsIgnoreCase(""))
		{
			return null;
		}
		
		List<String> list = new ArrayList<String>();

		JSONObject jo;
		try {
			jo = new JSONObject(json);
			for (int i = 1; i < jo.length() + 1; i++) {

				String value=getValue(jo.getString(i+""));
				list.add(value);
			}

		} catch (JSONException e) {
			System.out.println("解析错误");
			return null;
		}

		return list;
	}

}
