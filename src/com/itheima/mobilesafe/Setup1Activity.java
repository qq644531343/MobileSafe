package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * 	手机防盗设置向导
 * @author libo
 *
 */
public class Setup1Activity extends BaseSetupActivity {
	
	protected static final String TAG = "Setup1Activity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}
	
	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void showNext() {
		Intent it = new Intent(this,Setup2Activity.class);
		startActivity(it);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	
}
