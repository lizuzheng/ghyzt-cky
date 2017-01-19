package com.map.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 用于解析Json的工具类,所有私有方法都不要去管他是干嘛的
 * 
 * @author Beike
 * 
 */
public class JsonUtils {

	/**
	 * 解析单个Json,这是在你要的节点在内层节点之中才使用的方法
	 * 
	 * @param json
	 *            字符串类型的json数据
	 * @param attribute
	 *            一个数组，表示节点，当你要的属性在内层节点，需要把外层节点的属性写上去，最后再写你想要的属性
	 * @return
	 */
	public static String singleJson(String json, String[] attribute) {

		try {
			JSONTokener jsonParser = new JSONTokener(json);

			JSONObject person = (JSONObject) jsonParser.nextValue();

			for (int i = 0; i < attribute.length - 1; i++) {
				person = person.getJSONObject(attribute[i]);
			}

			return person.getString(attribute[attribute.length - 1]);

		} catch (JSONException e) {

			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 解析单个字符串，此种方法仅仅适用于你要的属性在最外层节点
	 * 
	 * @param json
	 *            字符串型的json数据
	 * @param attribute
	 *            你想要的属性
	 * @return 返回解析出来的值，为空表示根本没找到这个属性
	 */
	public static String singleJson(String json, String attribute) {
		String[] attStrings = { attribute };
		return singleJson(json, attStrings);

	}

	/**
	 * 这个方法是用来遍历json数据的，返回一个map的键值对，所以，要是这个json数据里面有相同属性，则这方法不适用
	 * 
	 * @param json
	 *            字符串型json数据
	 * @return
	 */
	public static Map<String, String> getKeys(String json) {

		try {
			JSONTokener jsonParser = new JSONTokener(json);

			JSONObject person = (JSONObject) jsonParser.nextValue();

			Map<String, String> map = new HashMap<String, String>();

			getNewJson(person, map);

			return map;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 用于遍历的辅助方法
	 * 
	 * @param person
	 * @return
	 */
	public Object getObject(JSONObject person, Map<String, String> map) {
		Iterator<String> keys;
		try {
			keys = person.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				Object o = person.get(key);

				if (o instanceof JSONObject) {

					getNewJson((JSONObject) o, map);

				} else {
					map.put(key, person.getString(key));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 为了防止错误，暂时只想到这个效率低下的办法，木要管他是干嘛的，这个只有上帝和我知道
	 * 
	 * @param person
	 * @return
	 */
	private static JsonUtils getNewJson(JSONObject person,
			Map<String, String> map) {

		new JsonUtils().getObject(person, map);
		return null;

	}

	/**
	 * 这个是通过遍历的方法找出
	 * 
	 * @param json
	 *            json字符串
	 * @param key
	 *            需要读取的key值
	 * @return
	 */
	public static String getKey(String json, String key) {
		try {
			JSONTokener jsonParser = new JSONTokener(json);

			JSONObject person = (JSONObject) jsonParser.nextValue();

			return getNewJson(person, key);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	
	
	/**
	 * 遍历取得相应键的辅助方法
	 * 
	 * @param person
	 * @param keyValue
	 * @return
	 */
	public String getObject(JSONObject person, String keyValue) {
		Iterator<String> keys;
		try {
			keys = person.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				Object o = person.get(key);

				if (o instanceof JSONObject) {

					return getNewJson((JSONObject) o, keyValue);
					
				} else {
					//System.out.println(key + " " + person.getString(key));
					if (key.equals(keyValue)) {
						return person.getString(key);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 私有方法你就不要去关心他是干嘛的
	 * 
	 * @param person
	 * @param keyValue
	 * @return
	 */
	private static String getNewJson(JSONObject person, String keyValue) {

		return new JsonUtils().getObject(person, keyValue);

	}
}
