package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {
	
	private static String path = "data/data/com.itheima.mobilesafe/files/address.db";

	/**
	 * 	查询号码对应的归属地
	 * @param number
	 * @return
	 */
	public static String querNumber(String number)
	{
		//号码过滤
		
		
		String address = number;
		SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor  cursor = database.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)", new String[]{number.substring(0, 7)});
		while(cursor.moveToNext())
		{
			String locationString = cursor.getString(0);
			address = locationString;
		}
		cursor.close();
		return address;
	}
	
}
