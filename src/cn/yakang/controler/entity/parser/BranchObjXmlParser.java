package cn.yakang.controler.entity.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import android.content.Context;
import cn.yakang.controler.entity.handleObj.BranchHandleObj;
import cn.yakang.controler.entity.handleObj.LeafHandleObj;
import cn.yakang.controler.entity.handleObj.Operation;
import cn.yakang.controler.util.file.FileTool;

public class BranchObjXmlParser extends HandleObjXmlParser {
	private boolean hasSubs;

	public BranchObjXmlParser(Context context) {
		super(context);
	}

	public boolean hasSubs(String fileName) {
		if (fileName == null) {
			return false;
		}
		Document document;
		try {
			if (!FileTool.isFileExist(xmlPath, fileName)) {
				return false;
			}
			document = reader.read(new File(xmlPath, fileName));
			List nodes = document.selectNodes("root/categories/category");
			for (Iterator iter = nodes.iterator(); iter.hasNext();) {
				Element tempEle = (Element) iter.next();
				Document tempDoc = tempEle.getDocument();
				if (tempDoc.selectSingleNode("root/categories/category/subs") != null) {
					hasSubs = true;
					break;
				}
			}
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		return hasSubs;
	}

	@Override
	protected List<BranchHandleObj> parser(Document document) {
		if (document == null) {
			return null;
		}
		Element root = document.getRootElement();
		Element operasEle = root.element("operations");
		Iterator operasIt = null;
		if (operasEle != null) {
			operasIt = operasEle.elementIterator();
		}

		List<Operation> operations = new ArrayList<Operation>();
		while (operasIt != null && operasIt.hasNext()) {
			Element operaEle = (Element) operasIt.next();
			String operaName = operaEle.attributeValue("name");
			String operaId = operaEle.attributeValue("id");
			operations.add(new Operation(operaName, operaId));
		}

		Element categoriesEle = root.element("categories");
		Iterator categoriesIt = null;
		if (categoriesEle != null) {
			categoriesIt = categoriesEle.elementIterator();
		}

		List<BranchHandleObj> categories = new ArrayList<BranchHandleObj>();
		while (categoriesIt != null && categoriesIt.hasNext()) {

			Element ele = (Element) categoriesIt.next();

			BranchHandleObj branchobj = new BranchHandleObj();
			String name = ele.attributeValue("name");
			String id = ele.attributeValue("id");
			branchobj.setOperations(operations);
			branchobj.setName(name);
			branchobj.setId(id);

			Element subOpera = ele.element("operations");
			Iterator subOperaIt = null;
			if (subOpera != null) {
				subOperaIt = subOpera.elementIterator();
			}

			List<Operation> operas = new ArrayList<Operation>();
			while (subOperaIt != null && subOperaIt.hasNext()) {
				Element operaEle = (Element) subOperaIt.next();
				String operaName = operaEle.attributeValue("name");
				String operaId = operaEle.attributeValue("id");
				operas.add(new Operation(operaName, operaId));
			}

			Element subsEle = ele.element("subs");
			Iterator subsIt = null;
			if (subsEle != null) {
				subsIt = subsEle.elementIterator();
			}

			List<LeafHandleObj> subs = new ArrayList<LeafHandleObj>();
			while (subsIt != null && subsIt.hasNext()) {
				Element e = (Element) subsIt.next();

				LeafHandleObj handleObj = new LeafHandleObj();
				String subName = e.attributeValue("name");
				String subId = e.attributeValue("id");
				String longitude = e.attributeValue("longitude");
				String latitude = e.attributeValue("latitude");
				handleObj.setOperations(operas);
				handleObj.setName(subName);
				handleObj.setId(subId);
				if (longitude != null && latitude != null) {
					Map<String, String> location = new HashMap<String, String>();
					location.put("longitude", longitude);
					location.put("latitude", latitude);
					handleObj.setLocation(location);
				}
				subs.add(handleObj);
			}
			branchobj.setSubs(subs);
			categories.add(branchobj);
		}
		return categories;
	}
}
