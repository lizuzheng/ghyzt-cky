package com.cky.common;

import java.util.Map;
import java.util.Set;

import com.esri.core.geometry.Envelope;
import com.esri.core.map.Graphic;

public class GraphicHelper {
	public static String getShowText(String displayField, Graphic graphic) {

		try {
			Map<String, Object> attributes = graphic.getAttributes();
			Set<String> keys = attributes.keySet();
			String text = "";
			for (String key : keys) {
				if (key.equalsIgnoreCase(displayField)
						&& null != attributes.get(key)
						&& attributes.get(key).toString().trim().length() > 0) {
					text = attributes.get(key).toString();
					break;
				}
			}

			return text;
		} catch (Exception ex) {
			// XiLog.error("读取要素显示标题出错", ex);
			return "";
		}
	}

	/**
	 * 获取指定graphic要素的外边界
	 * 
	 * @param graphics
	 * @return
	 */
	public static Envelope getExtent(Graphic graphic) {
		if (null == graphic) {
			return null;
		}

		double xmin = 0;
		double ymin = 0;
		double xmax = 0;
		double ymax = 0;
		Envelope envelope = new Envelope();

		graphic.getGeometry().queryEnvelope(envelope);

		xmin = envelope.getXMin();
		ymin = envelope.getYMin();
		xmax = envelope.getXMax();
		ymax = envelope.getYMax();
		Envelope extend = new Envelope(xmin, ymin, xmax, ymax);

		return extend;
	}

	/**
	 * 获取指定graphic集合所有要素的外边界
	 * 
	 * @param graphics
	 * @return
	 */
	public static Envelope getExtent(Graphic[] graphics) {
		if (null == graphics) {
			return null;
		}

		double xmin = 0;
		double ymin = 0;
		double xmax = 0;
		double ymax = 0;

		Envelope envelope = new Envelope();

		for (int i = 0; i < graphics.length; ++i) {
			graphics[i].getGeometry().queryEnvelope(envelope);
			if (0 == i) {
				xmin = envelope.getXMin();
				ymin = envelope.getYMin();
				xmax = envelope.getXMax();
				ymax = envelope.getYMax();
			} else {
				if (xmin > envelope.getXMin()) {
					xmin = envelope.getXMin();
				}
				if (ymin > envelope.getYMin()) {
					ymin = envelope.getYMin();
				}
				if (xmax < envelope.getXMax()) {
					xmax = envelope.getXMax();
				}
				if (ymax < envelope.getYMax()) {
					ymax = envelope.getYMax();
				}
			}
		}
		Envelope extend = new Envelope(xmin, ymin, xmax, ymax);

		return extend;
	}
}
