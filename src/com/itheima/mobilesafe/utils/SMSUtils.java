package com.itheima.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

/**
 * 短信操作工具
 * 
 * @author libo
 * 
 */
public class SMSUtils {

	/**
	 * 备份短信的回调接口
	 * 
	 * @author libo
	 * 
	 */
	public interface BackUpCallBack {
		/**
		 * 开始备份，得到进度的最大值
		 * 
		 * @param max
		 */
		public void beforeBackup(int max);

		/**
		 * 备份中，得到备份进度
		 * 
		 * @param progress
		 */
		public void onSmsBackup(int progress);
	}

	/**
	 * 备份用户短信
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void backupSMS(Context context, BackUpCallBack callback) throws Exception {

		// 读短信
		ContentResolver resolver = context.getContentResolver();

		// 写xml

		File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
		FileOutputStream fos = new FileOutputStream(file);

		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(fos, "utf-8");

		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "smss");

		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[] { "body", "address", "type", "date" }, null, null,
				null);
		int max = cursor.getCount();
		callback.beforeBackup(max);
		serializer.attribute(null, "max", max + "");

		int process = 0;
		while (cursor.moveToNext()) {
			String body = cursor.getString(0);
			String address = cursor.getString(1);
			String type = cursor.getString(2);
			String date = cursor.getString(3);

			serializer.startTag(null, "sms");

			serializer.startTag(null, "body");
			serializer.text(body);
			serializer.endTag(null, "body");

			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");

			serializer.startTag(null, "type");
			serializer.text(type);
			serializer.endTag(null, "type");

			serializer.startTag(null, "date");
			serializer.text(date);
			serializer.endTag(null, "date");

			serializer.endTag(null, "sms");

			process++;
			callback.onSmsBackup(process);
		}

		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();

	}

	/**
	 * 还原短信
	 */
	public static void restoreSMS(Context context, boolean deleteOld) throws Exception {

		Uri uri = Uri.parse("content://sms/");
		if (deleteOld) {
			context.getContentResolver().delete(uri, null, null);
		}

		// 读xml
		File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
		FileInputStream fis = new FileInputStream(file);
		
		String max = "0";
		ContentValues values = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(fis, "utf-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			
			switch (event) {

			case XmlPullParser.START_DOCUMENT:
				break;
				
			case XmlPullParser.START_TAG:
				
				if ("smss".equals(parser.getName())) {
					max = parser.getAttributeValue(null, "max");
				}
				if ("sms".equals(parser.getName())) {
					 values = new ContentValues();
				}else if ("body".equals(parser.getName())) {
					values.put("body", parser.nextText());
				}else if("address".equals(parser.getName()))
				{
					values.put("address", parser.nextText());
				}else if("type".equals(parser.getName()))
				{
					values.put("type", parser.nextText());
				}else if("date".equals(parser.getName()))
				{
					values.put("date", parser.nextText());
				}

				
				break;
			case XmlPullParser.END_TAG:
			
				if ("sms".equals(parser.getName())) {
					context.getContentResolver().insert(uri, values);
				}
				
				break;
			default:
				break;
			}
			event = parser.next();
		}
		
		fis.close();

	}

}
