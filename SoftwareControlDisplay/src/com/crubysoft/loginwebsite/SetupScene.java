package com.crubysoft.loginwebsite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class SetupScene extends Activity implements OnTouchListener, OnClickListener{

	private EditText urlEditTxt, passwdEditTxt, refreshSecondsEditTxt, touchSecondsEditTxt;
	private int mScreenW, mScreenH;
	private static String PASSWD = "00000";
	RadioButton cd_internet_radio, sync_dropbox_radio, ctrl_url_radio;
	private SharedPreferences prvPref;
	private SharedPreferences.Editor prevEditor;
	private static String customUrl = null, password = null, refreshSecs = null, touchSecs = null;
	private static String ServerURL = "http://www.aaronvanderzwan.com/maximage/examples/basic.html";
	private static int CD_SETTING_STATE;
	private AlertDialog.Builder updateDlgBuilder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.setup);

		prvPref = PreferenceManager.getDefaultSharedPreferences(this);
		prevEditor = prvPref.edit();
		customUrl = prvPref.getString("CUSTOM_URL", "http://");
		password = prvPref.getString("PASSWORD", "");
		refreshSecs = prvPref.getString("REFRESH_SECS", "0");
		touchSecs = prvPref.getString("TOUCH_SECS", "5");
		CD_SETTING_STATE = prvPref.getInt("CD_SETTING_STATE", 0);
		
		updateDlgBuilder = new AlertDialog.Builder(this);
		
		mScreenW = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		mScreenH = getWindow().getWindowManager().getDefaultDisplay().getHeight();

		urlEditTxt = (EditText)findViewById(R.id.urlEditTxt);
		passwdEditTxt = (EditText)findViewById(R.id.passwdEditTxt);
		refreshSecondsEditTxt = (EditText)findViewById(R.id.refreshSecondsEditTxt);
		touchSecondsEditTxt = (EditText)findViewById(R.id.touchSecondsEditTxt);
		
		urlEditTxt.setFocusable(false);
		passwdEditTxt.setFocusable(false);
		refreshSecondsEditTxt.setFocusable(false);
		touchSecondsEditTxt.setFocusable(false);
		
		Button btnUpgrade = (Button)findViewById(R.id.upgradeBtn);
		Button btnUpdate = (Button)findViewById(R.id.updateBtn);
		Button btnNext = (Button)findViewById(R.id.nextBtn);
		
		cd_internet_radio = (RadioButton)findViewById(R.id.cd_internet_radio);
		sync_dropbox_radio = (RadioButton)findViewById(R.id.sync_dropbox_radio);
		ctrl_url_radio = (RadioButton)findViewById(R.id.ctrl_url_radio);
		
		urlEditTxt.setWidth((int)(mScreenW*0.2));
		passwdEditTxt.setWidth((int)(mScreenW*0.2));
		
		urlEditTxt.setOnTouchListener(this);
		passwdEditTxt.setOnTouchListener(this);
		refreshSecondsEditTxt.setOnTouchListener(this);
		touchSecondsEditTxt.setOnTouchListener(this);
		
		passwdEditTxt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(passwdEditTxt.getText().toString().length() < 1)
					passwdEditTxt.setBackgroundResource(R.drawable.setup_pass);
				else
					passwdEditTxt.setBackgroundResource(R.drawable.edit_bg);
			}
		});
		btnUpgrade.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		
		cd_internet_radio.setTag(1);
		sync_dropbox_radio.setTag(2);
		ctrl_url_radio.setTag(3);
		
		cd_internet_radio.setOnTouchListener(this);
		sync_dropbox_radio.setOnTouchListener(this);
		ctrl_url_radio.setOnTouchListener(this);
		
		if(customUrl != null || !customUrl.isEmpty())
			urlEditTxt.setText(customUrl);
		if(refreshSecs != null || !refreshSecs.isEmpty())
			refreshSecondsEditTxt.setText(refreshSecs);
//		if(password != null || !password.isEmpty())
//			passwdEditTxt.setText(password);
		if(touchSecs != null || !touchSecs.isEmpty())
			touchSecondsEditTxt.setText(touchSecs);
		
		setRadioState(CD_SETTING_STATE);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int tag = Integer.parseInt(String.valueOf(v.getTag()));
		if(tag < 200)
			setRadioState(tag);
		else
			switch(v.getId()){
			case R.id.urlEditTxt:
				urlEditTxt.setFocusableInTouchMode(true);
				break;
			case R.id.passwdEditTxt:
				passwdEditTxt.setFocusableInTouchMode(true);
				break;
			case R.id.refreshSecondsEditTxt:
				refreshSecondsEditTxt.setFocusableInTouchMode(true);
				break;
			case R.id.touchSecondsEditTxt:
				touchSecondsEditTxt.setFocusableInTouchMode(true);
				break;
			default:
				urlEditTxt.setFocusableInTouchMode(false);
				urlEditTxt.setFocusable(false);
				passwdEditTxt.setFocusableInTouchMode(false);
				passwdEditTxt.setFocusable(false);
				refreshSecondsEditTxt.setFocusableInTouchMode(false);
				refreshSecondsEditTxt.setFocusable(false);
				touchSecondsEditTxt.setFocusableInTouchMode(false);
				touchSecondsEditTxt.setFocusable(false);
				break;
			}
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.upgradeBtn:
			break;
		case R.id.updateBtn:
			String tmpPasswd = passwdEditTxt.getText().toString();
