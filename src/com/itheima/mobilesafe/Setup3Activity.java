package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 	手机防盗设置向导
 * @author libo
 *
 */
public class Setup3Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	
	public void next(View view)
	{
		Intent it = new Intent(this,Setup4Activity.class);
		startActivity(it);
		finish();
	}
	
	public void pre(View view)
	{
		Intent it = new Intent(this,Setup2Activity.class);
		startActivity(it);
		finish();
	}
}
