package douzone.web.jhkang.backend.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Wizard {
	private InputMethod inputMethod;
	private OutputMethod outputMethod;
	private CodeGenerator codeGenerator;
	private Query saveQuery;
	private Query selectQuery;
	private Reader reader = new Reader();
	private String resultQuery = "";
	private Parser parser;
	private DBConnector dbCon = null;
	
	public Wizard(InputMethod inputMethod, OutputMethod outputMethod, Parser parser){
		this.inputMethod = inputMethod;
		this.outputMethod = outputMethod;
		this.parser = parser;
	}
	 
	public void run(){	
		outputMethod.display("......Code Gen.....(18.01.04.1)");
		if(inputMethod.doDBConnect()){
			dbCon = new OracleConnector();
		}
		switch(inputMethod.inputQueryType()){
		case SELECT:
			String selectFilePath = inputMethod.selectQueryFileInput();
			File selectFile = new File(selectFilePath);
			if(selectFile.exists()){
				String apiName = selectFile.getName().substring(0, selectFile.getName().indexOf("."));

				reader.read(selectFile);
				selectQuery = new Query(reader.getQuery());
				parser.parse(selectQuery);
				if(parser.getType() != QueryType.SELECT){
					outputMethod.display("select query error");
					return;
				}
				resultQuery = selectQueryGenerate(apiName);
			}
			else{
				outputMethod.display("file is not existing");
			}
			break;
		case UPDATE:
			List<String> saveFilePaths = inputMethod.saveQueryFileInput();
			List<String> saveFileNames = new ArrayList<String>();
			List<ModelField> modelFields = new ArrayList<ModelField>();
			
			List<QueryParameter> insertQueryParameters = null;
			List<QueryParameter> updateQueryParameters = null;
			List<QueryParameter> deleteQueryParameters = null;
			int queryOrder = 0;
			for(String saveFilePath : saveFilePaths){
				File saveFile = new File(saveFilePath);
				if(saveFile.exists()){
					saveFileNames.add(saveFile.getName().substring(0, saveFile.getName().indexOf(".")));
					
					reader.read(saveFile);
					saveQuery = new Query(reader.getQuery());
					parser.parse(saveQuery);
					switch(queryOrder){
					case 0:
						if(parser.getType() != QueryType.INSERT){
							outputMethod.display("insert query error");
							return;
						}
						insertQueryParameters = insertQueryAnalysis(modelFields);
						break;
					case 1:
						if(parser.getType() != QueryType.UPDATE){
							outputMethod.display("update query error");
							return;
						}
						updateQueryParameters = updateQueryAnalysis(modelFields);
						break;
					case 2:
						if(parser.getType() != QueryType.DELETE){
							outputMethod.display("delete query error");
							return;
						}
						deleteQueryParameters = deleteQueryAnalysis();
						break;
					default :
						
						break;
					}
				} else if(saveFilePath.equals("x")){
					saveFileNames.add("x");
				} else{
					outputMethod.display("file is not existing");
				}
				queryOrder++;
			}
			resultQuery = saveQueryGenerate(saveFileNames, insertQueryParameters, updateQueryParameters, deleteQueryParameters, modelFields);
			break;
		default :
			
			break;
		}
		outputMethod.display(resultQuery);
	}
	public String saveQueryGenerate(List<String> fileNames, List<QueryParameter> insertQueryParameters, List<QueryParameter> updateQueryParameters, List<QueryParameter> deleteQueryParameters, List<ModelField> modelFields){
		SaveApiFormat saveApiFormat = (SaveApiFormat)inputMethod.apiInfoInput(new SaveApiFormat());
		List<ApiParameter> apiParameter = new ArrayList<ApiParameter>();
		apiParameter.add(new ApiParameter("dataSource", "dataSource", "", "Body", saveApiFormat.usingModel));
		saveApiFormat.apiParameter = apiParameter;
		
		saveApiFormat.queryParameter = insertQueryParameters;
		saveApiFormat.updateParameter = updateQueryParameters;
		saveApiFormat.deleteParameter = deleteQueryParameters;
		saveApiFormat.apiDesc = "";
		saveApiFormat.httpMethod = "POST";
		saveApiFormat.query = saveApiFormat.url;
		
		codeGenerator = new SaveCodeGenerator(saveApiFormat, saveApiFormat.updateParameter, saveApiFormat.deleteParameter, fileNames);
		resultQuery = codeGenerator.codeGenerate();
		
		ModelFormat modelFormat = new ModelFormat();
		modelFormat.modelDesc = "";
		modelFormat.modelName = saveApiFormat.usingModel;
		modelFormat.modelField = modelFields;
		codeGenerator = new ModelCodeGenerator(modelFormat);
		
		resultQuery += "\n\n"
					+ codeGenerator.codeGenerate();
		
		return resultQuery;
	}
	
	public List<QueryParameter> insertQueryAnalysis(List<ModelField> modelFields){
		List<QueryParameter> insertQueryParameters = new ArrayList<QueryParameter>();
		List<String> parameters = parser.getParameters();
		List<String> columns = parser.getColumns();
		
		for(String parameter : parameters){
			String attr = parameter.replaceAll("^P_", "").toLowerCase();
			insertQueryParameters.add(new QueryParameter(parameter, true, attr));
		}
		
		for(String column : columns){
			if(!isDupleField(modelFields, column)){
				modelFields.add(new ModelField(column, column.toLowerCase(), "", column.toLowerCase(), "String", "{테이블명}", "{데이터타입}"));
			}
		}
		return insertQueryParameters;
	}
	public List<QueryParameter> updateQueryAnalysis(List<ModelField> modelFields){
		List<QueryParameter> updateQueryParameters = new ArrayList<QueryParameter>();
		List<String> parameters = parser.getParameters();
		List<String> columns = parser.getColumns();
		
		for(String parameter : parameters){
			String attr = parameter.replaceAll("^P_", "").toLowerCase();
			updateQueryParameters.add(new QueryParameter(parameter, true, attr));
		}
		
		for(String column : columns){
			if(!isDupleField(modelFields, column)){
				modelFields.add(new ModelField(column, column.toLowerCase(), "", column.toLowerCase(), "String", "{테이블명}", "{데이터타입}"));
			}
		}
		return updateQueryParameters;
		
	}
	public List<QueryParameter> deleteQueryAnalysis(){
		List<QueryParameter> deleteQueryParameters = new ArrayList<QueryParameter>();
		List<String> parameters = parser.getParameters();
		
		for(String parameter : parameters){
			String attr = parameter.replaceAll("^P_", "").toLowerCase();
			deleteQueryParameters.add(new QueryParameter(parameter, true, attr));
		}
		
		return deleteQueryParameters;
	}
	public boolean isDupleField(List<ModelField> modelFields, String newField){
		for(ModelField modelField : modelFields){
			if(modelField.alias.equals(newField)){
				return true;
			}
		}
		return false;
	}
	public String selectQueryGenerate(String apiName){
		List<String> parameters = parser.getParameters();
		
		ApiFormat apiFormat = new SelectApiFormat();
		
		apiFormat.url = apiName;
		apiFormat.usingModel = inputMethod.modelName();
		apiFormat.apiDesc = "";
		apiFormat.apiType = "";
		apiFormat.httpMethod = "GET";
		apiFormat.setQueryParameter(parameters);
		apiFormat.setApiParameter(parameters);
		apiFormat.query = apiFormat.url;
		apiFormat.returnType = "List";
		codeGenerator = new SelectCodeGenerator(apiFormat);
		resultQuery = codeGenerator.codeGenerate();
		
		// Create Model
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
				}
			}
		}
		ModelFormat modelFormat = new ModelFormat();
		modelFormat.modelName = apiFormat.usingModel;
		modelFormat.modelDesc = "";
		modelFormat.modelField = modelFields;
		codeGenerator = new ModelCodeGenerator(modelFormat);
		resultQuery += "\n\n"
					+ codeGenerator.codeGenerate();
		
		SchemaFormat schemaFormat = new SchemaFormat(modelFields);
		resultQuery += "\n" + schemaFormat.format; 
		
		GridColumnFormat gridColumnFormat = new GridColumnFormat(modelFields);
		resultQuery += "\n" + gridColumnFormat.format;
		return resultQuery;
	}
}
