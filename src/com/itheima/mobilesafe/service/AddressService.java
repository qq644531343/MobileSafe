package com.itheima.mobilesafe.service;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.NumberAddressQueryUtils;

public class AddressService extends Service {

	protected static final String TAG = "AddressService";
	// 监听来电
	private TelephonyManager tm;
	private MyPhoneStateListener listener;

	private OutCallReceiver receiver;

	// 窗体管理者
	private WindowManager wm;
	private View view ;
	
	private SharedPreferences sp;

	private class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state:状态 incomingNumber:电话号码
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 铃声想起/来电
				// 根据得到的电话号码查询归属地,并toast
				String address = NumberAddressQueryUtils
						.querNumber(incomingNumber);
				// Toast.makeText(getApplicationContext(),
				// address, 1).show();
				myToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE://空闲状态/挂掉电话
				if (view!=null) {
					wm.removeView(view);
					view = null;
				}

			default:
				break;
			}
		}

	}

	// 打电话广播
	private class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 得到要打出去的电话号码
			String phone = getResultData();
			String address = NumberAddressQueryUtils
					.querNumber(phone);
//			Toast.makeText(context, address, 1).show();
			myToast(address);
		}

	}
	
	private  WindowManager.LayoutParams params;

	public void myToast(String address) {
		// TODO Auto-generated method stub

		view = View.inflate(this, R.layout.address_show, null);
		
		TextView textView = (TextView) view
				.findViewById(R.id.tv_address);
		textView.setText(address);
		
		//{"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
		int which = sp.getInt("which", 0);
		int[] ids = {R.drawable.call_locate_white, R.drawable.call_locate_orange,
				R.drawable.call_locate_blue, R.drawable.call_locate_gray,R.drawable.call_locate_green};
		view.setBackgroundResource(ids[which]);
		
	    params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.gravity = Gravity.TOP + Gravity.LEFT;
		params.x = sp.getInt("lastx", 100);
		params.y = sp.getInt("lasty", 100);
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		params.setTitle("Toast");

		wm.addView(view, params);
		
		
		view.setOnTouchListener(new OnTouchListener() {
			
			int startX;
			int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://手指按下
					startX = (int)event.getRawX();
					startY = (int)event.getRawY();
					Log.i(TAG, "startX: " + startX + "  startY: " + startY);
					break;
				case MotionEvent.ACTION_MOVE://移动
					int newX = (int)event.getRawX();
					int newY = (int)event.getRawY();
					
					int dx = newX - startX;
					int dy = newY - startY;
					
					Log.i(TAG, "newX: " + newX + "  newY: " + newY);
					Log.i(TAG, "dX: " + dx + "  dy: " + dy);

					params.x += dx;
					params.y += dy;
					
					//边界问题
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.x > wm.getDefaultDisplay().getWidth() - view.getWidth()) {
						params.x = wm.getDefaultDisplay().getWidth() - view.getWidth();
					}
					if (params.y > wm.getDefaultDisplay().getHeight() - view.getHeight()) {
						params.y = wm.getDefaultDisplay().getHeight() - view.getHeight();
					}
					
					wm.updateViewLayout(view, params);
					
					startX = newX;
					startY = newY;
					
					break;
				case MotionEvent.ACTION_UP://离开
					int endX = (int)event.getRawX();
					int endY = (int)event.getRawY();
					Log.i(TAG, "endX: " + endX + "  endY: " + endY);
					
					//记录toast距离界面左上的距离
					Editor eidt = sp.edit();
					eidt.putInt("lastx", params.x);
					eidt.putInt("lasty", params.y);
					eidt.commit();
					
					break;
				default:
					break;
				}
				
				return false;//true事件处理完毕，不让其他控件接收
			}
		});
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// 注册打电话广播接收者
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter(
				"android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		// 监听来电
		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 取消打电话广播接收者
		unregisterReceiver(receiver);
		receiver = null;

		// 取消监听
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;

	}

}
