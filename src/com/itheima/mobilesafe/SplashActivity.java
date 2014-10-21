/**
 * 	功能：
 * 	1，显示logo及启动页
 * 2，应用程序初始化
 * 3，检查版本信息
 * 4，检查版权
 */

package com.itheima.mobilesafe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.mobilesafe.utils.StreamTools;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	
	protected static final String TAG = "SplashActivity";
	
	protected static final int ENTER_HOME = 1;
	protected static final int SHOW_UPDATE_DIALOG = 0;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR = 4;
	
	private TextView tv_splash_version;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.i(TAG, "升级检测结果: "+msg.what);
			switch (msg.what ) {
			case SHOW_UPDATE_DIALOG:
				
				break;
			case ENTER_HOME:
				enterHome();
				break;
			case URL_ERROR:
				enterHome();
				Toast.makeText(getApplicationContext(), "URL错误", Toast.LENGTH_SHORT).show();
				break;
			case NETWORK_ERROR:
				enterHome();
				Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case JSON_ERROR:
				enterHome();
				Toast.makeText(SplashActivity.this, "JSON解析出错", Toast.LENGTH_SHORT).show();
				break;
				
			default:
				break;
			}
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		tv_splash_version = (TextView)findViewById(R.id.tv_splash_version);
		tv_splash_version.setText(getString(R.string.appversion) + getVersionName());
		
		//检查升级
		checkUpdate();
		AlphaAnimation animation = new AlphaAnimation(0.2f	, 1.0f);
		animation.setDuration(500);
		findViewById(R.id.rl_root_splash).startAnimation(animation);
		
	}
	
	protected void enterHome() {
		// TODO Auto-generated method stub
		 Intent it = new Intent(this, HomeActivity.class);
		 startActivity(it);
		 //关闭当前页
		 finish();
	}

	/**
	 *  检查新版本
	 */
	private void checkUpdate() {
		// TODO Auto-generated method stub
		
		new Thread(){
			public void run() {
				//URL http://localhost:8080/updateinfo.json
				Message mes = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {
					URL url = new URL(getString(R.string.serverurl));
					//联网
					HttpURLConnection con = (HttpURLConnection)url.openConnection();
					con.setRequestMethod("GET");
					con.setConnectTimeout(10000);
					int code = con.getResponseCode();
					if (code == 200) {
						//联网成功
						InputStream is = con.getInputStream();
						//把流转成String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "联网成功了 " + result);
						//json解析
						JSONObject obj = new JSONObject(result);
						String apkurl = (String)obj.get("apkurl");
						String  version = (String)obj.get("version");
						String description = (String)obj.get("description");
						
						//校验是否有新版本
						if (getVersionName().equals(version)) {
							//版本一致，没有新版本，进入主页面
							mes.what = ENTER_HOME;
						}else {
							//有新版本，弹出升级对话框
							mes.what = SHOW_UPDATE_DIALOG;
						}
					}
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					mes.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					mes.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					mes.what = JSON_ERROR;
					e.printStackTrace();
				}finally{
						
					long endTime = System.currentTimeMillis();
					long dTime = endTime - startTime;	
					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					handler.sendMessage(mes);
				}
			};
		}.start();
		
	}

	/**
	 * 得到app版本名称
	 * */
	private String getVersionName()
	{
		//管理手机apk
		PackageManager pm = getPackageManager();

		try {
			//得到指定apk的功能清单文件
			PackageInfo pInfo = pm.getPackageInfo(getPackageName(), 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

}
