/**
 * 	功能：
 * 	1，显示logo及启动页
 * 2，应用程序初始化
 * 3，检查版本信息
 * 4，检查版权
 */

package com.itheima.mobilesafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.anim;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.utils.StreamTools;

public class SplashActivity extends Activity {
	
	protected static final String TAG = "SplashActivity";
	
	protected static final int ENTER_HOME = 1;
	protected static final int SHOW_UPDATE_DIALOG = 0;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR = 4;
	
	private TextView tv_splash_version;
	private TextView tv_splash_updateinfo;
	
	private String apkurl;
	private String  version;
	private String description;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.i(TAG, "升级检测结果: "+msg.what);
			switch (msg.what ) {
			case SHOW_UPDATE_DIALOG:
				showUpdateDialog();
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
		tv_splash_updateinfo = (TextView)findViewById(R.id.tv_splash_updateinfo);
		
		//检查升级
		checkUpdate();
		AlphaAnimation animation = new AlphaAnimation(0.2f	, 1.0f);
		animation.setDuration(500);
		findViewById(R.id.rl_root_splash).startAnimation(animation);
		
	}
	
	/**
	 * 	弹出升级对话框
	 */
	protected void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示升级");
		builder.setMessage(description);
		builder.setPositiveButton("立刻升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//下载apk，并替换安装
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					//sdcard存在
					//使用afinal框架下载apk
					FinalHttp finalHttp = new FinalHttp();
					tv_splash_updateinfo.setVisibility(View.VISIBLE);
					Log.i(TAG, "apk url: " + apkurl);
					finalHttp.download(apkurl, Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe2.0.apk",
							new AjaxCallBack<File>() {

								@Override
								public void onFailure(
										Throwable t,
										int errorNo,
										String strMsg) {
									// TODO Auto-generated method stub
									t.printStackTrace();
									Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_LONG).show();
									
									super.onFailure(t, errorNo, strMsg);
								}

								@Override
								public void onLoading(
										long count,
										long current) {
									// TODO Auto-generated method stub
									super.onLoading(count, current);
									int progress = (int) (current*100/count);
									tv_splash_updateinfo.setText("下载进度: " + progress + "%");
								}

								@Override
								public void onSuccess(
										File t) {
									// TODO Auto-generated method stub
									Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_LONG).show();
									installAPK(t);
									super.onSuccess(t);
								}
								//安装apk
								private void installAPK(
										File t) {
									// TODO Auto-generated method stub
									Intent it = new Intent();
									it.setAction("android.intent.action.VIEW");
									it.addCategory("android.intent.category.DEFAULT");
									it.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
									startActivity(it);
								}
						
					});
					
				}else {
					Toast.makeText(SplashActivity.this, "没有sdcard，请安装后再试", Toast.LENGTH_LONG).show();
					return;
				}
			}
		});
		
		builder.setNegativeButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				enterHome();
			}
		});
		builder.show();
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
						 apkurl = (String)obj.get("apkurl");
						 version = (String)obj.get("version");
						 description = (String)obj.get("description");
						
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
