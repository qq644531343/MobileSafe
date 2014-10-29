package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectContactActivity extends Activity {

	private ListView list_select_contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);

		list_select_contact = (ListView) findViewById(R.id.list_select_contact);

		List<Map<String, String>> data = getContactInfo();

		list_select_contact.setAdapter(new SimpleAdapter(this, data,
				R.layout.contact_item_view, new String[] {
						"name", "phone" }, new int[] {
						R.id.tv_name, R.id.tv_phone }));
	}

	/**
	 * 读取联系人
	 * 
	 * @return
	 */
	private List<Map<String, String>> getContactInfo() {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		// 得到内容解析器
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts"); // 表路径
		Uri uriData = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver
				.query(uri, new String[] { "contact_id" },
						null, null, null);
		while (cursor.moveToNext()) {
			String contact_id = cursor.getString(0);

			if (contact_id != null) {
				// 具体的某个联系人
				Map<String, String> map = new HashMap<String, String>();
				Cursor cursor2 = resolver.query(uriData,
						new String[] { "data1",
								"mimetype" },
						"contact_id=?",
						new String[] { contact_id },
						null);
				while (cursor2.moveToNext()) {
					String data1 = cursor2.getString(0);
					String mimetype = cursor2.getString(1);
					System.out.println("id:" + contact_id
							+ "\tdata1:" + data1
							+ "\t mimetype:  "
							+ mimetype);

					if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {
						map.put("phone", data1);
					} else if ("vnd.android.cursor.item/name"
							.equals(mimetype)) {
						map.put("name", data1);
					}

				}

				list.add(map);
				cursor2.close();
			}
		}
		cursor.close();
		return list;
	}

}
