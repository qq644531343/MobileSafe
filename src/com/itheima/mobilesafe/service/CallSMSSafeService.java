package com.itheima.mobilesafe.service;

import java.util.Iterator;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.receiver.SMSReceiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

public class CallSMSSafeService extends Service {

	public static final String TAG = "CallSMSSafeService";
	private InnerSMSReceiver receiver;
	
	private BlackNumberDao dao;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class InnerSMSReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "内部广播接收者，短信到来了");
			//检查发件人是否是黑名单号码，且设置了短信拦截或全部拦截
			Object[] objects= (Object[])intent.getExtras().get("dpus");
			for (Object object : objects) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
				String sender = smsMessage.getOriginatingAddress();
				String resultString = dao.findMode(sender);
				if ("2".equals(resultString)  || "3".equals(resultString)) {
					Log.i(TAG, "拦截短信:" + sender + smsMessage.getMessageBody());
					abortBroadcast();
				}
			}
			
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		dao = new BlackNumberDao(this);
		
		// 注册广播接收者
		receiver = new InnerSMSReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		unregisterReceiver(receiver);
		receiver = null;
		
	}

}
