package com.map.dialog;

import java.io.File;

import com.cky.application.MyAppliaction;
import com.cky.ghyzt.R;
import com.cky.ghyzt.AddPhotoActivity;
import com.jztx.utils.FileUtils;
import com.map.utils.PhotoName;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 弹出选择图片的方式选择框
 * @author Administrator
 *
 */
public class DialogSelectPhoto implements OnClickListener{

	private Context context;
	private Activity activity;

	public DialogSelectPhoto(Context context, Activity activity)
	{
		this.context=context;
		this.activity=activity;
	}
	
	public void show()
	{
	    View view=View.inflate(context, R.layout.dialog_selectphotoway, null);
	    //拍照
        Button btn_paizhao=(Button) view.findViewById(R.id.btn_select_paizhao);
        btn_paizhao.setOnClickListener(this);	
        
        //相册
        Button btn_xiangce=(Button) view.findViewById(R.id.btn_select_tuku);
        btn_xiangce.setOnClickListener(this);
        
		DialogUtils.showDialog(context, view);
	}

	@Override
	public void onClick(View view) {
		DialogUtils.closeDialog();
		switch (view.getId()) {
		//拍照
		case R.id.btn_select_paizhao:
			this.selectCamer();
			break;
		case R.id.btn_select_tuku:
			this.selectTuku();
			break;
			
			
		default:
			break;
		}
		
	}

	/**
	 * 打开图库
	 */
	private void selectTuku() {
		
		Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		activity.startActivityForResult(intent, 180);
	}

	/**
	 * 拍照
	 */
	private void selectCamer() {
		
		// 调用系统的拍照功能
		Intent intent = new Intent(
				MediaStore.ACTION_IMAGE_CAPTURE);


		//创建文件夹
		String path="/sdcard/CkyPoject/";
		File dir=new File(path);
		if(!dir.exists())
		{
			dir.mkdirs();
		}

		File tempFile=FileUtils.getFile(path,MyAppliaction.getPhotoName());
		//File tempFile = new File("/sdcard/CkyPoject/", PhotoName.getPhotoName());
		// 指定调用相机拍照后照片的储存路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
		activity.startActivityForResult(intent,119);
	}	
}










