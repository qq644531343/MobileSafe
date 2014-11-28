package com.itheima.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.itheima.mobilesafe.domain.AppInfo;

/**
 * 提供手机安装的所有应用程序信息
 * @author libo
 *
 */
public class AppInfoProvider {

	/**
	 * 获取所有应用程序的信息
	 * @return
	 */
	public static List<AppInfo> getAppInfos(Context context) {
		
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packinfos =  pm.getInstalledPackages(0); //所有的安装在系统上的应用包信息
		
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		for (PackageInfo info : packinfos) {
			//info  相当于应用清单文件
			String packName = info.packageName;
			Drawable packIcon = info.applicationInfo.loadIcon(pm);
			String name = info.applicationInfo.loadLabel(pm).toString();
			AppInfo appInfo = new AppInfo();
			appInfo.setName(name);
			appInfo.setIcon(packIcon);
			appInfo.setPackname(packName);
			
			appInfos.add(appInfo);
		}
		
		
		return appInfos;
	}
	
}
