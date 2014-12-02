package com.itheima.mobilesafe;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.AppManagerActivity.ViewHolder;
import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.domain.TaskInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;
import com.itheima.mobilesafe.engine.TaskInfoProvider;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewDebug.FlagToString;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/**
 * 任务/进程管理器
 * @author libo
 *
 */
public class TaskManagerActivity extends Activity {

	private TextView tv_process_count;
	private TextView tv_mem_info;
	private LinearLayout ll_Loading;
	private ListView lv_task_manager;
	private TextView tv_status;

	private List<TaskInfo> userTaskInfos;// 用户应用信息
	private List<TaskInfo> sysTaskInfos;// 系统应用信息
	private List<TaskInfo> allTaskInfos;
	private TaskManagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		
		tv_process_count = (TextView)findViewById(R.id.tv_process_count);
		tv_mem_info = (TextView)findViewById(R.id.tv_mem_info);
		ll_Loading = (LinearLayout)findViewById(R.id.ll_Loading);
		lv_task_manager = (ListView)findViewById(R.id.lv_task_manager);
		tv_status = (TextView) findViewById(R.id.tv_status);
	
		fillData();
		
		lv_task_manager.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				
				if (userTaskInfos != null && sysTaskInfos != null) {
					if (firstVisibleItem > userTaskInfos.size()) {
						tv_status.setText("系统进程: " + sysTaskInfos.size() + "个");
					} else {
						tv_status.setText("用户进程: " + userTaskInfos.size() + "个");
					}
				}

			}
		});
		
		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TaskInfo info;
				if (position == 0) {
					return;
				} else if (position == userTaskInfos.size() + 1) {
					return;
				} else if (position <= userTaskInfos.size()) { // 用户程序
					info = userTaskInfos.get(position - 1);
				} else { // 系统程序
					info = sysTaskInfos.get(position - userTaskInfos.size() - 2);
				}
				ViewHolder holder = (ViewHolder)view.getTag();
				if (info.isChecked()) {
					info.setChecked(false);
					holder.cb_status.setChecked(false);
				}else{
					info.setChecked(true);
					holder.cb_status.setChecked(true);
				}
			}
		});
	}

	public void setTitle() {
		int processCount = SystemInfoUtils.getRunningProcessCount(this);
		long availMem = SystemInfoUtils.getAvailMem(this);
		long totalMem = SystemInfoUtils.getTotalMem(this);
		
		tv_process_count.setText("运行中的进程:" + processCount + "个");
		tv_mem_info.setText("剩余/总内存:" + Formatter.formatFileSize(this, availMem) + "/" + Formatter.formatFileSize(this, totalMem));
	}

	//填充数据
	private void fillData() {
		// TODO Auto-generated method stub
		ll_Loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				getAllTaskInfos();
				//更新界面
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if (adapter == null) {
							adapter = new TaskManagerAdapter();
							lv_task_manager.setAdapter(adapter);
						} else {
							System.out.println(adapter);
							adapter.notifyDataSetChanged();
						}
						ll_Loading.setVisibility(View.INVISIBLE);
						setTitle();
					}
				});
			};
		}.start();
	}
	
	private class TaskManagerAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userTaskInfos.size() + sysTaskInfos.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TaskInfo info;
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("用户进程: " + userTaskInfos.size() + "个");
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			} else if (position == userTaskInfos.size() + 1) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setText("系统进程: " + sysTaskInfos.size() + "个");
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			} else if (position <= userTaskInfos.size()) { // 用户程序
				info = userTaskInfos.get(position - 1);
			} else { // 系统程序
				info = sysTaskInfos.get(position - userTaskInfos.size() - 2);
			}

			View view = null;
			ViewHolder holder;

			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) convertView.getTag();
			} else {
				view = View.inflate(TaskManagerActivity.this, R.layout.list_item_taskinfo, null);
				holder = new ViewHolder();
				holder.tv_name = (TextView) view.findViewById(R.id.tv_app_name);
				holder.tv_location = (TextView) view.findViewById(R.id.tv_app_location);
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
				holder.cb_status = (CheckBox)view.findViewById(R.id.cb_status);
				view.setTag(holder);
			}

			holder.tv_name.setText(info.getName());
			holder.iv_icon.setImageDrawable(info.getIcon());
			holder.cb_status.setChecked(info.isChecked());
			if (info.isUserTask()) {
				holder.tv_location.setText("用户进程:" + Formatter.formatFileSize(TaskManagerActivity.this, info.getMemsize()));
			} else {
				holder.tv_location.setText("系统进程:" +  Formatter.formatFileSize(TaskManagerActivity.this, info.getMemsize()));
			}
			return view;
		}
		
	}
	
	static class ViewHolder {
		TextView tv_name;
		TextView tv_location;
		ImageView iv_icon;
		CheckBox cb_status;
	}
	
	protected void getAllTaskInfos() {
		if (allTaskInfos != null) {
			allTaskInfos.clear();
		}
		allTaskInfos = TaskInfoProvider.getTaskInfos(getApplicationContext());
		if (userTaskInfos == null || sysTaskInfos == null) {
			userTaskInfos = new ArrayList<TaskInfo>();
			sysTaskInfos = new ArrayList<TaskInfo>();
		}else{
			userTaskInfos.clear();
			sysTaskInfos.clear();
		}
		for (TaskInfo info : allTaskInfos) {
			if (info.isUserTask()) {
				userTaskInfos.add(info);
			} else {
				sysTaskInfos.add(info);
			}
		}

	}
	
	//选中全部
	public void selectAll(View view) {
		for (TaskInfo info : userTaskInfos) {
			info.setChecked(true);
		}
		for (TaskInfo info : sysTaskInfos) {
			info.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}
	
	//反选
	public void selectOpposite(View view) {
		for (TaskInfo info : userTaskInfos) {
			info.setChecked(! info.isChecked());
		}
		for (TaskInfo info : sysTaskInfos) {
			info.setChecked(! info.isChecked());
		}
		adapter.notifyDataSetChanged();
	}
	
	//一键清理
	public void killAll(View view) {
		ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		for (TaskInfo info : userTaskInfos) {
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackname());
			}
		}
		for (TaskInfo info : sysTaskInfos) {
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackname());
			}
		}
		fillData();
	}
	
	//设置
	public void enterSetting(View view) {
		
	}
}
