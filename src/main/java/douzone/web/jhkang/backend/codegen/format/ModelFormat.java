package douzone.web.jhkang.backend.codegen.format;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import douzone.web.jhkang.backend.codegen.gen.ModelField;

public class ModelFormat{
	public String modelName;
	public String modelDesc;
	public List<ModelField> modelField;
	
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
			format += "    /**\n"
					+ String.format("     * - ***column*** : %s.%s - (%s)\n", field.tableName, field.alias, field.columnDataType)
					+ String.format("     * - ***model key*** : %s\n", field.alias)
					+ String.format("     * - ***description*** : %s\n", field.desc)
					+ "    */\n"
					+ String.format("    @SerializedName(\"%s\")\n", field.alias) 
					+ String.format("    @DzModelField(name=\"%s\", desc=\"%s\", colName=\"%s\")\n", field.name.toLowerCase(), field.desc, field.colName.toUpperCase())
					+ String.format("    private %s %s;\n", field.dataType, field.name);
		}
		format += "}";
	}
}
