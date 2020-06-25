package douzone.web.jhkang.backend.codegen.codehelp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import douzone.web.jhkang.backend.codegen.db.DBConnector;
import douzone.web.jhkang.backend.codegen.db.OracleConnector;
import douzone.web.jhkang.backend.codegen.db.Query;
import douzone.web.jhkang.backend.codegen.db.QueryType;
import douzone.web.jhkang.backend.codegen.format.ModelFormat;
import douzone.web.jhkang.backend.codegen.gen.CodeGenerator;
import douzone.web.jhkang.backend.codegen.gen.ModelCodeGenerator;
import douzone.web.jhkang.backend.codegen.gen.ModelField;
import douzone.web.jhkang.backend.codegen.io.FileOutput;
import douzone.web.jhkang.backend.codegen.io.OutputMethod;
import douzone.web.jhkang.backend.codegen.parser.MyJSQLParser;
import douzone.web.jhkang.backend.codegen.parser.Parser;

public class QueryAnal {
	private DBConnector dbCon = new OracleConnector();
	private Parser parser = new MyJSQLParser();
	private CodeHelpProperty codeHelpProperty;
	private FileOutput fileOutput;
	public QueryAnal(CodeHelpProperty codeHelpProperty) {
		this.codeHelpProperty = codeHelpProperty;
		this.fileOutput = new FileOutput();
	}
	public void setInit(String query, boolean whereTrimFlag) {
		Query selectQuery = whereTrimFlag ? new Query(whereTrim(query)) : new Query(query);
		parser.parse(selectQuery);
		if(parser.getType() != QueryType.SELECT){
			return;
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
	public String whereTrim(String sqlText) {
		
		return sqlText.substring(0, sqlText.lastIndexOf("WHERE"));
	}
	public Map<String, String> getColumns() {
		List<ModelField> modelFields = new ArrayList<ModelField>();
		
		
		Map<String, String> columnMap = new HashMap<String, String>();
		List<String> columns = parser.getColumns();
		for(String column : columns) {
			String tableName = column.indexOf('.') == -1 ? "" : column.substring(0, column.indexOf('.'));
			column = column.indexOf('.') == -1 ? column : column.substring(column.indexOf('.') +1);
			ModelField modelField = dbCon.query(tableName, column);
			
			columnMap.put(column.toUpperCase(), modelField.desc);
			
			modelField.alias = column;
			modelField.alias = column;
			modelField.name = column.toLowerCase();
			modelField.colName = column.toLowerCase();
			if(!isDupleField(modelFields, column)){
				modelFields.add(modelField);
			}
		}
		
		ModelFormat modelFormat = new ModelFormat();
		modelFormat.modelName = codeHelpProperty.model;
		modelFormat.modelDesc = "";
		modelFormat.modelField = modelFields;
		CodeGenerator codeGenerator = new ModelCodeGenerator(modelFormat);
		fileOutput.display(codeHelpProperty.model + ".java", codeGenerator.codeGenerate());
		
		return columnMap; 
	}
}
