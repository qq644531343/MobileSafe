package com.itheima.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 手机防盗设置向导
 * 
 * @author libo
 * 
 */
public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cb_protecting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
	
		cb_protecting = (CheckBox)findViewById(R.id.cb_protecting);
		boolean isChecked = sp.getBoolean("protecting", false);
		cb_protecting.setChecked(isChecked);
		setCheckBoxStatus(isChecked);
		cb_protecting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				setCheckBoxStatus(isChecked);
			}
		});
		
	}
	
	private void setCheckBoxStatus(boolean isChecked)
	{
		if (isChecked) {
			cb_protecting.setText("您已经开启防盗");
		}else {
			cb_protecting.setText("您没有开启防盗");
		}
		//保存选择的状态
		Editor editor = sp.edit();
		editor.putBoolean("protecting", isChecked);
		editor.commit();	
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();

		Intent it = new Intent(this, LostFindActivity.class);
		startActivity(it);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		Intent it = new Intent(this, Setup3Activity.class);
		startActivity(it);
		finish();
		overridePendingTransition(R.anim.tran_pre_in,
				R.anim.tran_pre_out);
	}
}
