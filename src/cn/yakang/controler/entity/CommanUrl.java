package cn.yakang.controler.entity;

public interface CommanUrl {
	public static final String BASE = "http://192.168.0.216:8080/fxh_platform/st/sandTable.do?controlType=";
	public static final String MODULE = "module";
	public static final String WRY = "wry";
	public static final String SZPJ = "szpj";
	public static final String CYD = "cyd";
	public static final String ZTT = "ztt";
	public static final String SERCHINFO_URL = BASE + WRY;
	public static final String WATERQUL_URL = BASE + SZPJ;
	public static final String SAMPLE_POINT = BASE + CYD;
	public static final String SPECIAL_MAP = BASE + ZTT;
	public static String [] urls = {SERCHINFO_URL,WATERQUL_URL,SAMPLE_POINT,SPECIAL_MAP};
}
