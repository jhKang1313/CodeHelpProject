package douzone.web.jhkang.backend.codegen.gen;

public class ModelField {
	public String alias;
	public String name;
	public String desc;
	public String colName;
	public String dataType;
	public String columnDataType;
	public String tableName;
	
	public ModelField(){
		
	}
	public ModelField(String alias, String name, String desc, String colName, String dataType, String tableName, String columnDataType){
		this.alias = alias;
		this.name = name;
		this.desc = desc;
		this.colName = colName;
		this.dataType = dataType;
		this.tableName = tableName;
		this.columnDataType = columnDataType;
	}
}
