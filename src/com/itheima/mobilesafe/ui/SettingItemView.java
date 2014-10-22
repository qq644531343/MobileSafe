package com.itheima.mobilesafe.ui;

import com.itheima.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 用于复用设置中心的item
 *@author libo
 * 
 */
public class SettingItemView extends RelativeLayout {

	private CheckBox cb_status;
	private TextView tv_title;
	private TextView tv_desc;

	private String desc_on;
	private String desc_off;

	private void initView(Context context) {
		// TODO Auto-generated method stub
		View.inflate(context, R.layout.setting_item_view,
				SettingItemView.this);

		cb_status = (CheckBox) this.findViewById(R.id.cb_status);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
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
	public SettingItemView(Context context, AttributeSet attrs) {
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

		// System.out.println(attrs.getAttributeValue(0));
		// System.out.println(attrs.getAttributeValue(1));
		// System.out.println(attrs.getAttributeValue(2));
		// System.out.println(attrs.getAttributeValue(3));
		// System.out.println(attrs.getAttributeValue(4));
		// System.out.println(attrs.getAttributeValue(5));
	}

	public SettingItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 检验组合控件是否选中
	 */
	public boolean isChecked() {
		return cb_status.isChecked();
	}

	/**
	 * 设置状态是否选中
	 */
	public void setChecked(boolean checked) {
		if (checked) {
			setDesc(desc_on);
		}else {
			setDesc(desc_off);
		}
		cb_status.setChecked(checked);
	}

	/**
	 * 设置组合控件的描述信息
	 */
	public void setDesc(String text) {
		tv_desc.setText(text);
	}

}
