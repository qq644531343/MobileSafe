package com.itheima.mobilesafe.ui;

import com.itheima.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 用于复用设置中心的item
 *@author libo
 * 
 */
public class SettingClickView extends RelativeLayout {

	private TextView tv_title;
	private TextView tv_desc;

	private String desc_on;
	private String desc_off;

	private void initView(Context context) {
		// TODO Auto-generated method stub
		View.inflate(context, R.layout.setting_click_view,
				SettingClickView.this);

		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);

	}

	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 布局文件使用的时候调用
	 * 
	 * @param context
	 * @param attrs
	 */
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
		String title = attrs
				.getAttributeValue(
						"http://schemas.android.com/apk/res/com.itheima.mobilesafe",
						"title2");
		desc_on = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.itheima.mobilesafe",
				"desc_on");
		desc_off = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.itheima.mobilesafe",
				"desc_off");
		     
		tv_title.setText(title);
		tv_desc.setText(desc_on);

		// System.out.println(attrs.getAttributeValue(0));
		// System.out.println(attrs.getAttributeValue(1));
		// System.out.println(attrs.getAttributeValue(2));
		// System.out.println(attrs.getAttributeValue(3));
		// System.out.println(attrs.getAttributeValue(4));
		// System.out.println(attrs.getAttributeValue(5));
	}

	public SettingClickView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 设置组合控件的描述信息
	 */
	public void setDesc(String text) {
		tv_desc.setText(text);
	}
	
	/**
	 *	设置标题
	 */
	public void setTitle(String text) {
		tv_title.setText(text);
	}

}
