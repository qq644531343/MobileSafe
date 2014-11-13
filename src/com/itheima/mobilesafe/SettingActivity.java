package com.itheima.mobilesafe;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;

import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.ui.SettingClickView;
import com.itheima.mobilesafe.ui.SettingItemView;
import com.itheima.mobilesafe.utils.ServiceUtils;

public class SettingActivity extends Activity {

	// 设置是否自动更新
	private SettingItemView siv_update;
	// 设置是否开启来电归属地显示
	private SettingItemView siv_show_address;
	private Intent showAddressIntent;
	
	//设置归属地显示背景
	private SettingClickView scv_changebg;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		//设置是否自动升级
		siv_update = (SettingItemView) findViewById(R.id.siv_update);

		boolean update = sp.getBoolean("update", false);
		siv_update.setChecked(update);

		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();
				// 判断是否选中
				if (siv_update.isChecked()) {// 已打开自动升级
					siv_update.setChecked(!siv_update
							.isChecked());
					editor.putBoolean("update", false);
				} else {// 没打开自动升级
					siv_update.setChecked(!siv_update
							.isChecked());
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
		
		//设置是否开启来电归属地显示
		siv_show_address = (SettingItemView)findViewById(R.id.siv_show_address);
		showAddressIntent = new Intent(this, AddressService.class);
		boolean isRunning = ServiceUtils.isServiceRunning(this, "com.itheima.mobilesafe.service.AddressService");
		siv_show_address.setChecked(isRunning);
		siv_show_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//监听来电显示的服务已开启
				if (siv_show_address.isChecked()) {
					//关闭
					stopService(showAddressIntent);
					siv_show_address.setChecked(false);
				}else {
					//开启
					startService(showAddressIntent);
					siv_show_address.setChecked(true);
				}
				
			}
		});
		
		//归属地背景
		scv_changebg = (SettingClickView)findViewById(R.id.scv_changebg);
		final String[] items = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
		int which = sp.getInt("which", 0);
		scv_changebg.setDesc(items[which]);
		scv_changebg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//弹出对话框
				AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
				builder.setTitle("归属地提示框风格");
				int dd = sp.getInt("which", 0);
				scv_changebg.setDesc(items[dd]);
				builder.setSingleChoiceItems(items, dd , new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//保存选择的样式
						Editor editor = sp.edit();
						editor.putInt("which", which);
						editor.commit();
						scv_changebg.setDesc(items[which]);
						
						//取消对话框
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("cancel", null);
				builder.show();
				
			}
		});

	}

}