//			if(tmpPasswd.length() != 10)
//				updateDlgBuilder.setTitle("Try Again!").
//						setMessage("The password field is incorrect.").
//						setCancelable(false).setPositiveButton("OK", null).create().show();
//			else if(tmpPasswd.isEmpty())
//				password = PASSWD;
//			else{
				password = tmpPasswd;
//			}
			
			String tmpTouchSecs = touchSecondsEditTxt.getText().toString();
			if(tmpTouchSecs.length() < 1 || tmpTouchSecs.isEmpty()){
				updateDlgBuilder.setTitle("Try Again!").
				setMessage("The Touch seconds field is incorrect.").
				setCancelable(false).setPositiveButton("OK", null).create().show();	
				break;
//				touchSecs = "5";
			}else
				touchSecs = tmpTouchSecs;

			Log.e("PASSWD", "touchSecs: " + touchSecs);
			prevEditor.putString("PASSWORD", password);
			prevEditor.putString("TOUCH_SECS", touchSecs);
			prevEditor.commit();

			break;
		case R.id.nextBtn:
			switch(CD_SETTING_STATE){
			case 1:
				startActivity(new Intent(SetupScene.this, WebviewScene.class));
				break;
			case 2:
				startActivity(new Intent(SetupScene.this, DropBoxScene.class));
				break;
			case 3:
				updateDlgBuilder.setTitle("Try Again!").
				setMessage("The field is incorrect. \n Be sure you're using web url for you").
				setCancelable(false).setPositiveButton("OK", null);
				
				String tmpUrl = urlEditTxt.getText().toString();
				if(tmpUrl.length() < 1 || !tmpUrl.contains("http://")){
					updateDlgBuilder.create().show();
				}else{
					String realUrl = tmpUrl.substring(tmpUrl.toLowerCase().lastIndexOf("http://") + 7); 
					if(realUrl.isEmpty() || realUrl == null){
						updateDlgBuilder.create().show();
						break;
					}else{
						customUrl = tmpUrl;
						prevEditor.putString("serverurl", customUrl);
						prevEditor.commit();
						String tmpRefreshSecs = refreshSecondsEditTxt.getText().toString();
						if(tmpRefreshSecs.length() < 1 || tmpRefreshSecs.isEmpty()){
							updateDlgBuilder.setTitle("Try Again!").
							setMessage("The field is incorrect. \n Be sure you're using fresh seconds for you").
							setCancelable(false).setPositiveButton("OK", null).create().show();
							break;
						}else
							refreshSecs = tmpRefreshSecs;
						
						startActivity(new Intent(SetupScene.this, WebviewScene.class));
					}
				}
			}
			Log.e("dddd", "refreshSecs@@:" + refreshSecs  );
			Log.e("dddd", "CD_SETTING_STATE@@:" + CD_SETTING_STATE);
			prevEditor.putString("CUSTOM_URL", customUrl);
			prevEditor.putString("REFRESH_SECS", refreshSecs);
			prevEditor.putInt("CD_SETTING_STATE", CD_SETTING_STATE);
			prevEditor.commit();
//			finish();
			break;			
		default:
			break;
		}
	}
	
	private void setRadioState(int value){
		switch(value){
		case 0:
		case 1:
			cd_internet_radio.setChecked(true);
			cd_internet_radio.setSelected(true);
			cd_internet_radio.setFocusable(true);
			sync_dropbox_radio.setChecked(false);
			ctrl_url_radio.setChecked(false);
			urlEditTxt.setEnabled(false);
			refreshSecondsEditTxt.setEnabled(false);
			CD_SETTING_STATE = 1;
			break;
		case 2:
			cd_internet_radio.setChecked(false);
			sync_dropbox_radio.setChecked(true);
			sync_dropbox_radio.setSelected(true);
			sync_dropbox_radio.setFocusable(true);
			ctrl_url_radio.setChecked(false);
			urlEditTxt.setEnabled(false);
			refreshSecondsEditTxt.setEnabled(false);
			CD_SETTING_STATE = 2;
			break;
		case 3:
			cd_internet_radio.setChecked(false);
			sync_dropbox_radio.setChecked(false);
			ctrl_url_radio.setChecked(true);
			ctrl_url_radio.setSelected(true);
			ctrl_url_radio.setFocusable(true);

			urlEditTxt.setEnabled(true);
			refreshSecondsEditTxt.setEnabled(true);
			CD_SETTING_STATE = 3;
			break;
		default:
			break;
		}
	}
}
