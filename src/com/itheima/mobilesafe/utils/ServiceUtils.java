package com.itheima.mobilesafe.utils;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;

public class ServiceUtils {
	
	/**
	 * 校验服务是否处于运行中
	 * @param context
	 * @param serviceName
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String serviceName)
	{
		ActivityManager am = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(100);
		Log.i("ServiceUtils", "checking: " + serviceName);
		for (RunningServiceInfo info : infos) {
			//得到正在运行的服务名 
			String name = info.service.getClassName();
			if (serviceName.equals(name)) {
				Log.i("ServiceUtils", "result:isRunning");
				return true;
			}
		}
		return false;
	}
}
