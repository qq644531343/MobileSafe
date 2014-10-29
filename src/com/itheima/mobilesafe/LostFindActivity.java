package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 手机防盗页
 * 
 * @author libo
 * 
 */
public class LostFindActivity extends Activity {

	private SharedPreferences sp;
	private TextView tv_safenumber;
	private ImageView iv_protecting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 判断是否已引导操作
		boolean configed = sp.getBoolean("configed", false);
		if (configed) {// 已引导
			setContentView(R.layout.activity_lost_find);
			tv_safenumber = (TextView)findViewById(R.id.tv_safenumber);
			iv_protecting = (ImageView)findViewById(R.id.iv_protecting);
			
			String phone = sp.getString("safenumber", "");
			System.out.println("phone:" + phone);
			tv_safenumber.setText(phone);
			boolean protecting = sp.getBoolean("protecting", false);
			if (protecting) {
				iv_protecting.setImageResource(R.drawable.lock);
			}else {
				iv_protecting.setImageResource(R.drawable.unlock);
			}
			
		} else {// 跳转设置向导页
			Intent it = new Intent(LostFindActivity.this,
					Setup1Activity.class);
			startActivity(it);
			// 关闭当前页
			finish();
		}

	}

	public void reEnterSetup(View view) {
		Intent it = new Intent(LostFindActivity.this,
				Setup1Activity.class);
		startActivity(it);
		// 关闭当前页
		finish();
	}
}
