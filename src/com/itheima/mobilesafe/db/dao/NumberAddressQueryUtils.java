package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {

	private static String path = "data/data/com.itheima.mobilesafe/files/address.db";

	/**
	 * 查询号码对应的归属地
	 * 
	 * @param number
	 * @return
	 */
	public static String querNumber(String number) {
		String address = number;

		SQLiteDatabase database = SQLiteDatabase.openDatabase(path,
				null, SQLiteDatabase.OPEN_READONLY);

		// 号码过滤 ^1[34568]\d{9}$
		String regString = "^1[34568]\\d{9}$";
		if (number.matches(regString)) {
			// 手机号码
			Cursor cursor = database
					.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[] { number
									.substring(0,
											7) });
			while (cursor.moveToNext()) {
				String locationString = cursor.getString(0);
				address = locationString;
			}
			cursor.close();
		} else {
			// 其他号码
			switch (number.length()) {
			case 3:
				address = "匪警";
				break;
			case 4:
				// 模拟器
				address = "模拟器";
				break;
			case 5:
				// 客服
				address = "客服";
				break;
			case 7:
			case 8:
				// 本地电话
				address = "本地电话";
				break;
			default:
				// 长途电话
				if (number.length() > 10
						&& number.startsWith("0")) {
					Cursor cursor = database
							.rawQuery("select location from data2 where area = ?",
									new String[] { number
											.substring(1,
													3) });
					while (cursor.moveToNext()) {
						String locationString = cursor
								.getString(0);
						address = locationString
								.substring(0,
										locationString.length() - 2);
					}
					cursor.close();

					cursor = database
							.rawQuery("select location from data2 where area = ?",
									new String[] { number
											.substring(1,
													4) });
					while (cursor.moveToNext()) {
						String locationString = cursor
								.getString(0);
						address = locationString
								.substring(0,
										locationString.length() - 2);
					}
					cursor.close();

				}
				break;
			}
		}

		return address;
	}

}
