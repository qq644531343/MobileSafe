package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.SMSUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
	
	/**
	 * 	短信备份
	 */
	public void smsBackup(View view) {
		try {
			SMSUtils.backupSMS(this);
			Toast.makeText(this, "备份短信成功", 1).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "备份短信失败", 1).show();
			e.printStackTrace();
		}
	}
	
	/**
	 * 短信还原
	 */
	public void smsRestore(View view) {
		
	}
}
