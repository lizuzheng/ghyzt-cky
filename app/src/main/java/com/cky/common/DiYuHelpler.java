package com.cky.common;

public class DiYuHelpler {

	// 所有的地域信息
	public static String[] getAllDiYU() {
		String[] diyu = { "天府新区", "龙泉驿区", "青白江区", "新都区", "温江区", "金堂县", "双流区",
				"郫县", "大邑县", "蒲江县", "新津县", "都江堰市", "彭州市", "邛崃市", "崇州市", "锦江区",
				"青羊区", "金牛区", "成华区", "武侯区", "简阳市", };
		return diyu;
	}

	// 所有的地域信息
	public static String[] getAllDiYU2() {
		String[] diyu = { "全部区市县", "天府新区", "龙泉驿区", "青白江区", "新都区", "温江区", "金堂县",
				"双流区", "郫县", "大邑县", "蒲江县", "新津县", "都江堰市", "彭州市", "邛崃市", "崇州市",
				"锦江区", "青羊区", "金牛区", "成华区", "武侯区", "简阳市", };
		return diyu;
	}

	// 判断一个点当前在哪个区
	public static String getPointDiYu(String diyu) {
		String[] allDiYu = getAllDiYU();

		for (int i = 0; i < allDiYu.length; i++) {
			if (diyu.contains(allDiYu[i])) {
				return allDiYu[i];

			}
		}

		return "成都市";

	}

}
