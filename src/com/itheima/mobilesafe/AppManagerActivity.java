package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;
import com.itheima.mobilesafe.utils.DensityUtil;

/**
 * 软件管理
 * 
 * @author libo
 * 
 */
@SuppressLint("NewApi")
public class AppManagerActivity extends Activity implements OnClickListener{

	private static final String TAG = "AppManagerActivity";
	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	private ListView lv_app_manager;
	private LinearLayout ll_Loading;
	private TextView tv_status;

	private List<AppInfo> appInfos; // 所有应用信息
	private List<AppInfo> userAppInfos;// 用户应用信息
	private List<AppInfo> sysAppInfos;// 系统应用信息

	private AppManagerAdapter adapter;

	private PopupWindow popupWindow;
	private AppInfo appInfo;	//被点击的条目
	
	private LinearLayout ll_uninstall;
	private LinearLayout ll_start;
	private LinearLayout ll_share;

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
				dimissPopupWindow();
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
				
				if (position == 0) {
					return;
				} else if (position == (userAppInfos.size() + 1)) {
					return;
				} else if (position <= userAppInfos.size()) {
					int newposition = position - 1;
					appInfo = userAppInfos.get(newposition);
				} else {
					int newposition = position - 1 - userAppInfos.size() - 1;
					appInfo = sysAppInfos.get(newposition);
				}

				dimissPopupWindow();
				
				View contentView = View.inflate(getApplicationContext(), R.layout.popup_app_item, null);
				ll_uninstall = (LinearLayout)contentView.findViewById(R.id.ll_uninstall);
				ll_start = (LinearLayout)contentView.findViewById(R.id.ll_start);
				ll_share = (LinearLayout)contentView.findViewById(R.id.ll_share);
				ll_uninstall.setOnClickListener(AppManagerActivity.this);
				ll_share.setOnClickListener(AppManagerActivity.this);
				ll_start.setOnClickListener(AppManagerActivity.this);
				
				popupWindow = new PopupWindow(contentView, -2, -2);
				int[] location = new int[2];
				view.getLocationInWindow(location);
				popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.TOP, DensityUtil.dip2px(AppManagerActivity.this, 60), location[1]);
			
				//!!!!动画要求窗体必须有背景颜色
				ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0,Animation.RELATIVE_TO_SELF, 0.5f);
				sa.setDuration(300);
				AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
				AnimationSet set = new AnimationSet(false);
				set.addAnimation(aa);
				set.addAnimation(sa);
				contentView.startAnimation(set);
				
			}
		});
	}

	protected void getAllAppInfos() {

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

	public void dimissPopupWindow() {
		// 把旧的弹出窗体关闭
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	protected void onDestroy() {

		dimissPopupWindow();
		super.onDestroy();
	}

	//布局的点击时间
	@Override
	public void onClick(View v) {
		dimissPopupWindow();
		switch (v.getId()) {
		case R.id.ll_share:
			Log.i(TAG, "分享：" + appInfo.getName());
			
			break;
		case R.id.ll_start:
			Log.i(TAG, "启动：" + appInfo.getName());
			
			break;
		case R.id.ll_uninstall:
			Log.i(TAG, "卸载：" + appInfo.getName());
			
			break;
		default:
			break;
		}
		
	}
}
