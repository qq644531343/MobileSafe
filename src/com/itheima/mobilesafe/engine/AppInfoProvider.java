package com.itheima.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.pm.ApplicationInfo;
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
			boolean userApp;
			boolean inRom;
			
			int flags = info.applicationInfo.flags;
			if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			//用户程序
				userApp = true;
			}else{
			//系统程序
				userApp = false;
			}
			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				//安装在手机内存
				inRom = true;
			}else{
				//安装在存储卡
				inRom = false;
			}
			
			AppInfo appInfo = new AppInfo();
			appInfo.setName(name);
			appInfo.setIcon(packIcon);
			appInfo.setPackname(packName);
			appInfo.setInRom(inRom);
			appInfo.setUserApp(userApp);
			appInfos.add(appInfo);
		}
		
		
		return appInfos;
	}
	
}
