package com.itheima.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
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
					//挂断电话
					endCall(); //非同步线程
					
					//删除呼叫记录
//					deleteCallLog(incomingNumber);
					//观察呼叫记录数据库内容变化
					Uri uri =  Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true, new MyContentObserver(incomingNumber,new Handler()));
					
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
			
			//提示号码为空或已关机，原理为呼叫转移
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void deleteCallLog(String incomingNumber) {
		ContentResolver resolver = getContentResolver();
		Uri uri =  Uri.parse("content://call_log/calls");
		resolver.delete(uri, "number=?", new String[] {incomingNumber});
	}
	
	private class MyContentObserver extends ContentObserver
	{
		private String incomingNumber;
		public MyContentObserver(String incomingNumber ,Handler handler) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			Log.i(TAG, "呼叫记录变化");
			deleteCallLog(incomingNumber);
			getContentResolver().unregisterContentObserver(this);
		}
		
	}

}

/*
 1、无条件转移 
设置后所有来电将立即转接到预先设定的固定电话或其他信息工具上。 
设置方法：拨*57*转出的电话号码#，听到证实音后挂机。 
注销方式：拨#57#，听到证实音后挂机。 

2、遇忙呼叫转移 
用户在通话过程中有第三方对其呼叫，将电话转移至预先设好的固定电话或其他通信工具上。
如果没有设置无应答呼叫转移，拒绝接听时也会将这个电话视为遇忙呼叫转移。 
设置方法：拨*40*转出电话号码#，听到证实音后挂机。 
注销方式：拨#40#，听到证实音后挂机。 

3、无应答呼叫转移 

接通后而无人应答对所发生的电话转移，一般小灵通上振铃超过6次而接听则视为无应答转移。 
设置方法：拨*41*转出电话号码#，听到证实音后挂机。 
注销方式：拨#41#，听到证实音后挂机。 
 */
