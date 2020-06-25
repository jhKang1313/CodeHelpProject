package douzone.web.jhkang.backend.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import douzone.web.jhkang.backend.codegen.codehelp.CodeHelpGen;
import douzone.web.jhkang.backend.codegen.codehelp.CodeHelpProperty;
import douzone.web.jhkang.backend.codegen.codehelp.format.Combo2KeywordTemplate;
import douzone.web.jhkang.backend.codegen.codehelp.format.ComboKeywordTemplateFormat;
import douzone.web.jhkang.backend.codegen.codehelp.format.DateComboKeywordTemplate;
import douzone.web.jhkang.backend.codegen.codehelp.format.DateKeywordTemplate;
import douzone.web.jhkang.backend.codegen.codehelp.format.DatePeriodComboKeywordTemplate;
import douzone.web.jhkang.backend.codegen.codehelp.format.DatePeriodKeywordTemplate;
import douzone.web.jhkang.backend.codegen.codehelp.format.KeywordTemplateFormat;
import douzone.web.jhkang.backend.codegen.codehelp.format.MyBatisFormat;
import douzone.web.jhkang.backend.codegen.codehelp.format.TemplateFormat;
import douzone.web.jhkang.backend.codegen.db.DBConnector;
import douzone.web.jhkang.backend.codegen.db.OracleConnector;
import douzone.web.jhkang.backend.codegen.db.Query;
import douzone.web.jhkang.backend.codegen.db.QueryParameter;
import douzone.web.jhkang.backend.codegen.db.QueryType;
import douzone.web.jhkang.backend.codegen.format.ApiFormat;
import douzone.web.jhkang.backend.codegen.format.GridColumnFormat;
import douzone.web.jhkang.backend.codegen.format.ModelFormat;
import douzone.web.jhkang.backend.codegen.format.SaveApiFormat;
import douzone.web.jhkang.backend.codegen.format.SchemaFormat;
import douzone.web.jhkang.backend.codegen.format.SelectApiFormat;
import douzone.web.jhkang.backend.codegen.gen.ApiParameter;
import douzone.web.jhkang.backend.codegen.gen.CodeGenerator;
import douzone.web.jhkang.backend.codegen.gen.ModelCodeGenerator;
import douzone.web.jhkang.backend.codegen.gen.ModelField;
import douzone.web.jhkang.backend.codegen.gen.SaveCodeGenerator;
import douzone.web.jhkang.backend.codegen.gen.SelectCodeGenerator;
import douzone.web.jhkang.backend.codegen.io.FileOutput;
import douzone.web.jhkang.backend.codegen.io.InputMethod;
import douzone.web.jhkang.backend.codegen.io.OutputMethod;
import douzone.web.jhkang.backend.codegen.io.Reader;
import douzone.web.jhkang.backend.codegen.parser.Parser;

