package com.tang.mylock;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class AddGuest extends AppCompatActivity{
	private TextView text_local_ip;
	private EditText text_local_port;
	private final static String SUCCESS="success";
	final DBHelper dbHelper=new DBHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_guest);
		text_local_port=(EditText)findViewById(R.id.text_local_port);
		text_local_ip=(TextView)findViewById(R.id.text_local_ip);
	}

	public void onReceive(View view){
		final int local_port=Integer.parseInt(text_local_port.getText().toString());
		try{
			text_local_ip.setText(getString(R.string.local_address)+getLocalIpAddress().toString());
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(local_port>0){
			new Thread(){
				String name,year="2016",month="05",date="28",addinfo="addinfo",password;
				@Override
				public void run(){
					try{
						ServerSocket serverSocket=new ServerSocket(local_port);
						serverSocket.setSoTimeout(10000);
						Socket client=serverSocket.accept();
						DataInputStream dataInputStream=new DataInputStream(client.getInputStream());
						DataOutputStream dataOutputStream=new DataOutputStream(client.getOutputStream());
						String tmpKey=dataInputStream.readUTF();
						RC4 rc4=new RC4(tmpKey);
						name=rc4.RC4parse(dataInputStream.readUTF());
						password=rc4.RC4parse(dataInputStream.readUTF());
						dataOutputStream.writeUTF(SUCCESS);
						client.close();
						serverSocket.close();
						User user=new User(getApplicationContext(),0,name,year,month,date,addinfo,password,0);
						dbHelper.insertUser(user);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}.start();
		}else {
			Utils.Toast(this,getString(R.string.port_error));
		}
	}

	private InetAddress getLocalIpAddress() throws UnknownHostException{
		WifiManager wifiManager = (WifiManager) getSystemService(android.content.Context.WIFI_SERVICE );
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return InetAddress.getByName(String.format("%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
				(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
	}
}
