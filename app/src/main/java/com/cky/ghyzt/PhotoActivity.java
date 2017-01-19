package com.cky.ghyzt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.jztx.utils.HttpUtils;
import com.map.dialog.DialogUtils;
import com.map.dialog.LoadDialog;
import com.map.utils.BitmapUtils;

import com.map.utils.ImageOnTouch;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import com.cky.ghyzt.R;
/**
 * 这是一个公有的，每个程序都会用的activity，主要用于显示一连串的图片信息
 *  参数：
 *  
 *   1、title 表示显示什么样的信息，titie
 *   2、urlMinList 保存的是传输过来的图片网址信息 (缩略图)
 *   3、urlMaxList 大图
 * 
 * @author Administrator
 *
 */
public class PhotoActivity extends Activity implements Runnable,OnClickListener{

	private TextView tv_titlename;
	private String Title;
	private GridView grid_photo;
	private Context context;
    private int selectId=0;
   
	private ImageView imageview;
	private  ArrayList<String> urlMaxList;
	private View selectView;
	private ArrayList<String> urlMinList;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);

		
		
		//初始化必要的参数信息
		this.baseinit();
		//初始化控件
		this.viewInit();
		//初始化数据
		this.dataInit();

	}

	/**
	 * 初始化必要的参数信息
	 */
	private void baseinit() {
		
		context = this;
		Bundle extras = getIntent().getExtras();
		Title = extras.getString("title");
		urlMinList=extras.getStringArrayList("urlMinList");
		urlMaxList=extras.getStringArrayList("urlMaxList");
		
	}

	private void dataInit() {
		
		
		
		//设置标题
		tv_titlename.setText("查看" + Title + "图片");

		if(urlMinList==null||urlMaxList==null)
			return;
		// 设置图片的基本显示信息
		this.imageBase();
		grid_photo.setAdapter(new ImageAdapter());
		grid_photo.setOnItemClickListener(new MyOnItem());	
		//设置默认
		showMaxImg(0);
	}

	private void imageBase() {
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.mipmap.ic_stub)
				.showImageForEmptyUri(R.mipmap.ic_empty)
				.showImageOnFail(R.mipmap.ic_error).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
				.build();

	}

	private void viewInit() {
		tv_titlename = (TextView) findViewById(R.id.tv_titlename);
		grid_photo = (GridView) findViewById(R.id.grid_photo);
		imageview = (ImageView) findViewById(R.id.iv_showphoto);
		imageview.setOnClickListener(this);

	}

	class MyOnItem implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			if (selectView != null)
				selectView.setBackgroundColor(Color.WHITE);

			selectView = view;
			view.setBackgroundResource(R.color.blue);		
			showMaxImg(arg2);
		}
	}
	
    private void showMaxImg(int arg2) {
    	LoadDialog.show(context, false, "读取图片中……请等待！");    	
    	
    	 selectId=arg2;
		 Thread t = new Thread(PhotoActivity.this);
		 t.start();
	}
		
	class ImageAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text;
			public ImageView image;
		}
		@Override
		public int getCount() {
			return urlMinList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.showphoto_layer,
						parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.tv_textid);
				holder.image = (ImageView) view.findViewById(R.id.iv_photoshow);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}

			holder.text.setText("" + (position + 1)); // TextView设置文本

			/**
			 * 显示图片 参数1：图片url 参数2：显示图片的控件 参数3：显示图片的设置 参数4：监听器
			 */
			imageLoader.displayImage(urlMinList.get(position), holder.image, options,
					animateFirstListener);

			return view;

		}
	}
	
	/**
	 * 图片加载第一次显示监听器
	 * @author Administrator
	 *
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		finish();

		return super.onKeyDown(keyCode, event);
	}

	Handler hd=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			LoadDialog.close();	
			Bitmap bitmap=(Bitmap) msg.obj;
			imageview.setImageBitmap(bitmap);
			
			
			//将控件设置为缩放
			setScale();
			
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	public void run() {
		
		Message msg=new Message();
		
		Bitmap bitmap = null;
		try {
			Bitmap bit = HttpUtils.getBitmap(urlMaxList.get(selectId));
			//Bitmap bit=null;
			bitmap=BitmapUtils.compressBitmap(bit, 400);		
		} catch (Exception e) {
			
		    try {
				bitmap=BitmapUtils.getSmallBitmap2(urlMaxList.get(selectId));
			} catch (Exception e1) {
				
			}
		}	
		
		msg.obj=bitmap;
		hd.sendMessage(msg);
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.iv_showphoto:
			setScale();
			break;

		default:
			break;
		}
	}

	
	
	private void setScale()
	{
		ScaleType scaleType = ScaleType.MATRIX;
		imageview.setScaleType(scaleType);
		imageview.setOnTouchListener(new ImageOnTouch(imageview));
	}
}
