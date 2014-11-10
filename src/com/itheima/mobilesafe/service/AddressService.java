package com.itheima.mobilesafe.service;

import com.itheima.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AddressService extends Service {
	
	//监听来电
	private TelephonyManager tm;
	private MyPhoneStateListener listener;
	
	private OutCallReceiver receiver;
	
	//窗体管理者
	private WindowManager wm;
	
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
//				Toast.makeText(getApplicationContext(), address, 1).show();
				myToast(address);
				break;

			default:
				break;
			}
		}
		
	}

	//打电话广播
	private class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//得到要打出去的电话号码
			String phone  = getResultData();
			String address = NumberAddressQueryUtils.querNumber(phone);
//			Toast.makeText(context, address, 1).show();
			myToast(address);
		}

	}
	
	public void myToast(String address) {
		// TODO Auto-generated method stub
		TextView textView = new TextView(getApplicationContext());
		textView.setText(address);
		textView.setTextSize(22);
		textView.setTextColor(Color.RED);
		
		
	}

	
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		//注册打电话广播接收者
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		
		tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		
		//监听来电
		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//取消打电话广播接收者
		unregisterReceiver(receiver);
		receiver = null;
		
		//取消监听
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);	
		listener = null;
		
	}

}
