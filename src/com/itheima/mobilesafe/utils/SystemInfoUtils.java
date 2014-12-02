package com.itheima.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * 系统信息
 * @author libo
 *
 */
public class SystemInfoUtils {

	/**
	 * 获取正在运行的进程数量
	 * @return
	 */
	public static int getRunningProcessCount(Context context) {
		
		ActivityManager  am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		return infos.size();
	}
	
	/**
	 * 剩余内存
	 */
	public static long getAvailMem(Context context) {
		ActivityManager  am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
	
	/**
	 * 总内存
	 */
	public static long getTotalMem(Context context) {
//		ActivityManager  am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo = new MemoryInfo();
//		am.getMemoryInfo(outInfo);
//		return outInfo.totalMem;
		
		File file = new File("/proc/meminfo");
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader bf = new BufferedReader(new InputStreamReader(fis));
			String line = bf.readLine();
			StringBuilder sb = new StringBuilder();
			for(char c: line.toCharArray())
			{
				if (c >= '0' && c <='9') {
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString()) * 1024;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}
