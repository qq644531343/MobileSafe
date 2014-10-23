package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.MD5Tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	
	protected static final String TAG = "HomeActivity";
	private GridView list_home;
	private MyAdapter adapter;
	
	private SharedPreferences sp;
	
	private static String[] names = {
		"手机防盗","通讯卫士","软件管理",
		"进程管理","流量统计","手机杀毒",
		"缓存清理","高级工具","设置中心"
		};
	private static int[] ids = {
		R.drawable.safe,  R.drawable.callmsgsafe,  R.drawable.app,
		R.drawable.taskmanager,  R.drawable.netmanager, R.drawable.trojan,
		R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		list_home = (GridView)findViewById(R.id.list_home);
		adapter = new MyAdapter();
		list_home.setAdapter(adapter);
		
		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 8://设置中心
					Intent it = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(it);
					break;
				case 0://手机防盗
					showLostFindDialog();
					break;
				default:
					break;
				}
			}
			
		});
	}
	
	protected void showLostFindDialog() {
		// TODO Auto-generated method stub
		//判断是否设置过密码
		 if (isSetupPwd()) {
			//已经设置密码，弹出输入框
			 showEnterDialog();
		} else {
			//没有设置密码，弹出设置密码对话框
			showSetupPwdDialog();
		}
	}
	
	/**
	 * 输入密码对话框
	 */
	private void showEnterDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		//自定义一个布局文件，
		View view = View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);
		et_setup_pwd = (EditText)view.findViewById(R.id.et_setup_pwd);
		ok = (Button)view.findViewById(R.id.ok);
		cancel = (Button)view.findViewById(R.id.cancel);
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//把对话框取消掉
				dialog.dismiss();
			}
		});
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//取出密码
				String password = et_setup_pwd.getText().toString().trim();
				String savedPassword = sp.getString("password", "");
				if (TextUtils.isEmpty(password) || ! (MD5Tool.getMD5(password)).equals(savedPassword)) {
					Toast.makeText(HomeActivity.this, "密码不正确", 0).show();
					return;
				}
				Log.i(TAG, "密码输入正确");
				dialog.dismiss();
				//进入防盗页面 
			}
		});
		
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}
	
	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	private Button ok;
	private Button cancel;
	private AlertDialog dialog;
	
	/**
	 * 设置密码对话框
	 */
	private void showSetupPwdDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		//自定义一个布局文件，
		View view = View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);
		et_setup_pwd = (EditText)view.findViewById(R.id.et_setup_pwd);
		et_setup_confirm = (EditText)view.findViewById(R.id.et__setup_confirm);
		ok = (Button)view.findViewById(R.id.ok);
		cancel = (Button)view.findViewById(R.id.cancel);
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//把对话框取消掉
				dialog.dismiss();
			}
		});
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//取出密码
				String password = et_setup_pwd.getText().toString().trim();
				String confirm = et_setup_confirm.getText().toString().trim();
				
				if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm) || !password.equals(confirm))  {
					Toast.makeText(HomeActivity.this, "密码不正确", 0).show();
					return;
				}
				
				Editor editor = sp.edit();
				editor.putString("password", MD5Tool.getMD5(password));
				editor.commit();
				dialog.dismiss();
				Toast.makeText(HomeActivity.this, "密码设置成功", 0).show();
			}
		});
		
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 判断是否设置过密码
	 * @return
	 */
	private boolean isSetupPwd(){
		String pwd = sp.getString("password", null);
		return ! (TextUtils.isEmpty(pwd));
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
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
		public View getView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(HomeActivity.this, R.layout.list_item_home, null);
			ImageView iv_item = (ImageView)view.findViewById(R.id.iv_item);
			TextView   tv_item = (TextView)view.findViewById(R.id.tv_item);
			
			tv_item.setText(names[position]);
			iv_item.setImageResource(ids[position]);
			return view;
		}
		
		
	}
	
	
}
