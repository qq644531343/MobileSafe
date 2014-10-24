package com.itheima.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima.mobilesafe.ui.SettingItemView;

/**
 * 手机防盗设置向导
 * 
 * @author libo
 * 
 */
public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView siv_setup2_sim;
	// 读取手机sim卡信息
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		siv_setup2_sim = (SettingItemView) findViewById(R.id.siv_setup2_sim);
		
		String simString = sp.getString("sim", null);
		boolean stored = false;
		if (!TextUtils.isEmpty(simString) ) {
			stored = true;
		}
		siv_setup2_sim.setChecked(stored);
		
		siv_setup2_sim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				siv_setup2_sim.setChecked(!siv_setup2_sim
						.isChecked());

				Editor editor = sp.edit();
				if (siv_setup2_sim.isChecked()) {
					// 保存sim卡的序列号
					String sim = tm.getSimSerialNumber();
					editor.putString("sim", sim);
				} else {
					editor.putString("sim", null);
				}
				editor.commit();

			}
		});
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		Intent it = new Intent(this, Setup3Activity.class);
		startActivity(it);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		Intent it = new Intent(this, Setup1Activity.class);
		startActivity(it);
		finish();
		overridePendingTransition(R.anim.tran_pre_in,
				R.anim.tran_pre_out);
	}
}
