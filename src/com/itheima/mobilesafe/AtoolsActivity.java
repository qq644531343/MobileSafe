package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.SMSUtils;

import android.app.Activity;
import android.app.ProgressDialog;
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
	
	private ProgressDialog pd;
	
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
		
		pd = new ProgressDialog(this);
		pd.setMessage("正在备份短信");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		
		new Thread(){
			@Override
			public void run() {
				try {
					SMSUtils.backupSMS(AtoolsActivity.this, new SMSUtils.BackUpCallBack() {
						
						@Override
						public void onSmsBackup(int progress) {
							pd.setProgress(progress);
						}
						
						@Override
						public void beforeBackup(int max) {
							pd.setMax(max);
						}
					});
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this, "备份短信成功", 1).show();
							
						}
					});
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this, "备份短信失败", 1).show();
							
						}
					});
					e.printStackTrace();
				}finally {
					pd.dismiss();
				}
	
			}
		}.start();
	}
	
	/**
	 * 短信还原
	 */
	public void smsRestore(View view) {
		try {
			SMSUtils.restoreSMS(AtoolsActivity.this, false);
			Toast.makeText(AtoolsActivity.this, "短信已恢复", 1).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
