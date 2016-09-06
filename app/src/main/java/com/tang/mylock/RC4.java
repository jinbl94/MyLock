package com.tang.mylock;

/**
 * Created by tang on 5/27/16.
 */

public class RC4{
	private static final int ARRAY_LENGTH=256;
	private static final int CHAR_MOD=256;

	private static char [] S=new char[ARRAY_LENGTH];
	private static char [] T=new char[ARRAY_LENGTH];

	RC4(String UserKey){
		char [] userKey=UserKey.toCharArray();
		for(int i=0;i<ARRAY_LENGTH;i++){
			S[i]=(char)i;
			T[i]=userKey[i%userKey.length];
		}
		int j=0,temp=0;
		for(int i=0;i<ARRAY_LENGTH;i++){
			j=(j+S[i]+T[i])%CHAR_MOD;
			temp=S[i];
			S[i]=S[j];
			S[j]=(char)temp;
		}
	}

	public static String RC4parse(String PlainText){
		int i=0,j=0;
		char temp;
		char [] plainText=PlainText.toCharArray();
		StringBuffer encryptText=new StringBuffer(plainText.length);
		for (int k=0;k<plainText.length;k++){
			i=(i+1)%CHAR_MOD;
			j=(j+S[i])%CHAR_MOD;
			temp=S[i];
			S[i]=S[j];
			S[j]=temp;
			temp=(char)(((int)(S[i]+S[j]))%CHAR_MOD);
			encryptText.append((char)(plainText[k]^S[temp]));
		}
		return encryptText.toString();
	}
}
