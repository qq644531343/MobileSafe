package com.itheima.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallSMSSafeService extends Service {

	public static final String TAG = "CallSMSSafeService";
	private InnerSMSReceiver receiver;
	private TelephonyManager tm;
	private MyCallListener listener;

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
			// 检查发件人是否是黑名单号码，且设置了短信拦截或全部拦截
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String sender = smsMessage.getOriginatingAddress();
				String resultString = dao.findMode(sender);
				if ("2".equals(resultString) || "3".equals(resultString)) {
					Log.i(TAG, "拦截短信:" + sender + " 说：" + smsMessage.getMessageBody());
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

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyCallListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		// 注册广播接收者
		receiver = new InnerSMSReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		unregisterReceiver(receiver);
		receiver = null;

		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;

	}

	private class MyCallListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: // 铃响
				String resultString = dao.findMode(incomingNumber);
				System.out.println("result " + resultString + "  incomming:" + incomingNumber);
				if ("1".equals(resultString) || "3".equals(resultString)) {
					Log.i(TAG, "拦截电话: " + incomingNumber);
					endCall();
				}
				break;

			default:
				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}
	}

	public void endCall() {
		// TODO Auto-generated method stub
//		IBinder iBinder = ServiceManager.getService(TELEPHONY_SERVICE);
		//反射
		try {
			//加在ServiceManager字节码
			Class clazz = CallSMSSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method method =  clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(iBinder).endCall();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
