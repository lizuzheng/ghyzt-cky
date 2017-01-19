package com.mapview.click;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;
import com.cky.ghyzt.R;
import com.cky.ghyzt.MainActivity;
import com.cky.model.ShapeModel;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.dialog.LoadDialog;
import com.map.dialog.ShowShapeDialog;
import com.map.utils.BitmapUtils;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;
import com.map.utils.PointToString;

public class DrawAllPoint extends MapOnTouchListener implements Runnable {

	private Context context;
	private GraphicsLayer graphicsLayer;
	private HashMap<Integer, ShapeModel> map;
	private ShapeModel shapeModel;
	private MapView mMapview;
	private ArrayList<String> minImageList;
	private ArrayList<String> maxImageList;
	private Bitmap resultBitmap;

	public DrawAllPoint(Context context, MapView view) {
		super(context, view);
		this.context = context;
		graphicsLayer = MainActivity.ghlayer;
		this.mMapview = view;
		// 移除原本存在的
		graphicsLayer.removeAll();
		LoadDialog.show(context, true, "加载中……");

		this.getAllShape();
	}

	// 获取保存的所有点
	private void getAllShape() {
		String Url = MyAppliaction.AppUrl + "getUserCGraphics.ashx";
		RequestParams params = new RequestParams();
		params.addBodyParameter("CGraphicsCreatNum",
				UserSingle.getSingle().userModel.getUserNum());
		params.addBodyParameter("CGraphicsOrg", "GHYZT");

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, Url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LoadDialog.close();
				Toast.makeText(context, "连接服务器失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				System.out.println(arg0.result);
				resultShape(arg0.result);
			}

		});
	}

	// 对查询到的所有的结果进行解析
	protected void resultShape(String result) {

		try {
			String code = JsonUtils.getKey(result, "code");

			if (code == null || code.equals("")) {
				Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
				return;
			}
			// 成功
			if (code.equals("Success")) {

				String data = JsonUtils.getKey(result, "data");
				// 获取到所有的保存图层
				List<ShapeModel> listModel = MapJsonUtils.getAllShape(data);

				this.showAll(listModel);

			} else {
				LoadDialog.close();
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			LoadDialog.close();
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onSingleTap(MotionEvent e) {

		int[] ids = graphicsLayer.getGraphicIDs(e.getX(), e.getY(), 10);

		if (ids.length > 0) {
			LoadDialog.show(context, true, "等待加载……");
			for (int i = 0; i < ids.length; i++) {

				try {

					this.showDialogInfo(map.get(ids[i]));
					break;
				} catch (Exception e2) {
					continue;
				}

			}

		}

		return super.onSingleTap(e);
	}

	// 将所有保存的数据显示出来
	private void showAll(List<ShapeModel> list) {

		int id = -1;

		map = new HashMap<Integer, ShapeModel>();

		System.out.println("点的个数:" + list.size());
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getCGraphicsType().equals("Point")) {
				Point p = PointToString.stringToPoint(list.get(i)
						.getCGraphicsGeometry());
				// 表示保存的是一个点
				Graphic graphic = new Graphic(p, new SimpleMarkerSymbol(
						Color.RED, 10, STYLE.CIRCLE));
				id = graphicsLayer.addGraphic(graphic);
				map.put(id, list.get(i));

				// 绘制名称
				View view = View.inflate(context, R.layout.showmaptext, null);
				TextView tv_show_text = (TextView) view
						.findViewById(R.id.tv_show_text);

				tv_show_text.setText(list.get(i).getCGraphicsName());
				tv_show_text.setWidth(400);

				Bitmap bit = getViewBitmap(view, 400, 100);

				Drawable drawable = new BitmapDrawable(bit);
				PictureMarkerSymbol pic = new PictureMarkerSymbol(drawable);
				pic.setOffsetX(40);
				pic.setOffsetY(20);
				Graphic gText = new Graphic(p, pic);
				graphicsLayer.addGraphic(gText);

			} else if (list.get(i).getCGraphicsType().equals("Polyline")) {
				// 表示保存的是一根线
				Polyline line = PointToString.StringToLine(list.get(i)
						.getCGraphicsGeometry());
				id = graphicsLayer.addGraphic(new Graphic(line,
						new SimpleLineSymbol(Color.RED, 2,
								SimpleLineSymbol.STYLE.DASH)));
				map.put(id, list.get(i));

				// 绘制标题
				View view = View.inflate(context, R.layout.showmaptext, null);
				TextView tv_show_text = (TextView) view
						.findViewById(R.id.tv_show_text);

				tv_show_text.setText(list.get(i).getCGraphicsName());
				tv_show_text.setWidth(400);

				Bitmap bit = getViewBitmap(view, 400, 100);

				Drawable drawable = new BitmapDrawable(bit);
				PictureMarkerSymbol pic = new PictureMarkerSymbol(drawable);
				pic.setOffsetX(10);
				pic.setOffsetY(5);

				Envelope env = new Envelope();
				line.queryEnvelope(env);
				Point center = env.getCenter();
				Graphic gText = new Graphic(center, pic);
				graphicsLayer.addGraphic(gText);

			} else if (list.get(i).getCGraphicsType().equals("Polygon")) {
				// 表示保存的是一个面
				Polygon gon = PointToString.stringToArea(list.get(i)
						.getCGraphicsGeometry());

				Graphic g = new Graphic(gon,
						new SimpleFillSymbol(Color.BLUE).setAlpha(70));

				id = graphicsLayer.addGraphic(g);

				Graphic g1 = new Graphic(gon, new SimpleLineSymbol(Color.RED,
						2, SimpleLineSymbol.STYLE.SOLID));

				graphicsLayer.addGraphic(g1);

				map.put(id, list.get(i));

				// 绘制标题
				View view = View.inflate(context, R.layout.showmaptext, null);
				TextView tv_show_text = (TextView) view
						.findViewById(R.id.tv_show_text);

				tv_show_text.setText(list.get(i).getCGraphicsName());
				tv_show_text.setWidth(400);

				Bitmap bit = getViewBitmap(view, 400, 100);

				Drawable drawable = new BitmapDrawable(bit);
				PictureMarkerSymbol pic = new PictureMarkerSymbol(drawable);
				pic.setOffsetX(10);
				pic.setOffsetY(5);

				Envelope env = new Envelope();
				gon.queryEnvelope(env);
				Point center = env.getCenter();
				Graphic gText = new Graphic(center, pic);
				graphicsLayer.addGraphic(gText);

			}
		}
		LoadDialog.close();

	}

	/**
	 * 把View绘制到Bitmap上
	 * 
	 * @param view
	 *            需要绘制的View
	 * @param width
	 *            该View的宽度
	 * @param height
	 *            该View的高度
	 * @return 返回Bitmap对象 add by csj 13-11-6
	 */
	public Bitmap getViewBitmap(View comBitmap, int width, int height) {

		Bitmap bitmap = null;
		if (comBitmap != null) {
			comBitmap.clearFocus();
			comBitmap.setPressed(false);

			boolean willNotCache = comBitmap.willNotCacheDrawing();
			comBitmap.setWillNotCacheDrawing(false);

			// Reset the drawing cache background color to fully transparent
			// for the duration of this operation
			int color = comBitmap.getDrawingCacheBackgroundColor();
			comBitmap.setDrawingCacheBackgroundColor(0);
			float alpha = comBitmap.getAlpha();
			comBitmap.setAlpha(1.0f);

			if (color != 0) {
				comBitmap.destroyDrawingCache();
			}

			int widthSpec = View.MeasureSpec.makeMeasureSpec(width,
					View.MeasureSpec.EXACTLY);
			int heightSpec = View.MeasureSpec.makeMeasureSpec(height,
					View.MeasureSpec.EXACTLY);
			comBitmap.measure(widthSpec, heightSpec);
			comBitmap.layout(0, 0, width, height);

			comBitmap.buildDrawingCache();
			Bitmap cacheBitmap = comBitmap.getDrawingCache();
			if (cacheBitmap == null) {
				Log.e("view.ProcessImageToBlur", "failed getViewBitmap("
						+ comBitmap + ")", new RuntimeException());
				return null;
			}
			bitmap = Bitmap.createBitmap(cacheBitmap);
			// Restore the view
			comBitmap.setAlpha(alpha);
			comBitmap.destroyDrawingCache();
			comBitmap.setWillNotCacheDrawing(willNotCache);
			comBitmap.setDrawingCacheBackgroundColor(color);
		}
		return bitmap;
	}

	private void showDialogInfo(ShapeModel shapeModel) {

		minImageList = new ArrayList<String>();
		maxImageList = new ArrayList<String>();
		if (shapeModel.getListImage() != null
				&& shapeModel.getListImage().size() > 0) {
			for (int i = 0; i < shapeModel.getListImage().size(); i++) {
				minImageList.add(MyAppliaction.AppUrl
						+ "getGraphicImage.ashx?photoname="
						+ shapeModel.getListImage().get(i));
				maxImageList.add(MyAppliaction.AppUrl
						+ "getGraphicImage.ashx?photoname="
						+ shapeModel.getListImage().get(i));
			}
		}

		this.shapeModel = shapeModel;

		Thread th = new Thread(this);
		th.start();

	}

	final Handler hd = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			LoadDialog.close();
			ShowShapeDialog dia = new ShowShapeDialog(context, mMapview);
			dia.show(context, shapeModel, resultBitmap, minImageList,
					maxImageList);
		}

	};

	@Override
	public void run() {

		Message msg = new Message();
		if (minImageList != null && minImageList.size() > 0) {

			try {
				Bitmap bit = com.jztx.utils.HttpUtils.getBitmap(minImageList
						.get(0));
				//Bitmap bit =null;
						resultBitmap = BitmapUtils.compressBitmap(bit, 400);
			} catch (Exception e) {
				resultBitmap = null;
			}

		} else {
			resultBitmap = null;
		}
		hd.sendMessage(msg);
	}

}
