package com.map.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

import com.cky.common.DiYuHelpler;
import com.cky.model.AutoTextModel;
import com.cky.model.GPSRrecordModel;
import com.cky.model.LayerImageModel;
import com.cky.model.LayerModel;
import com.cky.model.ShapeModel;
import com.cky.model.TuBanModel;
import com.cky.model.UserModel;
import com.cky.model.VersionModel;
import com.esri.android.map.Layer;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.Point;

public class MapJsonUtils {

	/**
	 * 判断表格是否存在，并且里面的数据是不是为空
	 * 
	 * @param json
	 * @return
	 */
	public static Boolean isNull(String json) {

		System.out.println(json);

		try {
			if (JsonUtils.getKey(json, "total") == null
					|| JsonUtils.getKey(json, "total").equals("")) {
				return true;
			}

		} catch (Exception e) {
			return true;
		}

		return JsonUtils.getKey(json, "total").equals("0");
	}

	// 统计标记图形
	public static Map<String, Integer> TongjiBiaoJi(String json) {

		// 创建一个列表用于返回结果
		Map<String, Integer> resultMap = new HashMap<String, Integer>();

		// 获取到所有的区域
		String[] allquyu = DiYuHelpler.getAllDiYU();

		// 初始化值
		for (int i = 0; i < allquyu.length; i++) {
			resultMap.put(allquyu[i], 0);
		}

		// 解析json
		if (json.substring(0, 1).equals("[")) {
			json = json.substring(1, json.length() - 1);
		}

		try {
			JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

			for (int i = 0; i < jsonObjs.length(); i++) {

				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);
				String quyu = map.get("quyu");

				try {
					// 获取到当前的个数
					int count = resultMap.get(quyu) + 1;
					// 设置当前个数
					resultMap.put(quyu, count);

				} catch (Exception e) {
					resultMap.put(quyu, 1);
				}

			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return resultMap;

	}

	// 统计图斑结果
	public static Map<String, String> TongjiTuBan(String json) {

		// 创建一个列表用于返回结果
		Map<String, String> resultMap = new HashMap<String, String>();

		// 解析json
		if (json.substring(0, 1).equals("[")) {
			json = json.substring(1, json.length() - 1);
		}

		try {
			JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

			for (int i = 0; i < jsonObjs.length(); i++) {

				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);

				resultMap.put(map.get("区市县"), map.get("count"));

			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return resultMap;

	}
	
	
	// 统计图斑结果
		public static Map<String, String> TongjiTuBanXiangZhen(String json) {

			// 创建一个列表用于返回结果
			Map<String, String> resultMap = new HashMap<String, String>();

			// 解析json
			if (json.substring(0, 1).equals("[")) {
				json = json.substring(1, json.length() - 1);
			}

			try {
				JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

				for (int i = 0; i < jsonObjs.length(); i++) {

					String value = jsonObjs.get(i).toString();
					Map<String, String> map = JsonUtils.getKeys(value);

					resultMap.put(map.get("乡镇"), map.get("count"));

				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			return resultMap;

		}

	/**
	 * 获取到保存的所有的图形
	 * 
	 * @param json
	 * @return
	 */
	public static List<ShapeModel> getAllShape(String json) {
		if (json.substring(0, 1).equals("[")) {
			json = json.substring(1, json.length() - 1);
		}
		List<ShapeModel> listModel = new ArrayList<ShapeModel>();
		try {
			JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

			for (int i = 0; i < jsonObjs.length(); i++) {

				ShapeModel model = new ShapeModel();
				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);

				model.setCGraphicsContent(map.get("CGraphicsContent"));
				model.setCGraphicsCreatNum(map.get("CGraphicsCreatNum"));
				model.setCGraphicsCreatTime(map.get("CGraphicsCreatTime"));
				model.setCGraphicsGeometry(map.get("CGraphicsGeometry"));
				model.setCGraphicsId(map.get("CGraphicsId"));
				model.setCGraphicsIsShare(map.get("CGraphicsIsShare"));
				model.setCGraphicsName(map.get("CGraphicsName"));
				model.setCGraphicsOrg(map.get("CGraphicsOrg"));
				model.setCGraphicsType(map.get("CGraphicsType"));
				model.setQuyu(map.get("CGraphicsQuYu"));

				try {
					if (map.get("CGraphicsImage") != null
							&& !map.get("CGraphicsImage").equals("")) {
						String[] str = map.get("CGraphicsImage").split(";");
						List<String> list = new ArrayList<String>();
						for (int j = 0; j < str.length; j++) {
							list.add(str[j]);
						}
						model.setListImage(list);
						System.out.println(map.get("CGraphicsImage") + "  "
								+ list.size());
					}
				} catch (Exception e) {
				}

				listModel.add(model);
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return listModel;

	}

	/**
	 * 获取当前用户的登陆信息
	 * 
	 * @param json
	 * @return
	 */
	public static UserModel getUser(String json) {
		if (json.substring(0, 1).equals("[")) {
			json = json.substring(1, json.length() - 1);
		}

		UserModel user = new UserModel();
		try {
			JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

			for (int i = 0; i < jsonObjs.length(); i++) {

				
//				[UserID]
//			      ,[UserNum]
//			      ,[UserPassword]
//			      ,[UserName]
//			      ,[MachineNumber]
//			      ,[IMEI]
//			      ,[UserPhone]
//			      ,[StateNum]
//			      ,[OrgNum]
//			      ,[IsNotVerification]
//			      ,[BeiZhu]
				

				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);
				user.setUserID(map.get("UserID"));
				user.setUserNum(map.get("UserNum"));
				user.setUserPassword(map.get("UserPassword"));
				user.setUserName(map.get("UserName"));
				user.setMachineNumber(map.get("MachineNumber"));
				user.setIMEI(map.get("IMEI"));
				user.setUserPhone(map.get("UserPhone"));
				user.setStateNum(map.get("StateNum"));
				user.setOrgNum(map.get("OrgNum"));
				user.setIsNotVerification(map.get("IsNotVerification"));
				user.setBeiZhu(map.get("BeiZhu"));

			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return user;

	}

	/**
	 * 获取图片信息
	 * 
	 * @param json
	 * @return
	 */
	public static List<String> getImgPath(String json) {
		String pic_path = "";
		List<String> listPath = new ArrayList<String>();
		try {
			JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

			for (int i = 0; i < jsonObjs.length(); i++) {

				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);
				pic_path = map.get("pic_path");
			}

			String[] path = pic_path.split(";");
			for (int i = 0; i < path.length; i++) {
				listPath.add(path[i]);
			}

			return listPath;

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return null;

	}

	// 解析搜索返回的值
	public static List<AutoTextModel> getAutoText(String json) {
		List<AutoTextModel> listAuto = new ArrayList<AutoTextModel>();
		try {
			JSONArray jsonObjs = new JSONArray(json);

			for (int i = 0; i < jsonObjs.length(); i++) {

				AutoTextModel model = new AutoTextModel();

				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);
				model.setCategory(map.get("category"));
				model.setKeyword(map.get("keyword"));
				model.setLabel(map.get("label"));
				model.setObjid(map.get("objid"));
				model.setPinyin(map.get("pinyin"));

				// 生成点
				Point point = new Point();
				String mapX = map.get("x");
				String mapY = map.get("y");
				if (mapX != null && !mapX.equals("") && mapY != null
						&& !mapY.equals("")) {

					double x = Double.parseDouble(mapX);
					double y = Double.parseDouble(mapY);
					point.setX(x);
					point.setY(y);
				}

				model.setPoint(point);
				model.setShortpinyin(map.get("shortpinyin"));
				model.setType(map.get("type"));

				listAuto.add(model);
			}
			return listAuto;

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return null;

	}

	// 解析版本信息
	public static VersionModel getVersion(String json) {

		if (json.substring(0, 1).equals("[")) {
			json = json.substring(1, json.length() - 1);
		}

		try {
			VersionModel model = new VersionModel();
			JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

			for (int i = 0; i < jsonObjs.length(); i++) {

				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);

				model.setVersionCode(map.get("VersionCode"));
				model.setVersionContent(map.get("VersionContent"));
				model.setVersionId(map.get("VersionId"));
				model.setVersionNum(map.get("VersionNum"));
				model.setVersionTime(map.get("VersionTime"));

			}

			return model;
		} catch (JSONException e) {

			return null;
		}

	}

	// 解析图层
	public static List<LayerModel> getLayers(String json) {

		if (json.substring(0, 1).equals("[")) {
			json = json.substring(1, json.length() - 1);
		}

		try {
			List<LayerModel> list = new ArrayList<LayerModel>();
			JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

			for (int i = 0; i < jsonObjs.length(); i++) {
				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);

				LayerModel model = new LayerModel();
				Layer layer = null;

				if (map.get("ALayerType").equals("LT_Tiled")) {
					// 透明度
					int toumingdu = Integer.parseInt(map.get("LayerOpcity"));
					Boolean visible = false;
					if (map.get("LayerIsShow").equals("true")) {
						visible = true;
					}

					System.out.println(map.get("ALayerName") + "切片");
					System.out.println(map.get("ALayerUrl") + "切片");

					layer = new ArcGISTiledMapServiceLayer(map.get("ALayerUrl"));
					layer.setName(map.get("ALayerName"));
					layer.setOpacity((float) (toumingdu / 100.0));
					layer.setVisible(visible);

				} else if (map.get("ALayerType").equals("LT_Dem")) {
					// 透明度
					int toumingdu = Integer.parseInt(map.get("LayerOpcity"));
					Boolean visible = false;
					if (map.get("LayerIsShow").equals("true")) {
						visible = true;
					}

					System.out.println(map.get("ALayerName") + "矢量");

					layer = new ArcGISDynamicMapServiceLayer(
							map.get("ALayerUrl"));
					layer.setName(map.get("ALayerName"));
					layer.setOpacity((float) (toumingdu / 100.0));
					layer.setVisible(visible);
				}

				model.setLayer(layer);
				// 是否查询
				if (map.get("LayerIsQuery") != null
						&& map.get("LayerIsQuery").equals("true")) {
					model.setIsQuery(true);

				} else {
					model.setIsQuery(false);
				}
				//
				// // 查询网址
				if (map.get("LayerIsQueryUrl") != null) {

					model.setQueryUrl(map.get("LayerIsQueryUrl"));

				}
				//
				// // 查询字段
				if (map.get("LayerIsQueryField") != null) {
					String result = "";
					try {
						result = map.get("LayerIsQueryField").toString();
						String[] LayerShowFiled = result.split(",");
						model.setQuertFiled(LayerShowFiled);
					} catch (Exception e) {

					}

				}
				//
				// // //显示字段
				if (map.get("LayerShowField") != null) {

					String result = "";
					try {
						result = map.get("LayerShowField").toString();
						String[] LayerShowFiled = result.split(",");
						model.setShowFiled(LayerShowFiled);
					} catch (Exception e) {

					}

				}

				//
				// //查询图层
				if (map.get("LayerQueryLayerID") != null
						&& !map.get("LayerQueryLayerID").equals("")) {

					String[] layerIdStr = map.get("LayerQueryLayerID").split(
							",");

					int[] layerId = new int[layerIdStr.length];
					for (int j = 0; j < layerIdStr.length; j++) {

						try {
							layerId[j] = Integer.parseInt(layerIdStr[j]);
						} catch (NumberFormatException e) {

						}

					}

					model.setLayerId(layerId);
				}
				//
				// //样式
				if (map.get("LayerShowStyle") != null) {

					model.setStyle(map.get("LayerShowStyle"));

				}
				//

				list.add(model);
			}

			return list;
		} catch (JSONException e) {

			return null;
		}

	}

	// 解析音像对比图层
	public static List<LayerImageModel> getLayersImage(String json) {
		if (json.substring(0, 1).equals("[")) {
			json = json.substring(1, json.length() - 1);
		}

		try {
			List<LayerImageModel> list = new ArrayList<LayerImageModel>();
			JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

			for (int i = 0; i < jsonObjs.length(); i++) {
				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);

				LayerImageModel model = new LayerImageModel();
				Layer layer = null;
				Layer layer_right = null;

				if (map.get("ALayerType").equals("LT_Tiled")) {
					// 透明度
					int toumingdu = Integer.parseInt(map.get("LayerOpcity"));
					Boolean visible = false;

					layer = new ArcGISTiledMapServiceLayer(map.get("ALayerUrl"));
					layer.setName(map.get("ALayerName"));
					layer.setOpacity((float) (toumingdu / 100.0));
					layer.setVisible(visible);

					layer_right = new ArcGISTiledMapServiceLayer(
							map.get("ALayerUrl"));
					layer_right.setName(map.get("ALayerName"));
					layer_right.setOpacity((float) (toumingdu / 100.0));
					layer_right.setVisible(visible);

				} else if (map.get("ALayerType").equals("LT_Dem")) {
					// 透明度
					int toumingdu = Integer.parseInt(map.get("LayerOpcity"));
					Boolean visible = false;

					layer = new ArcGISDynamicMapServiceLayer(
							map.get("ALayerUrl"));
					layer.setName(map.get("ALayerName"));
					layer.setOpacity((float) (toumingdu / 100.0));
					layer.setVisible(visible);

					layer_right = new ArcGISDynamicMapServiceLayer(
							map.get("ALayerUrl"));
					layer_right.setName(map.get("ALayerName"));
					layer_right.setOpacity((float) (toumingdu / 100.0));
					layer_right.setVisible(visible);
				}

				model.setLayer(layer);
				model.setLayerRight(layer_right);

				// 是否是基本底图
				Boolean isBaise = false;
				if (map.get("IsBaise").equals("true")) {
					isBaise = true;
				}
				model.setIsBaise(isBaise);

				model.setIsShow(map.get("IsShow"));

				list.add(model);
			}

			return list;
		} catch (JSONException e) {

			return null;
		}
	}

	// 解析GPS数据
	public static List<GPSRrecordModel> getGPSModel(String json) {
		if (json.substring(0, 1).equals("[")) {
			json = json.substring(1, json.length() - 1);
		}

		try {
			List<GPSRrecordModel> list = new ArrayList<GPSRrecordModel>();
			JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

			for (int i = 0; i < jsonObjs.length(); i++) {

				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);

				GPSRrecordModel model = new GPSRrecordModel();

				model.setGPSRrecordCreatTime(map.get("GPSRrecordCreatTime"));
				model.setGPSRrecordID(map.get("GPSRrecordID"));
				model.setGPSRrecordLat(map.get("GPSRrecordLat"));
				model.setGPSRrecordLong(map.get("GPSRrecordLong"));
				model.setGPSRrecordUserNum(map.get("GPSRrecordUserNum"));
				model.setGPSRrecordUserName(map.get("UserName"));
				list.add(model);
			}

			return list;
		} catch (JSONException e) {

			return null;
		}

	}

	//解析图斑
	public static List<TuBanModel> getTubanChaXun(String json) {
		if (json.substring(0, 1).equals("[")) {
			json = json.substring(1, json.length() - 1);
		}
		List<TuBanModel> listModel = new ArrayList<TuBanModel>();
		try {
			JSONArray jsonObjs = new JSONObject(json).getJSONArray("rows");

			for (int i = 0; i < jsonObjs.length(); i++) {

				TuBanModel model = new TuBanModel();
				String value = jsonObjs.get(i).toString();
				Map<String, String> map = JsonUtils.getKeys(value);

				model.setBeizhu(map.get("备注"));
				model.setBianhao(map.get("编号"));
				model.setBianhuanianfen(map.get("变化年份"));
				model.setBz(map.get("bz"));
				model.setId(map.get("Id"));
				model.setoBJECTID(map.get("OBJECTID"));
				model.setoBJECTID_1(map.get("OBJECTID_1"));
				model.setQushixian(map.get("区市县"));
				model.setTubanmianji(map.get("图斑面积"));
				model.setWeizhi(map.get("位置"));
				model.setXiangzheng(map.get("乡镇"));
				
	
				listModel.add(model);
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return listModel;
	}
}
