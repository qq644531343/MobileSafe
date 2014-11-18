package com.itheima.mobilesafe.test;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

import android.test.AndroidTestCase;

public class TestBlackNumberDB extends AndroidTestCase {

	public void testCreateDB() throws Exception {
		BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
		helper.getWritableDatabase();
	}

	public void testAdd() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		Random random = new Random();
		long  basenumber = 13161790000L;
		for (int i = 0; i < 100; i++) {
			dao.add(String.valueOf(basenumber + i), String.valueOf(random.nextInt(3) + 1));
		}
		
	}

	public void testDelete() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.delete("13161797155");
	}

	public void testUpdate() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.update("13161797155", "2");
	}

	public void testFind() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result =  dao.find("13161797155");
		assertEquals(true, result);
	}
	
	public void testFindAll()  throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		List<BlackNumberInfo> infos = dao.findAll();
		for (BlackNumberInfo blackNumberInfo : infos) {
			System.out.println(infos.toString());
		}
	}

}
