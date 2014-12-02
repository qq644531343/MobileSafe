package com.itheima.mobilesafe;

import java.text.Format;

import com.itheima.mobilesafe.utils.SystemInfoUtils;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

/**
 * 任务/进程管理器
 * @author libo
 *
 */
public class TaskManagerActivity extends Activity {

	private TextView tv_process_count;
	private TextView tv_mem_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		
		tv_process_count = (TextView)findViewById(R.id.tv_process_count);
		tv_mem_info = (TextView)findViewById(R.id.tv_mem_info);
		
		int processCount = SystemInfoUtils.getRunningProcessCount(this);
		long availMem = SystemInfoUtils.getAvailMem(this);
		long totalMem = SystemInfoUtils.getTotalMem(this);
		
		tv_process_count.setText("运行中的进程:" + processCount + "个");
		tv_mem_info.setText("剩余/总内存:" + Formatter.formatFileSize(this, availMem) + "/" + Formatter.formatFileSize(this, totalMem));
	
	}
}
