package com.cky.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cky.model.LandUseModel;

/**
 * 对用地性质进行数据写入
 * 
 * @author lzz
 * 
 */
public class LandUse {

	/**
	 * 获取所有类型
	 * 
	 * @return
	 */
	public static Map<String, List<String>> getLandMap() {
		// 装用地性质的
		Map<String, List<String>> landMap = new HashMap<String, List<String>>();
		// 住宅用地
		landMap.put("住宅用地", null);

		// 工业用地
		landMap.put("工业用地", null);

		// 商业服务业设施用地
		List<String> sfList = new ArrayList<String>();
		sfList.add("商业用地");
		sfList.add("商务用地");
		sfList.add("娱乐康体用地");
		sfList.add("其他服务设施用地");

		landMap.put("商业服务业设施用地", sfList);

		// 商业服务业子类
		// 1、商业用地
		List<String> syList = new ArrayList<String>();
		syList.add("零售商业用地、旅馆用地）");
		syList.add("批发市场用地餐饮用地");
		syList.add("旅馆用地");

		landMap.put("商业用地", syList);

		// 2、商务用地
		List<String> swList = new ArrayList<String>();
		swList.add("金融保险用地");
		swList.add("艺术传媒用地");
		swList.add("其他商务用地");

		landMap.put("商业用地", swList);

		// 3、娱乐康体用地
		List<String> ykList = new ArrayList<String>();
		ykList.add("娱乐用地");
		ykList.add("康体用地");

		landMap.put("娱乐康体用地", ykList);

		// 4、其他服务设施用地
		landMap.put("娱乐康体用地", null);

		// 公建配套设施
		List<String> gpList = new ArrayList<String>();
		gpList.add("农贸市场");
		gpList.add("社区服务中心");
		gpList.add("社区用房");
		gpList.add("街道办事处");
		gpList.add("邮政服务网点");
		gpList.add("文化活动中心");
		gpList.add("文化活动站");
		gpList.add("社区卫生服务中心");
		gpList.add("社区卫生服务站");
		gpList.add("体育活动中心");
		gpList.add("社会停车场（库）");
		gpList.add("加油站、加气站");
		gpList.add("垃圾用房");
		gpList.add("地铁站");
		gpList.add("公共厕所");

		landMap.put("公建配套设施", gpList);

		// 公共管理与公共服务设施

		List<String> ggList = new ArrayList<String>();
		ggList.add("医院");
		ggList.add("教育科研用地");
		ggList.add("社会服务福利用地");
		ggList.add("体育用地");
		ggList.add("行政办公用地");
		ggList.add("文化设施用地");
		ggList.add("文化古迹用地");
		ggList.add("外事用地");
		ggList.add("宗教用地");

		landMap.put("公共管理与公共服务设施", ggList);

		// 公用设施用地

		List<String> gyList = new ArrayList<String>();
		gyList.add("供水用地");
		gyList.add("供电用地");
		gyList.add("消防用地");
		gyList.add("环卫用地");

		landMap.put("公用设施用地", gyList);

		return landMap;
	}

	public static Map<String, List<LandUseModel>> getLandList() {
		Map<String, List<LandUseModel>> map = new HashMap<String, List<LandUseModel>>();

		String[] s0 = { "居住用地", "工业用地", "商业服务业设施用地", "公共管理与公共服务设施用地",
				"道路与交通设施用地", "公用设施用地", "物流仓储用地", "绿地与广场用地" };
		List<LandUseModel> list_0 = getList(0, s0);

		map.put("0", list_0);

		// 1000子类
		String[] s1000 = { "住宅用地", "服务设施用地" };
		List<LandUseModel> list_1000 = getList(1000, s1000);
		map.put("1000", list_1000);

		// 3000子类
		String[] s3000 = { "商业用地", "商务用地", "娱乐康体用地", "公用设施营业网点用地", "其他服务设施用地" };
		List<LandUseModel> list_3000 = getList(3000, s3000);
		map.put("3000", list_3000);

		// 4000子类
		String[] s4000 = { "医疗卫生用地", "教育科研用地", "行政办公用地", "体育用地", "社会服务福利用地" , "文化设施用地", "文化古迹用地", "外事用地",
				"宗教用地", "其他公共管理与公共服务设施用地"
		};
		List<LandUseModel> list_4000 = getList(4000, s4000);
		map.put("4000", list_4000);

		// 5000子类
		String[] s5000 = { "交通枢纽用地", "交通场站用地", "其他交通设施用地"};
		List<LandUseModel> list_5000 = getList(5000, s5000);
		map.put("5000", list_5000);

		// 6000子类
		String[] s6000 = { "供应设施用地", "环境设施用地", "安全设施用地", "其他公用设施用地" };
		List<LandUseModel> list_6000 = getList(6000, s6000);
		map.put("6000", list_6000);

		return map;
	}

	private static List<LandUseModel> getList(int parent, String[] s) {
		List<LandUseModel> list = new ArrayList<LandUseModel>();

		if (parent == 0) {

			for (int i = 0; i < s.length; i++) {
				int code = (parent + 1 + i) * 1000;
				LandUseModel model = new LandUseModel(code + "", parent + "",
						s[i]);
				list.add(model);
			}

		} else {
			for (int i = 0; i < s.length; i++) {
				int code = parent + 1 + i;
				LandUseModel model = new LandUseModel(code + "", parent + "",
						s[i]);
				list.add(model);
			}
		}

		return list;
	}

}
