package cn.yakang.controler.entity.parser;

import java.util.List;

import android.content.Context;
import cn.yakang.controler.entity.handleObj.LeafHandleObj;

public interface Parser {
	public List<LeafHandleObj> parse(String resource);
}
