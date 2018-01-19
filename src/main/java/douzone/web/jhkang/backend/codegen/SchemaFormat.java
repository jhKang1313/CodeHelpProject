package douzone.web.jhkang.backend.codegen;

import java.util.List;

public class SchemaFormat {
	public String format = "";
	
	public SchemaFormat(List<ModelField> modelField){
		/*format = "dataSource = dews.ui.dataSource(\"dataSource\", {\n"
				+ "  transport : {\n"
				+ "    read : {\n"
				+ "      url : dews.url.getApiUrl(\"\", \"\", \"\"),\n"
				+ "      data : function(){\n";*/
				
				
		for(ModelField field : modelField){
			format += String.format("{field : '%s'},\n", field.alias);
		}
		format = format.substring(0, format.length() - 2);
	}
}
