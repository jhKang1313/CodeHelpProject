package douzone.web.jhkang.backend.codegen;

import java.util.List;

public class GridColumnFormat {
	public String format = "";
	
	public GridColumnFormat(List<ModelField> modelFields){
		for(ModelField modelField : modelFields){
			String align;
			switch(modelField.dataType){
			case "String" :
				align = "left";
				break;
			case "BigDecimal" :
				align = "right";
				break;
			case "Date" :
				align = "center";
				break;
			default : 
				align = "left";
				break;
			}
			format += "{\n"
					+ String.format("  field : '%s',\n", modelField.alias)
					+ String.format("  title : '%s'\n", modelField.desc)
					+ String.format("  align : '%s'\n", align)
					+ "  width : 100\n"
					+ "},\n";
			
		}
		format = format.substring(0, format.length() -2);
	}
}
