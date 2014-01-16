package cn.yakang.controler.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import cn.yakang.controler.entity.CommanUrl;
import cn.yakang.controler.entity.handleObj.BranchHandleObj;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.entity.handleObj.LeafHandleObj;
import cn.yakang.controler.entity.parser.BranchObjXmlParser;
import cn.yakang.controler.entity.parser.LeafObjXmlParser;
import cn.yakang.controler.entity.parser.ModuleXmlParser;

public class SimulateTool {
	public static List<? extends IHandleObj> getSimulateData(Context context,String url){
		InputStream in = null;
		if(CommanUrl.SERCHINFO_URL.equals(url)){
			in = getInputStream(context, "wry");
			return getSearchInfoData(context,in);
		}else if(CommanUrl.WATERQUL_URL.equals(url)){
			in = getInputStream(context, "szpj");
			return getWaterQulData(context,in);
		}else if(CommanUrl.SAMPLE_POINT.equals(url)){
			in = getInputStream(context, "cyd");
			return getSamplePoint(context,in);
		}else if(CommanUrl.SPECIAL_MAP.equals(url)){
			in = getInputStream(context, "ztt");
			return getSpecialMap(context,in);
		}else if(CommanUrl.BASE.equals(url)){
			in = getInputStream(context, "module");
			return getModuleData(context, in);
		}
		return null;
	}
	public static InputStream getInputStream(Context context,String fileName){
		try {
			return context.getResources().getAssets().open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static List<? extends IHandleObj> getModuleData(Context context,InputStream in){
		return new ModuleXmlParser(context).parser(in);
	}
	private static List<? extends IHandleObj> getSearchInfoData(Context context,InputStream in){
		return new BranchObjXmlParser(context).parser(in);
	}
	private static List<? extends IHandleObj> getWaterQulData(Context context,InputStream in){
		return new LeafObjXmlParser(context).parser(in);
	}
	private static List<? extends IHandleObj> getSamplePoint(Context context,InputStream in){
		return new BranchObjXmlParser(context).parser(in);
	}
	private static List<? extends IHandleObj> getSpecialMap(Context context,InputStream in){
		return new BranchObjXmlParser(context).parser(in);
	}
}
