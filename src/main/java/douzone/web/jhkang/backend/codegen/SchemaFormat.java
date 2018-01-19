package douzone.web.jhkang.backend.codegen;

import java.util.List;

public class SchemaFormat {
	public String format = "";
	
	public SchemaFormat(List<ModelField> modelField, ApiFormat apiFormat){
		format = "dataSource = dews.ui.dataSource(\"dataSource\", {\n"
				+ "  grid : true,\n"
				+ "  transport : {\n"
				+ "    read : {\n"
				+ "      url : dews.url.getApiUrl(\"{module}\", \"{service}\", \"" + apiFormat.url + "\"),\n"
				+ "      data : function(){\n"
				+ "        return {\n";
		for(int i = 0 ; i < apiFormat.apiParameter.size(); i++){
			ApiParameter parameter = apiFormat.apiParameter.get(i);
			format += "          " + parameter.key + ": undefined";
			if(apiFormat.apiParameter.size() -1 != i){
				format += ",";
			}
			format += "\n";
		}
		format += "        }\n"
				+ "      }\n"
				+ "    }\n"
				+ "  },\n"
				+ "  error : function(e) {\n"
				+ "    if(e) {\n"
				+ "      dews.error(e.message);\n"
				+ "    }\n"
				+ "  },\n"
				+ "  schema : {\n"
				+ "    model : {\n"
				+ "      fields : [\n";
		for(int i = 0 ; i < modelField.size(); i++){
			ModelField field = modelField.get(i);
			format += String.format("        {field : '%s'}", field.alias);
			if(modelField.size() -1 != i){
				format += ",";
			}
			format += "\n";
		}
		format += "      ]\n"
				+ "    }\n"
				+ "  }\n"
				+ "});\n";
	}
}
