package com.cky.ghyzt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;
import com.cky.base.ViewState;
import com.cky.common.GraphicHelper;
import com.cky.common.SerializableChaXunZhuanTi;
import com.cky.common.SerializableMap;
import com.cky.model.AutoTextModel;
import com.cky.model.CaiJiDianDaoChuStateModel;
import com.cky.model.LayerModel;
import com.cky.model.QueryViewModel;
import com.cky.view.FunctionBtn;
import com.cky.view.ImageBtn;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.dialog.DialogUtils;
import com.map.dialog.LoadDialog;
import com.map.dialog.MeasureDialog;
import com.map.dialog.MoreDilog;
import com.map.dialog.ShowShapeDialog;
import com.map.dialog.savePointDialog;
import com.map.dialog.selectDrawDialog;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;
import com.mapview.click.DrawGraphics;
import com.mapview.click.DrawShapeModelPoint;
import com.mapview.click.InitMapClick;
import com.mapview.click.MapLoad;
import com.mapview.click.MapNullCick;
import com.mapview.click.MapSize;
import com.mapview.click.MyListLayerAdapter;
import com.mapview.click.MyLoactionListener;
import com.mapview.click.MyZoomListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cky.ghyzt.R;

public class MainActivity extends Activity implements OnClickListener {

    private Context context;
    private Activity activity;
    public static MapView mMapview;
    public static ArcGISFeatureLayer fLayer;
    public static GraphicsLayer ghlayer;

