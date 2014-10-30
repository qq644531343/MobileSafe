package com.itheima.mobilesafe.receiver;

import com.itheima.mobilesafe.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		
		// 接收短信
		Object[] objs = (Object[]) intent.getExtras().get("pdus");

		for (Object b : objs) {
			// 具体的某一条短信
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) b);
			String sender = sms.getOriginatingAddress();
			String body = sms.getMessageBody();
			
			String safenumber = sp.getString("safenumber", "");
			if (sender.contains(safenumber)) {
				if ("#*location*#".equals(body)) {
					Log.i(TAG, "收到定位短信");
					abortBroadcast();
				} else if ("#*alarm*#".equals(body)) {
					Log.i(TAG, "播放报警音乐");
					playAlarm(context);
					abortBroadcast();
				} else if ("#*wipedata*#".equals(body)) {
					Log.i(TAG, "清除数据");
					abortBroadcast();
				} else if ("#*alarm*#".equals(body)) {
					Log.i(TAG, "远程锁屏");
					abortBroadcast();
				}
			}
			
		}
	}

	private void playAlarm(Context context) {
		// TODO Auto-generated method stub
		MediaPlayer player  = MediaPlayer.create(context, R.raw.ylzs);
		player.setLooping(false);
		player.setVolume(1.0f, 1.0f);
		player.start();
	}

}
