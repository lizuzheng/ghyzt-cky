package com.cky.ghyzt;

import java.util.ArrayList;
import com.cky.ghyzt.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cky.application.MyAppliaction;
import com.map.dialog.DialogSelectPhoto;
import com.map.dialog.savePointDialog;
import com.map.utils.BitmapUtils;
import com.map.utils.PhotoName;

public class AddPhotoActivity extends Activity implements OnClickListener {

	private Context context;
	private Button btn_select;
	private Activity activity;
	private GridView gv_photo;
	private ArrayList<String> listPhoto;
	public static String photoname = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_photo);

		this.baseInit();

		this.viewInit();

		this.dateInit();

	}

	private void baseInit() {

		this.context = this;
		this.activity = this;

		// 初始化标题栏
		Button fanhui = (Button) findViewById(R.id.title_bar_btn_left);
		fanhui.setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title_bar_title);
		title.setText("添加图片信息");
		Button queding = (Button) findViewById(R.id.title_btn_right);
		queding.setText("确定");
		queding.setOnClickListener(this);

	}

	private void viewInit() {

		btn_select = (Button) findViewById(R.id.btn_selectimg);
		btn_select.setOnClickListener(this);

		gv_photo = (GridView) findViewById(R.id.gv_photo);

		// int colnum = (int)
		// (((getResources().getDisplayMetrics().widthPixels)) / 300);
		// gv_photo.setNumColumns(colnum);

	}

	private void dateInit() {
		listPhoto = new ArrayList<String>();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.btn_selectimg:
			DialogSelectPhoto select = new DialogSelectPhoto(context, activity);
			select.show();
			break;
		case R.id.title_bar_btn_left:
			savePointDialog.setListPhoto(null);
			this.finish();
			break;

		case R.id.title_btn_right:
			// 返回到上一个页面
			Intent intent = new Intent();
			intent.putStringArrayListExtra("listPhoto", listPhoto);
			setResult(120, intent);
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println(requestCode + "  " + resultCode);



		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 119:

				System.out.println("相机返回");
				try {

					if (!MyAppliaction.PhotoName.equals("")
							&& !MyAppliaction.PhotoName.equals("null")) {
						listPhoto.add("//sdcard/CkyPoject/"
								+ MyAppliaction.PhotoName);

				

						gv_photo.setAdapter(new MyGView(context, listPhoto));
					}
				} catch (Exception e1) {
					System.out.println(e1);
				}

				break;
			// 相册
			case 180:
				if (data != null) {
					String picturePath;
					Uri selectedImage = data.getData();

					try {
						String[] filePathColumn = { MediaStore.Images.Media.DATA };
						Cursor cursor = getContentResolver()
								.query(selectedImage, filePathColumn, null,
										null, null);
						cursor.moveToFirst();
						int columnIndex = cursor
								.getColumnIndex(filePathColumn[0]);
						cursor.getString(columnIndex);
						picturePath = cursor.getString(columnIndex);

						cursor.close();

					} catch (Exception e) {
						picturePath = selectedImage.getPath();
					}

					System.out.println(picturePath);
					if (picturePath != null && !picturePath.equals("")) {
						listPhoto.add(picturePath);
					}
					gv_photo.setAdapter(new MyGView(context, listPhoto));
				}
				break;

			default:
				break;
			}
		}

	}

	class MyGView extends BaseAdapter {

		private Context context;
		private ArrayList<String> listPhoto;

		public MyGView(Context context, ArrayList<String> listPhoto) {
			this.context = context;
			this.listPhoto = listPhoto;
		}

		@Override
		public int getCount() {

			return listPhoto.size();
		}

		@Override
		public Object getItem(int arg0) {

			return null;
		}

		@Override
		public long getItemId(int arg0) {

			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			View view = View.inflate(context, R.layout.include_imageview, null);
			ImageView image = (ImageView) view.findViewById(R.id.iv_photo);
			Bitmap bitmap = BitmapUtils.getSmallBitmap2(listPhoto.get(arg0));
			image.setImageBitmap(bitmap);
			return view;
		}

	}

}
