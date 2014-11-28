package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;

/**
 * 软件管理
 * 
 * @author libo
 * 
 */
@SuppressLint("NewApi")
public class AppManagerActivity extends Activity {

	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	private ListView lv_app_manager;
	private LinearLayout ll_Loading;
	private TextView tv_status;

	private List<AppInfo> appInfos; // 所有应用信息
	private List<AppInfo> userAppInfos;// 用户应用信息
	private List<AppInfo> sysAppInfos;// 系统应用信息

	private AppManagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
		tv_status = (TextView) findViewById(R.id.tv_status);

		long sdsize = getAvailabelSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
		long romsize = getAvailabelSpace(Environment.getDataDirectory().getAbsolutePath());

		tv_avail_sd.setText("SD卡可用空间: " + Formatter.formatFileSize(this, sdsize));
		tv_avail_rom.setText("内存可用空间: " + Formatter.formatFileSize(this, romsize));

		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
		ll_Loading = (LinearLayout) findViewById(R.id.ll_Loading);

		ll_Loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {

				// 获取应用程序信息
				getAllAppInfos();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (adapter == null) {
							adapter = new AppManagerAdapter();
							lv_app_manager.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						ll_Loading.setVisibility(View.INVISIBLE);
					}
				});
			};
		}.start();

		lv_app_manager.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {

				if (userAppInfos != null && sysAppInfos != null) {
					if (firstVisibleItem > userAppInfos.size()) {
						tv_status.setText("系统程序: " + sysAppInfos.size() + "个");
					} else {
						tv_status.setText("用户程序: " + userAppInfos.size() + "个");
					}
				}

			}
		});

		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AppInfo appInfo;
				if (position == 0) {
					return;
				} else if (position == (userAppInfos.size() + 1)) {
					return;
				} else if (position <= userAppInfos.size()) {
					int newposition = position - 1;
					appInfo = userAppInfos.get(newposition);
				}else {
					int newposition = position -1 - userAppInfos.size() - 1;
					appInfo = sysAppInfos.get(newposition);
				}
			}
		});
	}

	protected void getAllAppInfos() {
		// TODO Auto-generated method stub
		appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
		if (userAppInfos == null || sysAppInfos == null) {
			userAppInfos = new ArrayList<AppInfo>();
			sysAppInfos = new ArrayList<AppInfo>();
		}
		for (AppInfo info : appInfos) {
			if (info.isUserApp()) {
				userAppInfos.add(info);
			} else {
				sysAppInfos.add(info);
			}
		}

	}

	private class AppManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return userAppInfos.size() + sysAppInfos.size() + 2;
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			AppInfo info;
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("用户程序: " + userAppInfos.size() + "个");
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			} else if (position == userAppInfos.size() + 1) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setText("系统程序: " + sysAppInfos.size() + "个");
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			} else if (position <= userAppInfos.size()) { // 用户程序
				info = userAppInfos.get(position - 1);
			} else { // 系统程序
				info = sysAppInfos.get(position - userAppInfos.size() - 2);
			}

			View view = null;
			ViewHolder holder;

			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) convertView.getTag();
			} else {
				view = View.inflate(AppManagerActivity.this, R.layout.list_item_appinfo, null);
				holder = new ViewHolder();
				holder.tv_name = (TextView) view.findViewById(R.id.tv_app_name);
				holder.tv_location = (TextView) view.findViewById(R.id.tv_app_location);
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
				view.setTag(holder);
			}

			holder.tv_name.setText(info.getName());
			holder.iv_icon.setImageDrawable(info.getIcon());
			if (info.isInRom()) {
				holder.tv_location.setText("安装位置: 手机内存");
			} else {
				holder.tv_location.setText("安装位置:  外部存储");
			}
			return view;
		}

	}

	static class ViewHolder {
		TextView tv_name;
		TextView tv_location;
		ImageView iv_icon;

	}

	/**
	 * 获取某个目录的可用空间
	 * 
	 * @param path
	 * @return
	 */
	@SuppressLint("NewApi")
	private long getAvailabelSpace(String path) {

		StatFs statFs = new StatFs(path);
		long size = statFs.getBlockSizeLong();
		long count = statFs.getAvailableBlocksLong();

		return size * count;
	}
}
