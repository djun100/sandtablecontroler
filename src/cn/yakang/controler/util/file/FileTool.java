package cn.yakang.controler.util.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileTool {
	/**
	 * @param fileName 文件名
	 * @param path 文件夹
	 * @param data
	 * @return
	 */
	public static boolean saveFile(String path,String fileName,InputStream in){
		boolean flag = false;
		if(in == null){
			return flag;
		}
		if(!isFilePathExist(path)){
			new File(path).mkdirs();
		}
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(path,fileName));
			int len = 0;
			byte [] data = new byte[128];
			while((len = in.read(data)) != -1){
				outputStream.write(data,0,len);
			}
			outputStream.flush();
			flag = true;
		} catch (FileNotFoundException e) {
			Log.e("saveFile", "文件不存在");
		} catch (IOException e) {
			Log.e("saveFile", "IO错误");
		}finally{
			if(outputStream != null){
				try {
					outputStream.close();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	/**
	 *以输入流的形式
	*将文件保存到该应用缓存路径下的文件夹名称为folder的文件夹下
	 * @param fileName
	 * @param path
	 * @return
	 */
	public static byte[] getByteArrFrom(String path,String fileName){
		byte [] result = null;
		InputStream in = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			in = getInputStreamFrom(path,fileName);
			byte [] data = new byte[128];
			int len = 0;
			while((len = in.read(data))!= -1){
				baos.write(data, 0, len);
			}
			result = baos.toByteArray();
		} catch (FileNotFoundException e) {
			Log.e("getInputStreamFrom", "文件不存在");
		} catch (IOException e) {
			Log.e("getByteArrFrom", "IO错误");
		}
		return result;
	}
	
	public static InputStream getInputStreamFrom(String path,String fileName){
		InputStream in = null;
		File file = new File(path,fileName);
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Log.e("getInputStreamFrom", "文件不存在");
		}
		return in;
	}
	/**
	 * @param fileName
	 * @param path
	 * @return
	 */
	public static boolean isFileExist(String path,String fileName){
		if(!isFilePathExist(path)){
			return false;
		}
		for(File file : new File(path).listFiles()){
			if(file.getName().equals(fileName)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isFilePathExist(String path){
		File file = new File(path);
		return file.exists();
	}
	
}
