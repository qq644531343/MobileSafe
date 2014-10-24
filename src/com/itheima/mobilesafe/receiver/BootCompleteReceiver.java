package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompleteReceiver";
	private SharedPreferences sp;
	private TelephonyManager tm;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		tm =(TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		//读取已保存得sim信息
		String saveSim = sp.getString("sim", "") + "afu";
		//读取当前的sim信息
		String realSim = tm.getSimSerialNumber();
		//比较
		if (saveSim.equals(realSim)) {
			//sim卡没有变更
			Log.i(TAG, "sim卡没有变更");
		}else {
			//sim卡已变更，发一个短信给安全号码
			Log.i(TAG, "sim卡已变更");
			Toast.makeText(context, "sim卡已变更", 1).show();
		}

	}

}
