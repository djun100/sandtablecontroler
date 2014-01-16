package cn.yakang.controler.entity.parser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import android.content.Context;

import cn.yakang.controler.entity.handleObj.LeafHandleObj;
import cn.yakang.controler.entity.handleObj.Operation;
import cn.yakang.controler.util.file.FileTool;

public class LeafObjXmlParser extends HandleObjXmlParser{
	public LeafObjXmlParser(Context context) {
		super(context);
	}

	protected List<LeafHandleObj> parser(Document document){
		Element root = document.getRootElement();
		Element operasEle = root.element("operations");
		Iterator operasIt = null;
		if(operasEle != null){
			operasIt = operasEle.elementIterator();
		}
		
		List<Operation> operations = new ArrayList<Operation>();
		while(operasIt != null && operasIt.hasNext()){
			Element operaEle = (Element) operasIt.next();
			String operaName = operaEle.attributeValue("name");
			String operaId = operaEle.attributeValue("id");
			operations.add(new Operation(operaName,operaId));
		}
		
		Element subsEle = root.element("subs");
		Iterator subsIt = null;
		if(subsEle != null){
			subsIt = subsEle.elementIterator();
		}
		
		List<LeafHandleObj> subs = new ArrayList<LeafHandleObj>();
		while(subsIt != null && subsIt.hasNext()){
			Element ele = (Element) subsIt.next();
			
			LeafHandleObj handleObj = new LeafHandleObj();
			String name = ele.attributeValue("name");
			String id = ele.attributeValue("id");
			handleObj.setOperations(operations);
			handleObj.setName(name);
			handleObj.setId(id);
			subs.add(handleObj);
		}
		
		return subs;
	}
	
}
