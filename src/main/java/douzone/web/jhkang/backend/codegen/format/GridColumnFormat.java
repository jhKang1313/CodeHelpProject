package douzone.web.jhkang.backend.codegen.format;

import java.util.List;

import douzone.web.jhkang.backend.codegen.gen.ModelField;

public class GridColumnFormat {
	public String format = "";
	
	public GridColumnFormat(List<ModelField> modelFields){
		format =  "grid = dews.ui.grid(dewself.$grid, {\n"
				+ "  dataSource : dataSource,\n"
				+ "  editable : false,\n"
				+ "  checkable : false,\n"
				+ "  selectable : false,\n"
				+ "  autoBind : false,\n"
				+ "  columns : [\n";
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
			format += "    {\n"
					+ String.format("      field : '%s',\n", modelField.alias)
					+ String.format("      title : '%s',\n", modelField.desc)
					+ String.format("      align : '%s',\n", align)
					+ "      width : 100\n"
					+ "    },\n";
			
		}
		format = format.substring(0, format.length() -2);
		
		format += "\n"
				+ "  ],\n"
				+ "  dataBound : function(e){\n\n"
				+ "  },\n"
				+ "  change : function(e){\n\n"
				+ "  },\n"
				+ "  save : function(e){\n\n"
				+ "  },\n"
				+ "  rowAdd : function(e){\n\n"
				+ "  }\n"
				+ "});";
	}
}
