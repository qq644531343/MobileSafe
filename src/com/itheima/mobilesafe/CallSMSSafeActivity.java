package com.itheima.mobilesafe;

import java.util.List;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CallSMSSafeActivity extends Activity {

	private ListView lv_callsms_safe;
	private List<BlackNumberInfo> infos;
	private BlackNumberDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		
		lv_callsms_safe = (ListView)findViewById(R.id.lv_callsms_safe);
		dao = new BlackNumberDao(this);
		infos = dao.findAll();
		lv_callsms_safe.setAdapter(new CallSMSAdapter());
		
	}

	private class CallSMSAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Log.i(TAG, position);
			View view = View.inflate(getApplicationContext(), R.layout.list_item_callsms, null);
			TextView tv_black_number = (TextView) view.findViewById(R.id.tv_black_number);
			TextView tv_block_mode = (TextView) view.findViewById(R.id.tv_block_mode);
			tv_black_number.setText(infos.get(position).getNumber());
			String mode = infos.get(position).getModel();
			if ("1".equals(mode)) {
				tv_block_mode.setText("电话拦截");
			} else if ("2".equals(mode)) {
				tv_block_mode.setText("短信拦截");
			} else {
				tv_block_mode.setText("全部拦截");
			}
			return view;
		}

	}

}
