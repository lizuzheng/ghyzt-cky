package com.mapview.click;

import android.content.Context;

import android.widget.Toast;

import com.cky.ghyzt.MainActivity;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.map.dialog.LoadDialog;


/**
 * 地图加载事件
 * @author doudou
 *
 */
public class MapLoad implements OnStatusChangedListener{

	private Context context;
	private MapView mMapview;

	
	public MapLoad(Context context,MapView mMapview)
	{
		this.context=context;
		this.mMapview=mMapview;

		
	}
	
	
	@Override
	public void onStatusChanged(Object source, STATUS status) {
		
		if(status == STATUS.INITIALIZED){   
			
			for(int i=0;i<MainActivity.listLayers.size();i++)
			{
				MainActivity.listLayers.get(i).getLayer().setMinScale(mMapview.getMinScale());
				mMapview.addLayer(MainActivity.listLayers.get(i).getLayer());
			}
					
			//关闭对话框
			LoadDialog.close();
			
        	Toast.makeText(context, "地图加载成功！", 1).show();
        	
        	// 设置中心点
        	Point point=new Point();
        	//point.setXY(220438.88893056306, 193262.98718842116);
  
        	//mMapview.centerAt(point, true);
        	// 设置分辨率
        	//mMapview.setScale(364012.5752793211);
        	
        	//System.out.println(mMapview.getMinScale()+"sfcsadsadsadas");
			
        	
        	
        	MapSize.countBilichi(mMapview, MainActivity.tv_bilichi);
        	
        	//为地图添加一个gh图层，这样之后的所有绘制事件都可依据这个图层进行绘制
        	mMapview.addLayer(MainActivity.ghlayer);
        	
        }else if(status == STATUS.LAYER_LOADED){                      
        }else if((status == STATUS.INITIALIZATION_FAILED)){               
        }else if((status == STATUS.LAYER_LOADING_FAILED)){                
        }  
	}

}



















