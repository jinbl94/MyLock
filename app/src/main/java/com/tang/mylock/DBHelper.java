package com.tang.mylock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by tang on 5/26/16.
 */

public class DBHelper extends SQLiteOpenHelper{
	public static final String DATABASE_NAME="Users.db";
	public static final String USERS_TABLE_NAME="users";
	public static final String USERS_COLUMN_ID="id";
	public static final String USERS_COLUMN_NAME="name";
	public static final String USERS_COLUMN_BIRTHYEAR="birthyear";
	public static final String USERS_COLUMN_BIRTHMONTH="birthmonth";
	public static final String USERS_COLUMN_BIRTHDATE="birthdate";
	public static final String USERS_COLUMN_ADDINFO="addinfo";
	public static final String USERS_COLUMN_PASSWORD="password";
	public static final String USERS_COLUMN_COUNT="count";
	private Context mContext;

	public DBHelper(Context context){
		super(context,DATABASE_NAME,null,1);
		mContext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(
				"create table users "+
						"(id integer primary key,"+
						"name text,"+
						"birthyear text,"+
						"birthmonth text,"+
						"birthdate text,"+
						"addinfo text,"+
						"password text,"+
						"count integer)"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
		db.execSQL("drop table if exists users");
		onCreate(db);
	}

	public boolean insertUser(User user){
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues contentValues=new ContentValues();
		contentValues.put("name",user.getName());
		contentValues.put("birthyear",user.getYear());
		contentValues.put("birthmonth",user.getMonth());
		contentValues.put("birthdate",user.getDate());
		contentValues.put("addinfo",user.getAddinfo());
		contentValues.put("password",user.getPassword());
		contentValues.put("count",0);
		db.insert("users",null,contentValues);
		return true;
	}

	public Cursor getData(int id){
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor res=db.rawQuery("select * from users where id="+id+"",null);
		return res;
	}

	public User getUser(int id){
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor res=db.rawQuery("select * from users where id="+id+"",null);
		res.moveToFirst();
		return new User(
				mContext,
				res.getInt(res.getColumnIndex(USERS_COLUMN_ID)),
				res.getString(res.getColumnIndex(USERS_COLUMN_NAME)),
				res.getString(res.getColumnIndex(USERS_COLUMN_BIRTHYEAR)),
				res.getString(res.getColumnIndex(USERS_COLUMN_BIRTHMONTH)),
				res.getString(res.getColumnIndex(USERS_COLUMN_BIRTHDATE)),
				res.getString(res.getColumnIndex(USERS_COLUMN_ADDINFO)),
				res.getString(res.getColumnIndex(USERS_COLUMN_PASSWORD)),
				res.getInt(res.getColumnIndex(USERS_COLUMN_COUNT)));
	}

	public int numberOfRows(){
		SQLiteDatabase db=this.getReadableDatabase();
		int numRows=(int)DatabaseUtils.queryNumEntries(db,USERS_TABLE_NAME);
		return numRows;
	}

	public boolean updateUser(User user){
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues contentValues=new ContentValues();
		contentValues.put("name",user.getName());
		contentValues.put("birthyear",user.getYear());
		contentValues.put("birthmonth",user.getMonth());
		contentValues.put("birthdate",user.getDate());
		contentValues.put("addinfo",user.getAddinfo());
		contentValues.put("password",user.getPassword());
		contentValues.put("count",0);
		db.update("users",contentValues,"id = ? ",new String[]{user.getStringId()});
		return true;
	}

	public Integer deleteUser(Integer id){
		SQLiteDatabase db=this.getWritableDatabase();
		return db.delete("users","id = ? ",new String[]{Integer.toString(id)});
	}

	public ArrayList<User> getAllUsers(){
		ArrayList<User> array_list=new ArrayList<User>();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor res=db.rawQuery("select * from users",null);
		res.moveToFirst();

		while(res.isAfterLast()==false){
			array_list.add(new User(mContext,
					res.getInt(res.getColumnIndex(USERS_COLUMN_ID)),
					res.getString(res.getColumnIndex(USERS_COLUMN_NAME)),
					res.getString(res.getColumnIndex(USERS_COLUMN_BIRTHYEAR)),
					res.getString(res.getColumnIndex(USERS_COLUMN_BIRTHMONTH)),
					res.getString(res.getColumnIndex(USERS_COLUMN_BIRTHDATE)),
					res.getString(res.getColumnIndex(USERS_COLUMN_ADDINFO)),
					res.getString(res.getColumnIndex(USERS_COLUMN_PASSWORD)),
					res.getInt(res.getColumnIndex(USERS_COLUMN_COUNT))));
			res.moveToNext();
		}
		return array_list;
	}
}