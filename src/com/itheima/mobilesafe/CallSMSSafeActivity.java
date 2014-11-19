package com.itheima.mobilesafe;

import java.util.List;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CallSMSSafeActivity extends Activity {

	public static final String TAG = "CallSMSSafeActivity";
	private ListView lv_callsms_safe;
	private List<BlackNumberInfo> infos;
	private BlackNumberDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);

		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		dao = new BlackNumberDao(this);
		infos = dao.findAll();
		lv_callsms_safe.setAdapter(new CallSMSAdapter());

	}

	public void addBlackNunber(View view) {
		AlertDialog.Builder builder = new Builder(this);
		AlertDialog dialog = builder.create();
		View contentView = View.inflate(this, R.layout.dialog_add_blacknumber, null);
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();
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

			// view对象复用
			View view = null;
			ViewHolder holder;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(), R.layout.list_item_callsms, null);
				// 加快子view的查找速度
				holder = new ViewHolder();
				holder.tv_number = (TextView) view.findViewById(R.id.tv_black_number);
				holder.tv_model = (TextView) view.findViewById(R.id.tv_block_mode);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			holder.tv_number.setText(infos.get(position).getNumber());
			String mode = infos.get(position).getModel();
			if ("1".equals(mode)) {
				holder.tv_model.setText("电话拦截");
			} else if ("2".equals(mode)) {
				holder.tv_model.setText("短信拦截");
			} else {
				holder.tv_model.setText("全部拦截");
			}
			return view;
		}

		/**
		 * View对象的容器，记录子View
		 */
		class ViewHolder {
			TextView tv_number;
			TextView tv_model;
		}

	}

}
