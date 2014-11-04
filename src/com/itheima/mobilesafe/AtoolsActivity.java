package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 	高级工具
 * @author libo
 *
 */
public class AtoolsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	/**
	 * 	查询归属地
	 * @param view
	 */
	public void numberQuery(View view)
	{
		Intent it = new Intent(this, NumberAddressQueryActivity.class);
		startActivity(it);
	}
	
}