/*
 * History
 * Date		Desc
 * 20200624:코드헬프 코드 생성 추가
 * 
 * 
 */


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
	 
	public void run() throws Exception{	
		outputMethod.display("......Code Gen.....(20.06.23.1) + Code Help");
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
			
		case CODEHELP :
			String codehelpFilePath = inputMethod.selectQueryFileInput();
			File codehelpFile = new File(codehelpFilePath);
			if(codehelpFile.exists()){
				outputMethod.display("code help id : ");
				String codeHelpId = inputMethod.input();
				
				outputMethod.display("type => ");
				outputMethod.display("1 : KEYWORD");
				outputMethod.display("2 : COMBO_KEYWORD");
				outputMethod.display("3 : DATE_KEYWORD");
				outputMethod.display("4 : DATE_PERIOD_KEYWORD");
				outputMethod.display("5 : DATE_PERIOD_COMBO_KEYWORD");
				outputMethod.display("6 : COMBO_DATE_PERIOD_COMBO_KEYWORD");
				outputMethod.display("7 : COMBO2_KEYWORD");
				outputMethod.display("8 : DATE_COMBO_KEYWORD");
				String type = inputMethod.input();
				
				outputMethod.display("title : ");
				String title = inputMethod.input();

				reader.read(codehelpFile);
				selectQuery = new Query(reader.getQuery());
				parser.parse(selectQuery);
				if(parser.getType() != QueryType.SELECT){
					outputMethod.display("select query error");
					return;
				}
				CodeHelpProperty codeHelpProperty = setCodeHelpProperty(codeHelpId, title, type);
				resultQuery = codeHelpGenerate(codeHelpProperty);
			}
			else{
				outputMethod.display("file is not existing");
			}
			break;
		default:
			
			break;
		}
		outputMethod.display(resultQuery);
	}
	public CodeHelpProperty setCodeHelpProperty(String codeHelpId, String title, String type) {
		CodeHelpProperty codeHelpProperty = new CodeHelpProperty();
		
		codeHelpProperty.id = codeHelpId;
		codeHelpProperty.model = codeHelpId + "_Model";
		codeHelpProperty.title = title;
		codeHelpProperty.type = type;
		codeHelpProperty.sqlText = parser.getQuery();
		if(codeHelpProperty.type.contains("1")) {
			codeHelpProperty.templateEnum = "KEYWORD";
			codeHelpProperty.templateClass = "DzCHKeywordTemplate";
			
			codeHelpProperty.implClass = KeywordTemplateFormat.class;
		} else if(codeHelpProperty.type.contains("2")) {
			codeHelpProperty.templateEnum = "COMBO_KEYWORD";
			codeHelpProperty.templateClass = "DzCHComboKeywordTemplate";
			
			codeHelpProperty.implClass = ComboKeywordTemplateFormat.class;
		} else if(codeHelpProperty.type.contains("3")) {
			codeHelpProperty.templateEnum = "DATE_KEYWORD";
			codeHelpProperty.templateClass = "DzCHDateKeywordTemplate";
			
			codeHelpProperty.implClass = DateKeywordTemplate.class;
		} else if(codeHelpProperty.type.contains("4")) {
			codeHelpProperty.templateEnum = "DATE_PERIOD_KEYWORD";
			codeHelpProperty.templateClass = "DzCHDatePeriodKeywordTemplate";
			
			codeHelpProperty.implClass = DatePeriodKeywordTemplate.class;
		} else if(codeHelpProperty.type.contains("5")) {
			codeHelpProperty.templateEnum = "DATE_PERIOD_COMBO_KEYWORD";
			codeHelpProperty.templateClass = "DzCHDatePeriodComboKeywordTemplate";
			
			codeHelpProperty.implClass = DatePeriodComboKeywordTemplate.class;
			
		} else if(codeHelpProperty.type.contains("6")) {
			codeHelpProperty.templateEnum = "COMBO_DATE_PERIOD_COMBO_KEYWORD";
			codeHelpProperty.templateClass = "DzCHComboDatePeriodComboKeywordTemplate";
			
			codeHelpProperty.implClass = DatePeriodComboKeywordTemplate.class;
		} else if(codeHelpProperty.type.contains("7")) {
			codeHelpProperty.templateEnum = "COMBO2_KEYWORD";
			codeHelpProperty.templateClass = "DzCHCombo2KeywordTemplate";
			
			codeHelpProperty.implClass = Combo2KeywordTemplate.class;
			
		} else if(codeHelpProperty.type.contains("8")){
			codeHelpProperty.templateEnum = "DATE_COMBO_KEYWORD";
			codeHelpProperty.templateClass = "DzCHDateComboKeywordTemplate";
			
			codeHelpProperty.implClass = DateComboKeywordTemplate.class;
		} else {
			codeHelpProperty.templateEnum = "KEYWORD";
			codeHelpProperty.templateClass = "DzCHKeywordTemplate";
			
			codeHelpProperty.implClass = KeywordTemplateFormat.class;
		}
		return codeHelpProperty;
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
			if(parameter.equals("P_CD_COMPANY")){
				insertQueryParameters.add(new QueryParameter(parameter, false, "this.getCompanyCode()"));
			} else if(parameter.equals("P_ID_INSERT") || parameter.equals("P_ID_UPDATE")){
				insertQueryParameters.add(new QueryParameter(parameter, false, "this.getUserId()"));
			} else if(parameter.equals("P_DTS_INSERT") || parameter.equals("P_DTS_UPDATE")){
				insertQueryParameters.add(new QueryParameter(parameter, false, "new Date()"));
			} else {
				String attr = parameter.replaceAll("^P_", "").toLowerCase();
				insertQueryParameters.add(new QueryParameter(parameter, true, attr));
			}
			
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
			if(parameter.equals("P_CD_COMPANY")){
				updateQueryParameters.add(new QueryParameter(parameter, false, "this.getCompanyCode()"));
			} else if(parameter.equals("P_ID_INSERT") || parameter.equals("P_ID_UPDATE")){
				updateQueryParameters.add(new QueryParameter(parameter, false, "this.getUserId()"));
			} else if(parameter.equals("P_DTS_INSERT") || parameter.equals("P_DTS_UPDATE")){
				updateQueryParameters.add(new QueryParameter(parameter, false, "new Date()"));
			} else {
				String attr = parameter.replaceAll("^P_", "").toLowerCase();
				updateQueryParameters.add(new QueryParameter(parameter, true, attr));
			}
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
			if(parameter.equals("P_CD_COMPANY")){
				deleteQueryParameters.add(new QueryParameter(parameter, false, "this.getCompanyCode()"));
			} else {
				String attr = parameter.replaceAll("^P_", "").toLowerCase();
				deleteQueryParameters.add(new QueryParameter(parameter, true, attr));
			}
			
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
		
		SchemaFormat schemaFormat = new SchemaFormat(modelFields, apiFormat);
		resultQuery += "\n" + schemaFormat.format; 
		
		GridColumnFormat gridColumnFormat = new GridColumnFormat(modelFields);
		resultQuery += "\n" + gridColumnFormat.format;
		return resultQuery;
	}
	public String codeHelpGenerate(CodeHelpProperty codeHelpProperty) throws Exception{
		Class<?> implClass = Class.forName(codeHelpProperty.implClass.getName());
		TemplateFormat templateFormat = (TemplateFormat) implClass.getDeclaredConstructor(CodeHelpProperty.class).newInstance(codeHelpProperty);
		
		// model Code
		CodeHelpGen codeHelpGen = new CodeHelpGen();
		String modelCode = codeHelpGen.getModelCode(parser, dbCon, codeHelpProperty);
		FileOutput fileOutput = new FileOutput();
		
		codeHelpGen.setCodeHelpParam(parser, codeHelpProperty);
		
		fileOutput.display(codeHelpProperty.model + ".java", modelCode);
		
		//api
		fileOutput.display(codeHelpProperty.id + ".java", templateFormat.getCode());
		
		//mybatis 
		MyBatisFormat myBatisFormat = new MyBatisFormat();
		fileOutput.display(codeHelpProperty.id + ".xml", myBatisFormat.getMyBatisString(codeHelpProperty));
		
		return "";
	}
}
