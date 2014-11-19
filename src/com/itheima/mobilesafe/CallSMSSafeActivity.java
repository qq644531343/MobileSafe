package com.itheima.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

public class CallSMSSafeActivity extends Activity {

	public static final String TAG = "CallSMSSafeActivity";
	private ListView lv_callsms_safe;
	private List<BlackNumberInfo> infos;
	private BlackNumberDao dao;
	private CallSMSAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);

		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		dao = new BlackNumberDao(this);
		infos = dao.findAll();
		adapter = new CallSMSAdapter();
		lv_callsms_safe.setAdapter(adapter);

	}

	private EditText et_blacknumber;
	private CheckBox cb_phone;
	private CheckBox cb_sms;
	private Button bt_ok;
	private Button bt_cancel;

	public void addBlackNunber(View view) {

		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog dialog = builder.create();
		View contentView = View.inflate(this, R.layout.dialog_add_blacknumber, null);
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();

		et_blacknumber = (EditText) contentView.findViewById(R.id.et_blacknumber);
		cb_phone = (CheckBox) contentView.findViewById(R.id.cb_phone);
		cb_sms = (CheckBox) contentView.findViewById(R.id.cb_sms);
		bt_ok = (Button) contentView.findViewById(R.id.ok);
		bt_cancel = (Button) contentView.findViewById(R.id.cancel);

		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String blacknumber = et_blacknumber.getText().toString().trim();
				if (TextUtils.isEmpty(blacknumber)) {
					Toast.makeText(getApplicationContext(), "黑名单号码不能为空", 0).show();
					return;
				}
				String mode = "3";
				if (cb_phone.isChecked() && cb_sms.isChecked()) {
					// 全部拦截
					mode = "3";
				} else if (cb_phone.isChecked()) {
					// 电话拦截
					mode = "1";

				} else if (cb_sms.isChecked()) {
					// 短信拦截
					mode = "2";

				} else {
					Toast.makeText(getApplicationContext(), "请选择拦截模式", 0).show();
					return;
				}
				// 存储数据
				dao.add(blacknumber, mode);
				// 更新listview
				BlackNumberInfo info = new BlackNumberInfo();
				info.setNumber(blacknumber);
				info.setModel(mode);
				infos.add(0, info);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});

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
		public View getView(final int position, View convertView, ViewGroup parent) {

			// view对象复用
			View view = null;
			ViewHolder holder;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(), R.layout.list_item_callsms, null);
				// 加快子view的查找速度
				holder = new ViewHolder();
				holder.tv_number = (TextView) view.findViewById(R.id.tv_black_number);
				holder.tv_model = (TextView) view.findViewById(R.id.tv_block_mode);
				holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
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

			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 删除
					BlackNumberInfo info = infos.get(position);
					dao.delete(info.getNumber());
					infos.remove(position);
					adapter.notifyDataSetChanged();
				}
			});

			return view;
		}

		/**
		 * View对象的容器，记录子View
		 */
		class ViewHolder {
			TextView tv_number;
			TextView tv_model;
			ImageView iv_delete;
		}

	}

}
