package com.itheima.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

public class SelectContactActivity extends Activity {
	
	private ListView list_select_contact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		
		list_select_contact = (ListView)findViewById(R.id.list_select_contact);
	}
}
