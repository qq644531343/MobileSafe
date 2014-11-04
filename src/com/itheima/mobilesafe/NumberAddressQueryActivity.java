package com.itheima.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 	号码查询
 * @author libo
 *
 */
public class NumberAddressQueryActivity extends Activity {
	
	private static final String TAG = "NumberAddressQueryActivity";
	private EditText et_phone;
	private TextView result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address_query);
		
		et_phone = (EditText)findViewById(R.id.et_phone);
		result = (TextView)findViewById(R.id.result);
	}
	
	/**
	 * 	查询归属地
	 * @param view
	 */
	public void numberAddressQuery(View view)
	{
		String phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "号码为空", 0).show();
			return;
		}else {
			//查询数据库
			Log.i(TAG, "要查询的号码:" + phone);
		}
	}
}
