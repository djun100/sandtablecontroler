package cn.yakang.controler.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validater {
	private static final String IP_PATTERN = 
	        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean ipAddressValidate(final String ip){          
	      Pattern pattern = Pattern.compile(IP_PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             
	}
}
