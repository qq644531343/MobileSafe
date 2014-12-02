package com.itheima.mobilesafe.test;

import java.util.List;

import com.itheima.mobilesafe.domain.TaskInfo;
import com.itheima.mobilesafe.engine.TaskInfoProvider;

import android.test.AndroidTestCase;

public class TestTaskInfoProvider extends AndroidTestCase {

	public void testGetTaskInfos() throws Exception {
		
		List<TaskInfo> infos = TaskInfoProvider.getTaskInfos(getContext());
		for (TaskInfo taskInfo : infos) {
			System.out.println(taskInfo.toString());
		}
	}
}
