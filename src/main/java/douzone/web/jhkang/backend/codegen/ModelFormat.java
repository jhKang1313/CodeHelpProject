package douzone.web.jhkang.backend.codegen;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ModelFormat{
	public String modelName;
	public String modelDesc;
	List<ModelField> modelField;
	
	public String format = ""; 
	
	public ModelFormat(){
		
	}
						  
	public ModelFormat(String modelName, String modelDesc, List<ModelField> modelField){
		this.modelName = modelName;
		this.modelDesc = modelDesc;
		this.modelField = modelField;
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		
		format += "/*\n"
				+ "최초 작성자 : {이름}\n"
				+ String.format("최초 작성일 : %s\n", sdf.format(date))
				+ "설    명 : \n"
				+ "************************************************\n"
				+ "* 코드 수정 History\n"
				+ "* \n"
				+ "************************************************\n"
				+ "*/\n\n"
				
				+ "/**\n"
				+ " * @brief {주사용테이블}\n"
				+ " */\n"
				+ String.format("@DzModel(name=\"%s\", desc=\"\")\n", modelName, modelDesc)
				+ String.format("public class %s {\n", modelName);
		for(ModelField field : modelField){
			format += "\t/**\n"
					+ String.format("\t * - ***column*** : %s.%s - (%s)\n", field.tableName, field.alias, field.columnDataType)
					+ String.format("\t * - ***model key*** : %s\n", field.alias)
					+ String.format("\t * - ***description*** : %s\n", field.desc)
					+ "\t*/\n"
					+ String.format("\t@SerializedName(\"%s\")\n", field.alias) 
					+ String.format("\t@DzModelField(name=\"%s\", desc=\"%s\", colName=\"%s\")\n", field.name, field.desc, field.colName)
					+ String.format("\tprivate %s %s;\n", field.dataType, field.name);
		}
		format += "}";
	}
}
