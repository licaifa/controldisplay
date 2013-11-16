package com.crubysoft.loginwebsite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/*
 * @crubysoft cpanda
 */
public class LoginWebsite extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals("android.intent.action.BOOT_COMPLETED")){
			Intent mainAct = new Intent(context, WebviewScene.class);
			mainAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mainAct);
		}
	}

}
