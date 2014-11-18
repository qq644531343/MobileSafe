package com.itheima.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.R.color;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.AvoidXfermode.Mode;
import android.widget.ListView;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

/**
 * 黑名单数据库的增删改查业务类
 * 
 * @author libo
 * 
 */
public class BlackNumberDao {

	private BlackNumberDBOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * 查询全部黑名单号码
	 */
	public List<BlackNumberInfo> findAll() {

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blacknumber", null);

		List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();

		while (cursor.moveToNext()) {
			BlackNumberInfo info = new BlackNumberInfo();
			String number  = cursor.getString(0);
			String mode  = cursor.getString(1);
			info.setNumber(number);
			info.setModel(mode);
			list.add(info);
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 查询黑名单号码是否存在
	 */
	public boolean find(String number) {

		boolean result = false;

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber where number = ?", new String[] { number });
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}

	/**
	 * 添加黑名单号码 mode:1电话拦截 2:短信拦截 3全部拦截
	 */
	public void add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		db.insert("blacknumber", null, values);
	}

	/**
	 * 修改黑名单号码的拦截模式
	 */
	public void update(String number, String newmode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		db.update("blacknumber", values, "number=?", new String[] { number });
		db.close();
	}

	/**
	 * 删除黑名单号码
	 */
	public void delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacknumber", "number=?", new String[] { number });
		db.close();
	}

}
