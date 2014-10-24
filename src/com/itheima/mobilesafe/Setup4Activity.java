package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

/**
 * 	手机防盗设置向导
 * @author libo
 *
 */
public class Setup4Activity extends Activity {
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		sp = getSharedPreferences("config", MODE_PRIVATE);
	}
	
	public void next(View view)
	{
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		
		Intent it = new Intent(this,LostFindActivity.class);
		startActivity(it);
		finish();
	}
	
	public void pre(View view)
	{
		Intent it = new Intent(this,Setup3Activity.class);
		startActivity(it);
		finish();
	}
}
