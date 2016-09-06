package com.tang.mylock;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tang on 5/27/16.
 */

public class Utils{
	public static void Toast(Context context,String string){
		//show message
		Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
	}
}
