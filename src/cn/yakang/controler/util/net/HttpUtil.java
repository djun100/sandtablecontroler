package cn.yakang.controler.util.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	
	public static boolean testServerState(String url){
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpConnectionParams.setConnectionTimeout(httpget.getParams(),3000);
		HttpConnectionParams.setSoTimeout(httpget.getParams(), 3000);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getJsonString(String url) {
		String str = null;
		byte [] date = getByteArr(url);
		if(date != null){
			try {
				str = new String(date,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return str;
	}
	
	public static byte[] getByteArr(String url){
		InputStream in = getInputStream(url);
		if(in != null){
			byte [] date = new byte[128];
			int len = 0;
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				while((len = in.read(date)) != -1){
					baos.write(date, 0, len);
				}
				baos.flush();
				return baos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static InputStream getInputStream(String url){
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpConnectionParams.setConnectionTimeout(httpget.getParams(),3000);
		HttpConnectionParams.setSoTimeout(httpget.getParams(), 3000);
		InputStream in = null;
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if(entity != null) {
					in = response.getEntity().getContent();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
}
