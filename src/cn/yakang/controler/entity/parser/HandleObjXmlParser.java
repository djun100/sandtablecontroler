package cn.yakang.controler.entity.parser;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import android.content.Context;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.util.file.FileContext;
import cn.yakang.controler.util.file.FileTool;

public abstract class HandleObjXmlParser {
	protected SAXReader reader;
	protected String xmlPath;
	public HandleObjXmlParser(Context context){
		reader = new SAXReader();
		xmlPath = FileContext.getXmlFilePath(context);
	}
	public List<? extends IHandleObj> parser(String file){
		Document document = null;
		if(!FileTool.isFileExist(xmlPath, file)){
			return null;
		}
		try {
			document = reader.read(new File(xmlPath,file));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return parser(document);
	}
	public List<? extends IHandleObj> parser(InputStream in){
		Document document = null;
		try {
			document = reader.read(in);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return parser(document);
	}
	protected abstract List<? extends IHandleObj> parser(Document document);
}
