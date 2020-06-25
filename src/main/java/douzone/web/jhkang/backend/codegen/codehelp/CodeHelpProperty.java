package douzone.web.jhkang.backend.codegen.codehelp;

import java.util.HashMap;
import java.util.Map;

public class CodeHelpProperty {
	public String title;
	public String id;
	public String type;
	public String desc;
	public Map<String, String> params = new HashMap<String, String>();
	public Map<String, String> columns = new HashMap<String, String>();
	public String sqlText;
	public String model;
	
	public String templateClass;
	public String templateEnum;
	public Class<?> implClass;
	
	
}
