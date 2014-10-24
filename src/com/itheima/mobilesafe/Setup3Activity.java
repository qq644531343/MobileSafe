package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 手机防盗设置向导
 * 
 * @author libo
 * 
 */
public class Setup3Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		Intent it = new Intent(this, Setup4Activity.class);
		startActivity(it);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		Intent it = new Intent(this, Setup2Activity.class);
		startActivity(it);
		finish();
		overridePendingTransition(R.anim.tran_pre_in,
				R.anim.tran_pre_out);
	}
}