    public static TextView tv_bilichi;
    private int locatState = 0;// 0表示未定位 1表示定位状态 2表示超出屏幕中心
    private View viewList;
    private Button btn_dingwei;
    private SerializableChaXunZhuanTi chaxunmodel;
    private QueryViewModel queryViewModel;
    private CaiJiDianDaoChuStateModel cajidaochu;
    public static List<LayerModel> listLayers;
    public static EditText ed_wenbenkuang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.init();
        this.viewInit();
        this.dataInit();

    }

    // 初始化必需数据
    private void init() {
        this.context = this;
        this.activity = this;
        ghlayer = new GraphicsLayer();
    }

    // 控件初始化
    private void viewInit() {
        // 地图控件
        mMapview = (MapView) findViewById(R.id.map);
        // 地图加载事件
        mMapview.setOnStatusChangedListener(new MapLoad(context, mMapview));

        // 放大按钮
        Button fangda = (Button) findViewById(R.id.fangda);
        fangda.setOnClickListener(this);

        // 缩小按钮
        Button suoxiao = (Button) findViewById(R.id.suoxiao);
        suoxiao.setOnClickListener(this);

        // 设置缩放监听
        tv_bilichi = (TextView) findViewById(R.id.dingwei_bilichi);

        // 设置定位事件
        btn_dingwei = (Button) findViewById(R.id.dingwei_loaction);
        btn_dingwei.setOnClickListener(this);

        // 图层控制
        FunctionBtn fb_tuceng = (FunctionBtn) findViewById(R.id.fb_tuceng);
        fb_tuceng.setOnClickListener(this);

        // 查询图层
        FunctionBtn fb_chaxunLayer = (FunctionBtn) findViewById(R.id.fb_chaxunlayer);
        ViewState.addView("图层查询", fb_chaxunLayer, R.drawable.biankuang_click9,
                R.drawable.fb_style, null);
        fb_chaxunLayer.setOnClickListener(this);
        ViewState.setViewOnclick("图层查询");

        // 影像对比
        FunctionBtn fb_duibi = (FunctionBtn) findViewById(R.id.fb_duibi);
        ViewState.addView("影像对比", fb_duibi, R.drawable.biankuang_click9,
                R.drawable.fb_style, null);
        fb_duibi.setOnClickListener(this);

        // 标记
        FunctionBtn fb_biaoji = (FunctionBtn) findViewById(R.id.fb_biaoji);
        ViewState.addView("轨迹", fb_biaoji, R.drawable.biankuang_click9,
                R.drawable.fb_style, null, true);
        fb_biaoji.setOnClickListener(this);

        // 清楚所有
        FunctionBtn fb_qingchu = (FunctionBtn) findViewById(R.id.fb_qingchu);
        // ViewState.addView("清除", fb_qingchu, R.drawable.biankuang_click9,
        // R.drawable.fb_style, null);
        fb_qingchu.setOnClickListener(this);

        // 专题查询
        FunctionBtn fb_zhauntichaxun = (FunctionBtn) findViewById(R.id.fb_zhuantichaxun);
        fb_zhauntichaxun.setOnClickListener(this);

        // 专题统计
        FunctionBtn fb_zhuantitongji = (FunctionBtn) findViewById(R.id.fb_zhuantitongji);
        fb_zhuantitongji.setOnClickListener(this);

        // 底部功能按钮

        // 全图
        ImageBtn ib_allimg = (ImageBtn) findViewById(R.id.ib_allimg);
        ib_allimg.setOnClickListener(this);

        // 测量
        ImageBtn ib_celiang = (ImageBtn) findViewById(R.id.ib_celiang);
        ViewState.addView("测量", ib_celiang, R.color.onclick,
                R.drawable.imagebtn_style, "测量");
        ib_celiang.setOnClickListener(this);

        // 上报
        ImageBtn ib_sahngbao = (ImageBtn) findViewById(R.id.ib_shangbao);
        ViewState.addView("上报", ib_sahngbao, R.color.onclick,
                R.drawable.imagebtn_style, "上报");
        ib_sahngbao.setOnClickListener(this);

        // 更多
        ImageBtn ib_more = (ImageBtn) findViewById(R.id.ib_gengduo);
        ib_more.setOnClickListener(this);

        // 搜索文本框
        ed_wenbenkuang = (EditText) findViewById(R.id.ed_sousuo_update);

        // 文本框之上的点击textView
        TextView tv_sosuo_tv_onclick = (TextView) findViewById(R.id.sosuo_tv_onclick);
        tv_sosuo_tv_onclick.setOnClickListener(this);

        // 导出数据
        FunctionBtn fb_shujudaochu = (FunctionBtn) findViewById(R.id.fb_daochu);
        fb_shujudaochu.setOnClickListener(this);

    }

    // 数据初始化
    private void dataInit() {

        LoadDialog.show(context, true, "地图加载中……");

        // 读取个人图层
        this.initLayer();

        // 比例尺监听
        mMapview.setOnZoomListener(new MyZoomListener(tv_bilichi, mMapview));

    }

    // 读取个人图层
    private void initLayer() {

        String url = MyAppliaction.AppUrl + "getLayer.ashx";

        RequestParams params = new RequestParams();
        params.addBodyParameter("userNum",
                UserSingle.getSingle().userModel.getUserNum());

        HttpUtils http = new HttpUtils();
        http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                layerResult(arg0.result);

            }

        });

    }

    // 图层查询结果
    private void layerResult(String result) {

        // 设置一个正常的底图，将其隐藏，获取到当前地图的最小比例，目前是最快速的解决方案
        Layer layer = new ArcGISTiledMapServiceLayer(
                "http://125.70.229.65/ArcGIS/rest/services/EMAP_ZW_CD_CD2012/MapServer");
        mMapview.addLayer(layer);
        layer.setVisible(false);

        try {
            String code = JsonUtils.getKey(result, "code");

            if (code == null || code.equals("")) {
                Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
                return;
            }
            // 成功
            if (code.equals("Success")) {
                String data = JsonUtils.getKey(result, "data");
                System.out.println(data);
                List<LayerModel> listLayer = MapJsonUtils.getLayers(data);

                // 放到全局字段
                listLayers = listLayer;

                // 加载查询事件
                mMapview.setOnTouchListener(new InitMapClick(context, mMapview));

            } else {
                String msg = JsonUtils.getKey(result, "msg");
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
        }
    }

    // 弹出图层选择框
    private void showListDialog() {
        if (viewList == null) {
            viewList = View.inflate(context, R.layout.dialog_layerlistview,
                    null);

            ListView list = (ListView) viewList
                    .findViewById(R.id.dia_layerlist);

            // 为listView绑定数据
            MyListLayerAdapter myadapter = new MyListLayerAdapter(context,
                    listLayers);
            list.setAdapter(myadapter);
        } else {
            ViewGroup viewgroup = (ViewGroup) viewList.getParent();
            viewgroup.removeAllViews();
        }

        DialogUtils.showDialog(context, viewList);
    }

    // 处理点击事件
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.suoxiao:

                MapSize.setZoomOut(context, mMapview);

                break;
            case R.id.fangda:

                MapSize.setZoomMin(context, mMapview);

                break;
            case R.id.fb_qingchu:
                final Callout callout = mMapview.getCallout();
                callout.hide();
                ghlayer.removeAll();
                ViewState.removeAllState();
                // 记录轨迹状态不变
                if (ViewState.getViewState("轨迹")) {
                    // 再绘制已经绘制的路径
                    MyLoactionListener.ghlayerId = MainActivity.ghlayer
                            .addGraphic(new Graphic(MyLoactionListener.poly,
                                    new SimpleLineSymbol(Color.RED, 2)));
                }

                mMapview.setOnTouchListener(new MapNullCick(context, mMapview));

                break;

            case R.id.fb_biaoji:
                if (ViewState.getViewState("轨迹")) {
                    ViewState.setViewUn("轨迹");
                    // 结束轨迹上报
                    btn_dingwei.setBackgroundResource(R.mipmap.loaction_img);
                    locatState = 0;
                    MyLoactionListener.openLocation(mMapview, context, locatState,
                            "GPSRrecord");

                } else {
                    ViewState.setViewOnclick("轨迹", false);
                    // 启动轨迹上报统计
                    btn_dingwei.setBackgroundResource(R.mipmap.dingwei_onclick);
                    locatState = 1;
                    MyLoactionListener.openLocation(mMapview, context, locatState,
                            "GPSRrecord");
                }

                break;

            case R.id.fb_tuceng:
                showListDialog();
                break;

            case R.id.fb_chaxunlayer:
                if (ViewState.getViewState("图层查询")) {
                    ViewState.setViewUn("图层查询");
                    mMapview.setOnTouchListener(new MapNullCick(context, mMapview));
                } else {
                    ViewState.setViewOnclick("图层查询");
                    mMapview.setOnTouchListener(new InitMapClick(context, mMapview));
                }

                break;

            case R.id.fb_duibi:

                Intent intent = new Intent(context, ImgContrastActivity.class);
                startActivity(intent);

                break;

            case R.id.dingwei_loaction:
                // 定位和非定位的状态不一样，所以这里应该有一个布尔值，表示当前点击的状态
                if (locatState == 0) {
                    view.setBackgroundResource(R.mipmap.dingwei_onclick);
                    locatState = 1;
                    MyLoactionListener.openLocation(mMapview, context, locatState,
                            "dingwei");
                } else if (locatState == 1) {
                    // 目前想到的办法是用定时器解决这个问题
                    view.setBackgroundResource(R.mipmap.fanwei);
                    locatState = 2;
                    MyLoactionListener.openLocation(mMapview, context, locatState, "fanwei");
                } else {
                    view.setBackgroundResource(R.mipmap.loaction_img);
                    locatState = 0;
                    MyLoactionListener.openLocation(mMapview, context, locatState, "yaunshi");
                }

                break;

            case R.id.ib_allimg:
                // 显示全图
                mMapview.setScale(mMapview.getMinScale());
                // 设置中心点
                mMapview.centerAt(30.683121, 104.079818, true);
                break;

            case R.id.ib_celiang:
                // 测量距离和面积
                if (ViewState.getViewState("测量")) {
                    ViewState.setViewUn("测量");
                    mMapview.setOnTouchListener(new MapNullCick(context, mMapview));
                } else {
                    MeasureDialog.openDialog(context, mMapview);
                }

                break;

            case R.id.ib_gengduo:

                MoreDilog.openDialog(activity, context);

                break;

            case R.id.ib_shangbao:
                selectDrawDialog.openDialog(activity, context, mMapview);
                break;

            case R.id.sosuo_tv_onclick:

                // 执行跳转，这里的值与搜索的值一一对应
                Intent query = new Intent(context, QueryActivity.class);

                Bundle b1 = new Bundle();
                b1.putSerializable("value", queryViewModel);
                query.putExtras(b1);
                startActivityForResult(query, 20);

                break;

            case R.id.fb_zhuantichaxun:

                Intent zhuangtichaxun = new Intent(context,
                        ZhuanTiChaXunActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("value", chaxunmodel);
                zhuangtichaxun.putExtras(b);
                startActivityForResult(zhuangtichaxun, 201);

                break;

            case R.id.fb_zhuantitongji:

                Intent zhaungtitongji = new Intent(context,
                        ZhuanTiTongJiActivity.class);
                startActivityForResult(zhaungtitongji, 200);

                break;

            case R.id.fb_daochu:

                Intent caijidaochu = new Intent(context,
                        CaiJiDianDaoChuActivity.class);
                Bundle b2 = new Bundle();
                b2.putSerializable("value", cajidaochu);
                caijidaochu.putExtras(b2);
                startActivityForResult(caijidaochu, 300);

                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println(requestCode + "   " + resultCode + "");

        // 调用相机返回值
        if (requestCode == 110) {
            if (resultCode == 120) {
                try {
                    ArrayList<String> listPhoto;
                    if (data != null) {
                        listPhoto = data.getStringArrayListExtra("listPhoto");
                    } else {
                        listPhoto = null;
                    }
                    savePointDialog.setListPhoto(listPhoto);
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }

        // 地图搜索返回值
        if (requestCode == 20) {
            if (resultCode == 21) {
                queryViewModel = (QueryViewModel) data
                        .getSerializableExtra("QueryViewModel");

            }
        }

        // 专题查询
        if (requestCode == 300) {
            // 显示采集点
            if (resultCode == 302) {

                cajidaochu = (CaiJiDianDaoChuStateModel) data
                        .getSerializableExtra("caijidaochu");

                mMapview.setOnTouchListener(new DrawShapeModelPoint(context,
                        mMapview, cajidaochu.getListModelSelect()));

            } else {
                cajidaochu = null;
            }
        }

        // 专题查询
        if (requestCode == 201) {
            // 显示采集点
            if (resultCode == 202) {
                chaxunmodel = (SerializableChaXunZhuanTi) data
                        .getSerializableExtra("chaxunjieguo");
                if (chaxunmodel != null) {

                    if (chaxunmodel.getModel().getXuanxiangka().equals("图斑")) {

                        // 结果值
                        FeatureSet result = chaxunmodel.getModel().getResult();

                        Point centerPoint = GraphicHelper.getExtent(
                                result.getGraphics()).getCenter();

                        // 设置地图的中心
                        mMapview.centerAt(centerPoint, true);
                        // 设置分辨率
                        mMapview.setScale(46182.92920838987);

                        // 绘制
                        mMapview.setOnTouchListener(new DrawGraphics(context,
                                mMapview, result.getGraphics()));

                        System.out.println("图斑事件");
                    } else {
                        // LoadDialog.show(context, true, "绘制中……");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                }
                                Message msg = new Message();
                                hdCaiJi.sendMessage(msg);

                            }
                        }).start();

                    }
                }

                System.out.println("执行绘制");

            }

        }

    }


    Handler hdCaiJi = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            mMapview.setOnTouchListener(new DrawShapeModelPoint(context,
                    mMapview, chaxunmodel.getModel().getListModel_caiji()));

            // LoadDialog.close();
        }
    };

    // 双击退出
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
