package com.itheima.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *  黑名单管理
 * @author libo
 *
 */
public class BlackNumberDBOpenHelper  extends SQLiteOpenHelper{

	//创建数据库blacknumber.db
	public BlackNumberDBOpenHelper(Context context) {
		super(context, "blacknumber.db", null, 1);
		
	}

	//初始化表结构
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacknumber (" +
				"_id integer primary key autoincrement," +
				"number varchar(20)," +
				"mode varchar(2) )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
