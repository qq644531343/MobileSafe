package com.itheima.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;

/**
 * 设置向导基础类
 * 
 * @author libo
 * 
 */
public abstract class BaseSetupActivity extends Activity {

	protected static final String TAG = "BaseSetupActivity";
	// 手势识别器
	private GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		detector = new GestureDetector(this,
				new SimpleOnGestureListener() {

					// 手指滑动时
					// e1起点，e2终点，velocityX x轴速度 单位pix/s
					@Override
					public boolean onFling(MotionEvent e1,
							MotionEvent e2,
							float velocityX,
							float velocityY) {
						// TODO Auto-generated method
						// stub
						// 从左往右滑 显示上一页
						if ((e2.getRawX() - e1
								.getRawX()) > 200) {
							Log.i(TAG, "右滑");
							showPre();
							return true;
						}
						// 从右往左滑 显示下一页
						if (e1.getRawX() - e2.getRawX() > 200) {
							Log.i(TAG, "左滑");
							showNext();
							return true;
						}
						return super.onFling(e1, e2,
								velocityX,
								velocityY);
					}

				});
	}
	
	// 使用手势识别器
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			detector.onTouchEvent(event);
			return super.onTouchEvent(event);
		}

	public abstract  void showNext();
	
	public  abstract void showPre() ;
	
	public   void next(View view)
	{
		showNext();
	}
	
	public  void pre(View view)
	{
		showPre();
	}
}
