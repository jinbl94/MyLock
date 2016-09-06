package com.tang.mylock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class UserInfo extends AppCompatActivity{
	private DBHelper dbHelper;
	private EditText text_name;
	private EditText text_year;
	private EditText text_month;
	private EditText text_date;
	private EditText text_addinfo;
	private EditText text_password;
	private EditText text_password_check;
	private Button button_edit;
	private Button button_unlock;
	private Button button_share;
	private Button button_save;
	private Button button_delete;
	private LinearLayout server_info;
	private EditText text_ip;
	private EditText text_port;
	private int id_To_Update=0;
	private User user;
	private final static String INITIALIZE="initialize";
	private final static String INITFINISHED="initfinished";
	private final static String SUCCESS="success";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		text_name=(EditText)findViewById(R.id.edit_name);
		text_year=(EditText)findViewById(R.id.edit_year);
		text_month=(EditText)findViewById(R.id.edit_month);
		text_date=(EditText)findViewById(R.id.edit_date);
		text_addinfo=(EditText)findViewById(R.id.edit_addinfo);
		text_password=(EditText)findViewById(R.id.edit_password);
		text_password_check=(EditText)findViewById(R.id.edit_password_check);
		button_edit=(Button)findViewById(R.id.button_edit);
		button_unlock=(Button)findViewById(R.id.button_unlock);
		button_share=(Button)findViewById(R.id.button_share);
		button_save=(Button)findViewById(R.id.button_save);
		button_delete=(Button)findViewById(R.id.button_delete);
		server_info=(LinearLayout)findViewById(R.id.server_info);
		text_ip=(EditText)findViewById(R.id.text_ip);
		text_port=(EditText)findViewById(R.id.text_port);
	}

	@Override
	protected void onResume(){
		super.onResume();
		dbHelper=new DBHelper(this);

		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			id_To_Update=extras.getInt("id");
			if(id_To_Update>0){
				button_save.setFocusable(false);
				button_save.setVisibility(View.INVISIBLE);
				button_delete.setFocusable(false);
				button_delete.setVisibility(View.INVISIBLE);

				user=dbHelper.getUser(id_To_Update);

				text_name.setText(user.getName());
				text_name.setFocusable(false);
				text_name.setClickable(false);

				text_year.setText(user.getYear());
				text_year.setFocusable(false);
				text_year.setClickable(false);

				text_month.setText(user.getMonth());
				text_month.setFocusable(false);
				text_month.setClickable(false);

				text_date.setText(user.getDate());
				text_date.setFocusable(false);
				text_date.setClickable(false);

				text_addinfo.setText(user.getAddinfo());
				text_addinfo.setFocusable(false);
				text_addinfo.setClickable(false);

				text_password.setText(user.getPassword());
				text_password.setFocusable(false);
				text_password.setClickable(false);

				text_password_check.setText(user.getPassword());
				text_password_check.setFocusable(false);
				text_password_check.setClickable(false);
			}else{
				enableEdit();
			}
		}
	}

	private void enableEdit(){
		text_name.setFocusable(true);
		text_name.setClickable(true);
		text_name.setFocusableInTouchMode(true);

		text_year.setFocusable(true);
		text_year.setClickable(true);
		text_year.setFocusableInTouchMode(true);

		text_month.setFocusable(true);
		text_month.setClickable(true);
		text_month.setFocusableInTouchMode(true);

		text_date.setFocusable(true);
		text_date.setClickable(true);
		text_date.setFocusableInTouchMode(true);

		text_addinfo.setFocusable(true);
		text_addinfo.setClickable(true);
		text_addinfo.setFocusableInTouchMode(true);

		text_password.setFocusable(true);
		text_password.setClickable(true);
		text_password.setFocusableInTouchMode(true);

		text_password_check.setFocusable(true);
		text_password_check.setClickable(true);
		text_password_check.setFocusableInTouchMode(true);

		button_edit.setFocusable(false);
		button_edit.setVisibility(View.INVISIBLE);
		button_unlock.setFocusable(false);
		button_unlock.setVisibility(View.INVISIBLE);
		button_save.setFocusable(true);
		button_save.setVisibility(View.VISIBLE);
		button_share.setFocusable(false);
		button_share.setVisibility(View.INVISIBLE);
		if(id_To_Update<1){
			button_delete.setFocusable(false);
			button_delete.setVisibility(View.INVISIBLE);
		}
		server_info.setFocusable(false);
		server_info.setVisibility(View.INVISIBLE);
	}

	public void onEdit(View view){
		enableEdit();
	}

	public void onUnlock(View view){
		//todo: jump to unlock activity
		final String ip=text_ip.getText().toString();
		final int port=Integer.parseInt(text_port.getText().toString());
		if(ip.equals("")||port==0){
			Utils.Toast(this,getString(R.string.server_error));
			return;
		}
		new Thread(){
			@Override
			public void run(){
				try{
					Socket socket=new Socket(ip,port);
					String result;
					try{
						RC4 rc4=new RC4(user.getCount()+user.getaddinfo()+user.getPassword());
						Log.d("UserInfo",user.getCount()+user.getaddinfo()+user.getPassword());
						Log.d("UserInfo","Sending to: "+socket.getRemoteSocketAddress());
						DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
						DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
						dataOutputStream.writeUTF(user.getName());
						if(user.getName().equals("Guest")){
							//if use guest, simply send password by plaintext
							//todo: this is unsafe, use diffie-hellman to format a temp key later
							dataOutputStream.writeUTF(user.getPassword());
						}else{
							dataOutputStream.writeUTF(rc4.RC4parse(user.getName()));
						}
						result=dataInputStream.readUTF();
						if(result.equals(INITIALIZE)){
							//initialize user information
							try{
								String tmpKey=dataInputStream.readUTF();
								RC4 mRC4=new RC4(tmpKey);
								dataOutputStream.writeUTF(mRC4.RC4parse(user.getName()));
								dataOutputStream.writeUTF(mRC4.RC4parse(user.getaddinfo()));
								dataOutputStream.writeUTF(mRC4.RC4parse(user.getPassword()));
								if(dataInputStream.readUTF().equals(mRC4.RC4parse(INITFINISHED))){
									//todo: initialize finished
									System.out.println(getString(R.string.init_success));
									Utils.Toast(getApplicationContext(),"Touch again to unlock");
									dataOutputStream.writeUTF("bye");
								}else{
									//todo: initialize failed
									System.out.println(getString(R.string.init_failed));
								}
							}catch(IOException e){
								e.printStackTrace();
							}catch(Exception e){
								e.printStackTrace();
							}
						}else if(result.equals(SUCCESS)){
							//todo: unlock succeed
							user.incCount();
							dbHelper.updateUser(user);
							System.out.println(SUCCESS);
						}else {
							//todo: unlock failed
							System.out.println("Fail");
						}
						dataOutputStream.writeUTF("bye");
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						socket.close();
						Log.d("UserInfo","Socket closed");
					}
				}catch(UnknownHostException e1){
					e1.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void onSave(View view){
		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			if(text_password.getText().toString().equals(text_password_check.getText().toString())){
				user=new User(this,
						id_To_Update,
						text_name.getText().toString(),
						text_year.getText().toString(),
						text_month.getText().toString(),
						text_date.getText().toString(),
						text_addinfo.getText().toString(),
						text_password.getText().toString(),0);
				if(user.Invalidate()){
					return;
				}
			}else{
				Utils.Toast(this,getString(R.string.password_unmatched));
				return;
			}
			if(user.getId()>0){
					if(dbHelper.updateUser(user)){
						Utils.Toast(this,getString(R.string.updated));
						this.finish();
					}else{
						Utils.Toast(this,getString(R.string.update_failed));
					}
			}else{
				if(text_password.getText().toString().equals(text_password_check.getText().toString())){
					if(dbHelper.insertUser(user)){
						Utils.Toast(this,getString(R.string.inserted));
						this.finish();
					}else{
						Utils.Toast(this,getString(R.string.insert_failed));
					}
				}else{
					Utils.Toast(this,getString(R.string.password_unmatched));
				}
			}
		}
	}

	public void onDelete(View view){
		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			if(id_To_Update>0){
				if(dbHelper.deleteUser(id_To_Update)>0){
					Utils.Toast(this,getString(R.string.deleted));
					this.finish();
				}else{
					Utils.Toast(this,getString(R.string.delete_failed));
				}
			}else{
				Utils.Toast(this,getString(R.string.user_not_found));
			}
		}
	}

	public void onShare(View view){
		//todo: share user with others
		final String ip=text_ip.getText().toString();
		final int port=Integer.parseInt(text_port.getText().toString());
		new Thread(){
			@Override
			public void run(){
				try{
					Socket socket=new Socket(ip,port);
					String result;
					try{
						RC4 rc4=new RC4(user.getCount()+user.getaddinfo()+user.getPassword());
						String guest_encrypted=rc4.RC4parse("Guest");
						Log.d("UserInfo","Sending to: "+socket.getRemoteSocketAddress());
						DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
						DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
						//random number as temp key
						//todo: replace it with diffie-hellman later
						Random random=new Random();
						String tmpKey=Integer.toString(random.nextInt());
						dataOutputStream.writeUTF(tmpKey);
						rc4=new RC4(tmpKey);
						dataOutputStream.writeUTF(rc4.RC4parse("Guest"));
						dataOutputStream.writeUTF(rc4.RC4parse(guest_encrypted));
						result=dataInputStream.readUTF();
						if(result.equals(SUCCESS)){
							//todo: success
							System.out.println("Succeed");
						}else {
							//todo: failed
							System.out.println("Failed");
						}
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						socket.close();
						Log.d("UserInfo","Socket closed");
					}
				}catch(UnknownHostException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}.start();
	}
}
