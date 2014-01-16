package cn.yakang.controler.util.file;

import java.io.File;

import android.content.Context;

public class FileContext {
	public static String getXmlFilePath(Context context){
		return context.getFilesDir().getAbsolutePath() + File.separator + "XML";
	}
}
