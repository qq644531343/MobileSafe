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
public class Setup2Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
	}
	
	public void next(View view)
	{
		Intent it = new Intent(this,Setup3Activity.class);
		startActivity(it);
		finish();
	}
	
	public void pre(View view)
	{
		Intent it = new Intent(this,Setup1Activity.class);
		startActivity(it);
		finish();
	}
}
