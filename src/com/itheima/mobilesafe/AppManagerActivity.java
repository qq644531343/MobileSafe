package com.itheima.mobilesafe;

import java.text.Format;
import java.util.List;

import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 	软件管理
 * @author libo
 *
 */
@SuppressLint("NewApi")
public class AppManagerActivity extends Activity {

	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	private ListView lv_app_manager;
	private LinearLayout ll_Loading;
	 private List<AppInfo> appInfos;
	 private AppManagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		
		tv_avail_rom = (TextView)findViewById(R.id.tv_avail_rom);
		tv_avail_sd = (TextView)findViewById(R.id.tv_avail_sd);
		
		long sdsize = getAvailabelSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
		long romsize = getAvailabelSpace(Environment.getDataDirectory().getAbsolutePath());

		tv_avail_sd.setText("SD卡可用空间: " + Formatter.formatFileSize(this, sdsize));
		tv_avail_rom.setText("内存可用空间: " + Formatter.formatFileSize(this, romsize));
		
		lv_app_manager = (ListView)findViewById(R.id.lv_app_manager);
		ll_Loading = (LinearLayout)findViewById(R.id.ll_Loading);
		
		ll_Loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if (adapter == null) {
							adapter = new AppManagerAdapter();
							lv_app_manager.setAdapter(adapter);
						}else{
							adapter.notifyDataSetChanged();
						}
						ll_Loading.setVisibility(View.INVISIBLE);
					}
				});
			};
		}.start();

	
	}
	
	private class AppManagerAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			
			return appInfos.size();
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
			
			View view = null;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder)convertView.getTag();
			}else{
				view = View.inflate(AppManagerActivity.this, R.layout.list_item_appinfo, null);
				holder = new ViewHolder();
				holder.tv_name = (TextView)view.findViewById(R.id.tv_app_name);
				holder.tv_location = (TextView)view.findViewById(R.id.tv_app_location);
				holder.iv_icon = (ImageView)view.findViewById(R.id.iv_app_icon);
				view.setTag(holder);
			}
			AppInfo info = appInfos.get(position);
			holder.tv_name.setText( info.getName());
			holder.iv_icon.setImageDrawable(info.getIcon());
			
			return view;
		}
		
		
	}
	
	static class ViewHolder
	{
		TextView tv_name;
		TextView tv_location;
		ImageView iv_icon;
		
	}
	
	
	/**
	 * 获取某个目录的可用空间
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
