package com.itheima.mobilesafe.test;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;

import android.test.AndroidTestCase;

public class TestBlackNumberDB extends AndroidTestCase {

	public void testCreateDB() throws Exception {
		BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
		helper.getWritableDatabase();
	}

}
