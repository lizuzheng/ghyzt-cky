package com.cky.view;

import com.cky.ghyzt.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageBtn extends LinearLayout {

	private ImageView imageView;
	private TextView textView;
	private LinearLayout lay;

	public ImageBtn(Context context) {
		super(context);

	}

	public ImageBtn(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.MyView);

		// 文字
		CharSequence text = a.getText(R.styleable.MyView_text);
		if (text == null) {
			text = "";
		}
		// 获取图片img
		Drawable img = a.getDrawable(R.styleable.MyView_src);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_buttom_function, this);
		imageView = (ImageView) findViewById(R.id.imgbtn_img);
		textView = (TextView) findViewById(R.id.imgbtn_text);
		
		 lay=(LinearLayout) findViewById(R.id.imgbtn_lay);
	     lay.setBackgroundResource(R.drawable.imagebtn_style);
		
		imageView.setBackgroundDrawable(img);
		textView.setText(text);
		
	}

	public void setBackgroundResource(int id)
	{
		lay.setBackgroundResource(id);
	}
	
	/**
	 * 设置图片资源
	 */
	public void setImageResource(int resId) {
		imageView.setImageResource(resId);
	}

	/**
	 * 设置显示的文字
	 */
	public void setTextViewText(String text) {
		textView.setText(text);
	}

	
	
}
