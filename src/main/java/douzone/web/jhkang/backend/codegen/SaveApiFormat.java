package douzone.web.jhkang.backend.codegen;

import java.util.ArrayList;
import java.util.List;

public class SaveApiFormat extends ApiFormat{
	public String format = "";
	public List<QueryParameter> updateParameter;
	public List<QueryParameter> deleteParameter;
	public List<String> fileNames;
	public String sqlPackSourceCode = "\t\t\t\tSqlPack so = new SqlPack();\n"
									+ "\t\t\t\tso.setStoreProcedure(false);\n"
									+ "\t\t\t\tso.setSqlText(sqlText);\n"
									+ "\t\t\t\tthis.update(so);\n"
									+ "\t\t\t}\n"
									+ "\t\t}\n"; 
	public SaveApiFormat(){
		
	}
	public void setApiParameter(List<String> parameters){
		
	}
	public void setQueryParameter(List<String> parameters){
		this.queryParameter = new ArrayList<QueryParameter>();
		for(String parameter : parameters){
			this.queryParameter.add(new QueryParameter(parameter, true, parameter.replaceAll("^P_", "").toLowerCase()));
		}
	}
	public void setUpateQueryParameter(List<String> parameters){
		this.updateParameter = new ArrayList<QueryParameter>();
		
		for(String parameter : parameters){
			this.updateParameter.add(new QueryParameter(parameter, true, parameter.replaceAll("^P_", "").toLowerCase()));
		}
	}
	public void setDeleteQueryParameter(List<String> parameters){
		this.deleteParameter = new ArrayList<QueryParameter>();
		
		for(String parameter : parameters){
			this.deleteParameter.add(new QueryParameter(parameter, true, parameter.replaceAll("^P_", "").toLowerCase()));
		}
	}
	public SaveApiFormat(String url, String apiDesc, String httpMethod,String apiType, String returnType, String usingModel,List<ApiParameter> apiParameter,List<QueryParameter> queryParameter, String query, List<QueryParameter> updateParameter, List<QueryParameter> deleteParameter, List<String> fileName) {
		super(url, apiDesc, httpMethod, apiType, returnType, usingModel, apiParameter,queryParameter, query);
		this.updateParameter = updateParameter;
		this.deleteParameter = deleteParameter;
		this.fileNames = fileName;
		
		format = "/**\n"
				+" * @section Description \n"
				+" * @details 저장 API\n"
				+" * @author \n"
				+String.format(" * @details **URL** - /api/{module}/{service}/%s\n", url);
		format +=" * @details **Method** - POST\n"
				+ String.format(" * - @param DzGridModel<%s> dataSource {테이블명} models.%s\n", usingModel, usingModel)
				+ " * - @return boolean\n"
				+ " */\n";
		
		/* API Define */
		format += String.format("@DzApi(url=\"/%s\", desc=\"%s\", httpMethod=DzRequestMethod.%s)\n", url, apiDesc, httpMethod) 
				+String.format("public boolean %s(\n", url);												 
		
		/* Parameter Setting */
		ApiParameter apiParam = apiParameter.get(0);
		format += String.format("\t\t@DzParam(key = \"%s\", desc = \"%s\", paramType = DzParamType.%s) %s<%s> %s", apiParam.key, apiParam.desc, apiParam.type, "DzGridModel", this.usingModel, apiParam.attrName);
		
		format += ") throws Exception{\n"
				+ "\tDbTransaction transaction = null;\n"
				+ "\ttry{\n"
				+ "\t\ttransaction = this.beginTransaction();\n\n"
				+ (fileName.get(0).equals("x") ? "" : String.format("\t\tList<%s> added = %s.getAdded();\n", this.usingModel, apiParam.attrName))
				+ (fileName.get(1).equals("x") ? "" : String.format("\t\tList<%s> updated = %s.getUpdated();\n", this.usingModel, apiParam.attrName))
				+ (fileName.get(2).equals("x") ? "" : String.format("\t\tList<%s> deleted = %s.getDeleted();\n\n", this.usingModel, apiParam.attrName));
				
		
		
		/*insert code*/
		if(!fileName.get(0).equals("x")){
			format += "\t\tif(added != null){\n"
					+ String.format("\t\t\tfor(%s item : added){\n", this.usingModel)
					+ "\t\t\t\tHashMap<String, Object> parameters = new HashMap<String, Object>();\n\n";
			for(QueryParameter queryParam : queryParameter){
				String funcName = queryParam.value.substring(0, 1).toUpperCase() + queryParam.value.substring(1, queryParam.value.length());
				format += String.format("\t\t\t\tparameters.put(\"%s\", item.get%s());\n", queryParam.key, funcName);
			}
			format += "\n";
			format += String.format("\t\t\t\tString  sqlText = QueryGenerator.get(this.getClass(), \"%s.sql\", parameters);\n", fileNames.get(0))
					+ sqlPackSourceCode;
		}
		
		/*update code*/
		if(!fileName.get(1).equals("x")){
			format += "\t\tif(updated != null){\n"                                                         
					+ String.format("\t\t\tfor(%s item : updated){\n", this.usingModel)                    
					+ "\t\t\t\tHashMap<String, Object> parameters = new HashMap<String, Object>();\n\n"; 
			
			for(QueryParameter queryParam : updateParameter){
				String funcName = queryParam.value.substring(0, 1).toUpperCase() + queryParam.value.substring(1, queryParam.value.length());
				format += String.format("\t\t\t\tparameters.put(\"%s\", item.get%s());\n", queryParam.key, funcName);
			}
			format += "\n";
			format += String.format("\t\t\t\tString sqlText = QueryGenerator.get(this.getClass(), \"%s.sql\", parameters);\n", fileNames.get(1))
					+ sqlPackSourceCode;
		}
		
		/*delete code*/
		if(!fileName.get(2).equals("x")){
			format += "\t\tif(deleted != null){\n"                                                         
					+ String.format("\t\t\tfor(%s item : deleted){\n", this.usingModel)                    
					+ "\t\t\t\tHashMap<String, Object> parameters = new HashMap<String, Object>();\n\n"; 
			for(QueryParameter queryParam : deleteParameter){
				String funcName = queryParam.value.substring(0, 1).toUpperCase() + queryParam.value.substring(1, queryParam.value.length());
				format += String.format("\t\t\t\tparameters.put(\"%s\", item.get%s());\n", queryParam.key, funcName);
			}
			format += "\n";
			format += String.format("\t\t\t\tString sqlText = QueryGenerator.get(this.getClass(), \"%s.sql\", parameters);\n", fileNames.get(2))
					+ sqlPackSourceCode;
			
		}
		format += "\t\ttransaction.commit();\n"
				+ "\t}catch(DzApplicationRuntimeException e){\n"
				+ "\t\tif(transaction != null){\n"
				+ "\t\t\ttransaction.rollback();\n"
				+ "\t\t}\n"
				+ "\t\tthrow e;\n"
				+ "\t}catch(Exception e){\n"
				+ "\t\tif(transaction != null){\n"
				+ "\t\t\ttransaction.rollback();\n"
				+ "\t\t}\n"
				+ "\t\tthrow e;\n"
				+ "\t}\n"
				+ "\treturn true;\n"
				+ "}";
	}
}
