package com.itheima.mobilesafe.service;

import com.itheima.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class AddressService extends Service {
	
	//监听来电
	private TelephonyManager tm;
	private MyPhoneStateListener listener;
	
	private class MyPhoneStateListener extends PhoneStateListener
	{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			//state:状态 incomingNumber:电话号码
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://铃声想起/来电
				//根据得到的电话号码查询归属地,并toast
				String address = NumberAddressQueryUtils.querNumber(incomingNumber);
				Toast.makeText(getApplicationContext(), address, 1).show();
				break;

			default:
				break;
			}
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		
		//监听来电
		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//取消监听
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);	
		listener = null;
	}

}
