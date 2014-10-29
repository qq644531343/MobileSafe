package com.itheima.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 手机防盗设置向导
 * 
 * @author libo
 * 
 */
public class Setup3Activity extends BaseSetupActivity {
	
	private EditText et_setup3_phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		
		et_setup3_phone = (EditText)findViewById(R.id.et_setup3_phone);
		sp.getString("safenumber", "");
		et_setup3_phone.setText(sp.getString("safenumber", ""));
	}
	
	/**
	 * 选择联系人
	 * @param view
	 */ 
	public void selectContact(View view) {
		Intent it = new Intent(Setup3Activity.this,SelectContactActivity.class);
		startActivityForResult(it, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		String phone = data.getStringExtra("phone").replace("-", "").replace(" ", "");
		et_setup3_phone.setText(phone);
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		
		String phone = et_setup3_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "安全号码尚未设置", 0).show();
			return;
		}
		//保存安全号码
		Editor editor = sp.edit();
		editor.putString("safenumber", phone);
		editor.commit();
		
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
