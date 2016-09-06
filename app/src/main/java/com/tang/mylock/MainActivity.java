package com.tang.mylock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

	ListView List_users;
	DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		List_users=(ListView)findViewById(R.id.list_users);
	}

	@Override
	protected void onResume(){
		super.onResume();
		dbHelper=new DBHelper(this);
		final ArrayList<User> array_list=dbHelper.getAllUsers();
		ArrayList<String> name_list=new ArrayList<>();
		for(int i=0;i<array_list.size();i++){
			name_list.add(i,(array_list.get(i)).getName());
		}
		ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,name_list);

		List_users.setAdapter(arrayAdapter);
		List_users.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
				int id_To_Search=(array_list.get(arg2)).getId();
				Bundle dataBundle=new Bundle();
				dataBundle.putInt("id",id_To_Search);
				Intent intent=new Intent(getApplicationContext(),UserInfo.class);
				intent.putExtras(dataBundle);
				startActivity(intent);
			}
		});
	}

	public void onAddUser(View view){
		Bundle dataBundle=new Bundle();
		dataBundle.putInt("id",0);
		Intent intent=new Intent(getApplicationContext(),UserInfo.class);
		intent.putExtras(dataBundle);
		startActivity(intent);
	}

	public void onDeleteAll(View view){
		dbHelper.onUpgrade(dbHelper.getWritableDatabase(),
				dbHelper.getWritableDatabase().getVersion(),
				dbHelper.getWritableDatabase().getVersion()+1);
		onResume();
	}

	public void onAddGuest(View view){
		startActivity(new Intent(this,AddGuest.class));
	}
}