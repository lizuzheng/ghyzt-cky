package com.mapview.click;

import java.util.List;

import com.cky.ghyzt.R;
import com.cky.ghyzt.MainActivity;
import com.cky.model.LayerModel;
import com.esri.android.map.Layer;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


/**
 * 自定义适配器
 * @author doudou
 *
 */
public class MyListLayerAdapter extends BaseAdapter {

	private Context context;
	private List<LayerModel> layers;


		
	public MyListLayerAdapter(Context context, List<LayerModel> layers) {
		this.context=context;
		this.layers=layers;
		
	}

	public int getCount() {

		return layers.size();
	}

	public Object getItem(int arg0) {

		return null;
	}

	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		View view = View.inflate(context,
				R.layout.select_layout, null);
		final CheckBox cb = (CheckBox) view.findViewById(R.id.map_checkbox);
		
	    cb.setChecked(layers.get(arg0).getLayer().isVisible());
		
		cb.setOnClickListener(new Map_check(layers.get(arg0).getLayer()));
				
		final SeekBar SB = (SeekBar) view.findViewById(R.id.map_seekbar);
		SB.setProgress((int)(layers.get(arg0).getLayer().getOpacity()*10));
		SB.setOnSeekBarChangeListener(new MapSeekBar(layers.get(arg0).getLayer()));
		cb.setText(layers.get(arg0).getLayer().getName());
		return view;
	}
}

/**
 * 选择显示的图层
 * @author doudou
 *
 */
class Map_check implements OnClickListener {


	private Layer layer;


	public Map_check(Layer layer) {
		this.layer=layer;
	}

	public void onClick(View arg0) {
		final CheckBox cb = (CheckBox) arg0;

		Boolean cbIscheck = cb.isChecked();
			
		layer.setVisible(cbIscheck);
		
		
		
	}
}

/**
 * 图层的透明度调节
 * @author doudou
 *
 */
class MapSeekBar implements OnSeekBarChangeListener {

    private Layer layer;
	


	public MapSeekBar(Layer layer) {
		this.layer=layer;
	}

	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

		float mapOpcity = (float) (arg0.getProgress() / 10.0);			
		layer.setOpacity(mapOpcity);
		
		
	}

	public void onStartTrackingTouch(SeekBar arg0) {
	}

	public void onStopTrackingTouch(SeekBar arg0) {
		Log.i("SeekBar", arg0.getProgress() + "");

	}
}
