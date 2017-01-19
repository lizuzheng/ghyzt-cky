package com.mapview.click;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cky.ghyzt.R;
import com.cotr.cdkc.Cotr;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.LocationDisplayManager.AutoPanMode;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

import java.util.Timer;
import java.util.TimerTask;

public class MyLoactionListener implements LocationListener {

    private static LocationDisplayManager ls;
    private static Context context_location;
    private static String type_location;

    private static MapView mMapview;
    public static Polyline poly;
    public static int ghlayerId = -1;
    private static boolean isFirst = true;

    private static double locy;
    private static double locx;
    private static Timer timer1;
    private int count = 0;
    private static GraphicsLayer weizhiGL;
    private int lastId = -1;


    public MyLoactionListener(MapView mMapview) {
        this.mMapview = mMapview;

    }

    @Override
    public void onLocationChanged(Location location) {
            // 1、获取到当前为定位点
            locy = location.getLatitude();
            locx = location.getLongitude();

        Point point = new Point(locx,locy);

        Point mapPoint = (Point) GeometryEngine.project(point,
                SpatialReference.create(4326),
                mMapview.getSpatialReference());

           if (isFirst&&type_location.equals("dingwei")) {
               isFirst=false;
               mMapview.centerAt(mapPoint, true);
           }


    }

    // 判断是不是在屏幕中间，不在中间就执行操作
    private static void queryCenter() {

        Point point = new Point( locx,locy);
        Point mapPoint = (Point) GeometryEngine.project(point,
                SpatialReference.create(4326),
                mMapview.getSpatialReference());

        Envelope rExtent = new Envelope();
        mMapview.getExtent().queryEnvelope(rExtent);
        double leftB_x = rExtent.getXMin();
        double leftB_y = rExtent.getYMin();
        double topR_x = rExtent.getXMax();
        double topR_y = rExtent.getYMax();

        // 设置范围
        rExtent.setXMin(leftB_x + 100);
        rExtent.setXMax(topR_x - 100);
        rExtent.setYMax(topR_y - 100);
        rExtent.setYMin(leftB_y + 100);

        // 判断当前点是否在屏幕范围内
        if (!rExtent.contains(mapPoint)) {
            mMapview.centerAt(mapPoint, true);
        }
    }

    //定时器回调
    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                try {
                    queryCenter();
                } catch (Exception ex) {
                }

            }
            super.handleMessage(msg);
        }

        ;
    };

    @Override
    public void onProviderDisabled(String status) {

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void onStatusChanged(String arg0, int status, Bundle arg2) {
        switch (status) {
            // GPS状态为可见时
            case LocationProvider.AVAILABLE:
                Log.i("", "当前GPS状态为可见状态");
                break;
            // GPS状态为服务区外时
            case LocationProvider.OUT_OF_SERVICE:
                Log.i("", "当前GPS状态为服务区外状态");
                break;
            // GPS状态为暂停服务时
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.i("", "当前GPS状态为暂停服务状态");
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.i("", "第一次定位");
                break;
        }

        System.out.println("定位测试状态改变");

    }

    // 对定位进行配置
    public static void openLocation(MapView mMapView, Context context,
                                    int state, String type) {
        context_location = context;
        isFirst = true;
        AutoPanMode autoPanMode = AutoPanMode.LOCATION;
        type_location = type;

        ls = mMapView.getLocationDisplayManager();


        //定位和跟随状态的操作
        if (state == 1 || state == 2) {

            ls.setAllowNetworkLocation(true);
            ls.setShowPings(true);
            ls.setShowLocation(true);
            ls.setLocationListener(new MyLoactionListener(mMapView));
            ls.setAutoPanMode(autoPanMode);
            ls.start();
            Toast.makeText(context, "定位中……", Toast.LENGTH_LONG).show();
            System.out.println("定位中……");

        }

        //取消定位的操作
        if (state == 0) {
            try {
                System.out.println("停止");
                //mMapView.removeLayer(weizhiGL);
                timer1.cancel();
                timer1 = null;
                weizhiGL=null;
                isFirst=true;
            } catch (Exception e) {

            }
            poly = null;
            ls.stop();
            Toast.makeText(context, "取消定位", Toast.LENGTH_LONG).show();
            System.out.println("定位结束");

        }

        if (type_location.equals("fanwei")) {
            if (timer1 == null) {
                try {
                    timer1 = new Timer();
                    timer1.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            // 需要做的事:发送消息
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }, 0, 1000); // 1s后执行task,经过1s再次执行
                } catch (Exception e) {

                    System.out.println("执行出错");
                    System.out.println(e.toString());
                }
            }
        }
    }
}
