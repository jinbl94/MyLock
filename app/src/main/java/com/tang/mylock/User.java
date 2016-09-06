package com.tang.mylock;

import android.content.Context;

/**
 * Created by tang on 5/26/16.
 */

public class User{
	private int id;
	private String name;
	private String year;
	private String month;
	private String date;
	private String addinfo;
	private String password;
	private int count;
	private boolean invalidate=true;

	User(Context context,int Id,String Name,String Year,String Month,String Date,String Addinfo,String Password,int Count){
		if(Id<0){
			Utils.Toast(context,context.getString(R.string.id_error));
			return;
		}
		if(Name.equals("")||Name.length()<6){
			Utils.Toast(context,context.getString(R.string.name_error));
			return;
		}
		if(Year.equals("")||Integer.parseInt(Year)<0||Integer.parseInt(Year)>9999){
			Utils.Toast(context,context.getString(R.string.year_error));
			return;
		}
		if(Month.equals("")||Integer.parseInt(Month)<0||Integer.parseInt(Month)>12){
			Utils.Toast(context,context.getString(R.string.month_error));
			return;
		}
		if(Date.equals("")||Integer.parseInt(Date)<0||Integer.parseInt(Date)>31){
			Utils.Toast(context,context.getString(R.string.date_error));
			return;
		}
		if(Addinfo.equals("")||Addinfo.length()<6){
			Utils.Toast(context,context.getString(R.string.addinfo_error));
			return;
		}
		if(Password.equals("")||Password.length()<6){
			Utils.Toast(context,context.getString(R.string.password_error));
			return;
		}
		invalidate=false;
		id=Id;
		name=Name;
		year=Year;
		month=Month;
		date=Date;
		addinfo=Addinfo;
		password=Password;
		count=Count;
	}

	int getId(){
		return id;
	}

	String getStringId(){
		return Integer.toString(id);
	}

	String getName(){
		return name;
	}

	String getYear(){
		return year;
	}

	String getMonth(){
		return month;
	}

	String getDate(){
		return date;
	}

	String getAddinfo(){
		return addinfo;
	}

	String getaddinfo(){
		return year+month+date+addinfo;
	}

	String getPassword(){
		return password;
	}

	int getCount(){
		return count;
	}

	boolean Invalidate(){
		return invalidate;
	}

	void incCount(){
		count+=1;
	}
}
