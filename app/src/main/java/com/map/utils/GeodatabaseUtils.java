package com.map.utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.cky.ghyzt.MainActivity;
import com.cky.model.ShapeModel;
import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.renderer.Renderer;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.SymbolHelper;
import com.esri.core.table.TableException;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 将点线面保存成为arcmap识别的格式
 * @author Administrator
 *
 */
public class GeodatabaseUtils {
	
	private Context context;
	private List<ShapeModel> listModel;
	private List layerList;

	public GeodatabaseUtils(Context context,List<ShapeModel> listModel)
	{
		this.context=context;
		this.listModel=listModel;
	}
	
	
	/**
     * 读取Geodatabase中离线地图信息
     * @param geodatabsePath 离线Geodatabase文件路径
     */
    private void addFeatureLayer(String geodatabsePath) {

        Geodatabase localGdb = null;
        try {
            localGdb = new Geodatabase(geodatabsePath,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        layerList = new ArrayList<>();
        // 添加FeatureLayer到MapView中
        if (localGdb != null) {
            for (GeodatabaseFeatureTable gdbFeatureTable : localGdb.getGeodatabaseTables()) {
                if (gdbFeatureTable.hasGeometry()){
                    FeatureLayer layer = new FeatureLayer(gdbFeatureTable);
                    MainActivity.mMapview.addLayer(layer);
                }
            }
        }
    }
    
    
    /**
     * 添加要素绘制样式模板
     * 获取图层要素模板并添加到featureTempleteView
     * @param layer
     */
    private void addFeatureTemplate(FeatureLayer layer) {
        List<FeatureTemplate> featureTemp = ((GeodatabaseFeatureTable) layer.getFeatureTable()).getFeatureTemplates();
        for (FeatureTemplate featureTemplate : featureTemp) {
            GeodatabaseFeature g = null;
            try {
                g = ((GeodatabaseFeatureTable) layer.getFeatureTable()).createFeatureWithTemplate(featureTemplate, null);
                Renderer renderer = layer.getRenderer();
                Symbol symbol = renderer.getSymbol(g);
                float scale = context.getResources().getDisplayMetrics().density;
                int widthInPixels = (int) (35 * scale + 0.5f);
                Bitmap bitmap = SymbolHelper.getLegendImage(symbol, widthInPixels, widthInPixels);

                //将要素样式模板赋值给imgButton
//                ImageButton imageButton = new ImageButton(context);
//                imageButton.setImageBitmap(bitmap);
//                imageButton.setTag(layer);//保存当前待编辑图层
//                ((LinearLayout)featureTempleteView).addView(imageButton);//添加到要素样式模板列表
//                imageButton.setOnClickListener(new ImageButtonOnClickListener());
            } catch (TableException e) {
//                e.printStackTrace();
//                Toast.makeText(MainActivity.this, "Error：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    
    
    
}















