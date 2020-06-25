package douzone.web.jhkang.backend.codegen.codehelp;

import java.util.ArrayList;
import java.util.List;

import douzone.web.jhkang.backend.codegen.db.DBConnector;
import douzone.web.jhkang.backend.codegen.format.ModelFormat;
import douzone.web.jhkang.backend.codegen.gen.ModelCodeGenerator;
import douzone.web.jhkang.backend.codegen.gen.ModelField;
import douzone.web.jhkang.backend.codegen.parser.Parser;

public class CodeHelpGen {
	public String getModelCode(Parser parser, DBConnector dbCon, CodeHelpProperty codeHelpProperty) throws Exception {
		List<String> columns = parser.getColumns();
		List<ModelField> modelFields = new ArrayList<ModelField>();
		
		if(dbCon == null){
			for(String column : columns){
				column = column.indexOf('.') == -1 ? column : column.substring(column.indexOf('.') +1);
				if(!isDupleField(modelFields, column)){
					modelFields.add(new ModelField(column, column.toLowerCase(), "", column.toLowerCase(), "String", "{테이블명}", "{데이터타입}"));
				}
			}
		}
		else{
			for(String column : columns){
				String tableName = column.indexOf('.') == -1 ? "" : column.substring(0, column.indexOf('.'));
				column = column.indexOf('.') == -1 ? column : column.substring(column.indexOf('.') +1);
				ModelField modelField = null;
				
				if(tableName.equals("")){
					modelField = new ModelField();
					modelField.desc = "";
					modelField.columnDataType = "{데이터타입}";
					modelField.tableName = "{테이블명}";
					modelField.dataType = "String";
				}
				else{
					modelField = dbCon.query(tableName, column);
				}
				
				modelField.alias = column;
				modelField.name = column.toLowerCase();
				modelField.colName = column.toLowerCase();
				if(!isDupleField(modelFields, column)){
					modelFields.add(modelField);
					if(modelField.desc != null && !modelField.desc.equals("")) {
						codeHelpProperty.columns.put(modelField.alias, modelField.desc);
					} else {
						codeHelpProperty.columns.put(modelField.alias, modelField.alias);
					}
					
				}
			}
		}
		ModelFormat modelFormat = new ModelFormat();
		modelFormat.modelName = codeHelpProperty.model;
		modelFormat.modelDesc = "";
		modelFormat.modelField = modelFields;
		return new ModelCodeGenerator(modelFormat).codeGenerate();
	}
	public void setCodeHelpParam(Parser parser, CodeHelpProperty codeHelpProperty) {
		List<String> parameter = parser.getParameters();
		for(String param : parameter) {
			if(param.startsWith("P_FIXED") || param.equals("P_COMPANY_CD")) {
				continue;
			}
			codeHelpProperty.params.put(param.replaceAll("^P_", "").toLowerCase(), "");
		}
	}
	public boolean isDupleField(List<ModelField> modelFields, String newField){
		for(ModelField modelField : modelFields){
			if(modelField.alias.equals(newField)){
				return true;
			}
		}
		return false;
	}
}
