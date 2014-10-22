package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima.mobilesafe.ui.SettingItemView;

public class SettingActivity extends Activity {
	
	 private SettingItemView siv_update;
	 private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		siv_update = (SettingItemView)findViewById(R.id.siv_update);
		
		boolean update = sp.getBoolean("update", false);
		siv_update.setChecked(update);
		
		siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();
				//判断是否选中
				if (siv_update.isChecked()) {//已打开自动升级
					siv_update.setChecked(!siv_update.isChecked());
					editor.putBoolean("update", false);
				}else {//没打开自动升级
					siv_update.setChecked(!siv_update.isChecked());
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
		
	}
	
}
