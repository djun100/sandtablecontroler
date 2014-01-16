package cn.yakang.controler.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {
	/** 
     * 获取字符串内的所有汉字的汉语拼音并大写每个字的首字母 
     * @param chinese 
     * @return 
     */  
    public static String spell(String chinese) {  
        if (chinese == null) {  
            return null;  
        }  
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写  
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不标声调  
        format.setVCharType(HanyuPinyinVCharType.WITH_V);// u:的声母替换为v  
        try {  
            StringBuilder sb = new StringBuilder();  
            for (int i = 0; i < chinese.length(); i++) {
            	char ch = chinese.charAt(i);
            	if((ch >= 97 && ch <= 122) || (ch >= 65 && ch <= 90)){
            		sb.append(ch);
            	}else{
            		String[] array = PinyinHelper.toHanyuPinyinStringArray(ch, format);
            		if (array == null || array.length == 0) {  
                        continue;  
                    }  
                    String s = array[0];// 不管多音字,只取第一个  
                    char c = s.charAt(0);  
                    //String pinyin = String.valueOf(c).toUpperCase().concat(s.substring(1));
                    String pinyin = String.valueOf(c);
                    sb.append(pinyin);  
            	}
            }
            return sb.toString();  
        } catch (BadHanyuPinyinOutputFormatCombination e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
}
