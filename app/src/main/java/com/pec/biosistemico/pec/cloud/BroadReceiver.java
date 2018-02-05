package com.pec.biosistemico.pec.cloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		//Log.i("Script", "BroadReceiver");		
		Intent it = new Intent("REPLICACAO_IBS");
		//it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startService(it);
		
	}

}
