package cn.yakang.controler.entity.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import android.content.Context;
import cn.yakang.controler.entity.handleObj.IHandleObj;
import cn.yakang.controler.entity.handleObj.Module;

public class ModuleXmlParser extends HandleObjXmlParser {

	public ModuleXmlParser(Context context) {
		super(context);
	}

	@Override
	protected List<? extends IHandleObj> parser(Document document) {
		Element root = document.getRootElement();
		Element modulesEle = root.element("modules");
		Iterator moduleIt = null;
		if(modulesEle != null){
			moduleIt = modulesEle.elementIterator();
		}
		List<Module> modules = new ArrayList<Module>();
		while(moduleIt != null && moduleIt.hasNext()){
			Element moduleEle = (Element) moduleIt.next();
			String name = moduleEle.attributeValue("name");
			String id = moduleEle.attributeValue("id");
			String h = moduleEle.attributeValue("h");
			Module module = new Module();
			module.setName(name);
			module.setId(id);
			module.setHierarchy(h);
			modules.add(module);
		}
		return modules;
	}

}
