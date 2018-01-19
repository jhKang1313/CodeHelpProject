package douzone.web.jhkang.backend.codegen;

public class ApiParameter {
	public String key;
	public String desc;
	public String type;
	public String dataType;
	public String attrName;
	public ApiParameter(String attName, String key, String desc, String type, String dataType){
		this.attrName = attName;
		this.key = key;
		this.desc = desc;
		this.type = type;
		this.dataType = dataType;
	}
}
