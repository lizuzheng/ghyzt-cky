package com.map.dialog;

import com.cky.ghyzt.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;

import com.cky.model.UserModel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;

public class Dialog_updatePass {

	private static final int style = R.style.dialog;
	private static Dialog d=null;
	private static Context context;
	
	public static void openDialog(final Context context1)
	{
		context=context1;
		
		View view=View.inflate(context1, R.layout.dia_passupdate, null);
		d = new Dialog(context1, style);		
		d.setContentView(view);	
		d.setCanceledOnTouchOutside(true);
		d.show();
		
		
		
		final EditText up_pass_old=(EditText) view.findViewById(R.id.up_pass_old);
		final EditText up_pass_new=(EditText) view.findViewById(R.id.up_pass_new);
		final EditText up_pass_new2=(EditText) view.findViewById(R.id.up_pass_new2);
        Button btn_xiugai=(Button) view.findViewById(R.id.btn_xiugai);
        btn_xiugai.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

                  String old=up_pass_old.getText().toString();
                  String new1=up_pass_new.getText().toString();
                  String new2=up_pass_new2.getText().toString();
                  
                  if(old.equals("")||new1.equals("")||new2.equals(""))
                  {
                	  Toast.makeText(context1, "请填写完整后继续", Toast.LENGTH_LONG).show();
                  }
                  else if(!new1.equals(new2))
                  {
                	  Toast.makeText(context1, "两次密码输入不一致", Toast.LENGTH_LONG).show();
                  }
                  else if(old.equals(UserSingle.getSingle().userModel.getUserPassword()))
                  {
                	  LoadDialog.show(context1, true, "修改中……");
                	  updatePass(new1);
                  }
                  else
                  {
                	  Toast.makeText(context1, "旧密码输入不正确", Toast.LENGTH_LONG).show();
                  }
				
			}
		});
        
        
        
	}

	protected static void updatePass(final String new1) {
		
		String Url=MyAppliaction.AppUrl+"upPassWord.ashx";
		RequestParams params = new RequestParams();
		params.addBodyParameter("UserNum",UserSingle.getSingle().userModel.getUserNum());
		params.addBodyParameter("newPassword",new1);

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, Url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LoadDialog.close();
				 Toast.makeText(context, "修改失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LoadDialog.close();
				updateResult(arg0.result,new1);
				
			}
			
		});
		
	}

	protected static void updateResult(String result,String newPass) {
		try {
			String code = JsonUtils.getKey(result, "code");
					
			if (code == null || code.equals("")) {
				Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
				return;
			}
			// 成功
			if (code.equals("Success")) {

				Toast.makeText(context, "修改成功", Toast.LENGTH_LONG).show();
				UserSingle.getSingle().userModel.setUserPassword(newPass);

			} else {
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}
	}
}















