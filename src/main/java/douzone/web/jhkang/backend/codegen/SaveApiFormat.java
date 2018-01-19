package douzone.web.jhkang.backend.codegen;

import java.util.ArrayList;
import java.util.List;

public class SaveApiFormat extends ApiFormat{
	public String format = "";
	public List<QueryParameter> updateParameter;
	public List<QueryParameter> deleteParameter;
	public List<String> fileNames;
	public String sqlPackSourceCode = "                SqlPack so = new SqlPack();\n"
									+ "                so.setStoreProcedure(false);\n"
									+ "                so.setSqlText(sqlText);\n"
									+ "                this.update(so);\n"
									+ "            }\n"
									+ "        }\n"; 
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
		format += String.format("        @DzParam(key = \"%s\", desc = \"%s\", paramType = DzParamType.%s) %s<%s> %s", apiParam.key, apiParam.desc, apiParam.type, "DzGridModel", this.usingModel, apiParam.attrName);
		
		format += ") throws Exception{\n"
				+ "    DbTransaction transaction = null;\n"
				+ "    try{\n"
				+ "        transaction = this.beginTransaction();\n\n"
				+ (fileName.get(0).equals("x") ? "" : String.format("        List<%s> added = %s.getAdded();\n", this.usingModel, apiParam.attrName))
				+ (fileName.get(1).equals("x") ? "" : String.format("        List<%s> updated = %s.getUpdated();\n", this.usingModel, apiParam.attrName))
				+ (fileName.get(2).equals("x") ? "" : String.format("        List<%s> deleted = %s.getDeleted();\n\n", this.usingModel, apiParam.attrName));
				
		
		
		/*insert code*/
		if(!fileName.get(0).equals("x")){
			format += "        if(added != null){\n"
					+ String.format("            for(%s item : added){\n", this.usingModel)
					+ "                HashMap<String, Object> parameters = new HashMap<String, Object>();\n\n";
			for(QueryParameter queryParam : queryParameter){
				if(queryParam.isVariable == true){
					String funcName = queryParam.value.substring(0, 1).toUpperCase() + queryParam.value.substring(1, queryParam.value.length());
					format += String.format("                parameters.put(\"%s\", item.get%s());\n", queryParam.key, funcName);
				} else {
					format += String.format("                parameters.put(\"%s\", %s);\n", queryParam.key, queryParam.value);
				}
				
			}
			format += "\n";
			format += String.format("                String  sqlText = QueryGenerator.get(this.getClass(), \"%s.sql\", parameters);\n", fileNames.get(0))
					+ sqlPackSourceCode;
		}
		
		/*update code*/
		if(!fileName.get(1).equals("x")){
			format += "        if(updated != null){\n"                                                         
					+ String.format("            for(%s item : updated){\n", this.usingModel)                    
					+ "                HashMap<String, Object> parameters = new HashMap<String, Object>();\n\n"; 
			
			for(QueryParameter queryParam : updateParameter){
				if(queryParam.isVariable == true){
					String funcName = queryParam.value.substring(0, 1).toUpperCase() + queryParam.value.substring(1, queryParam.value.length());
					format += String.format("                parameters.put(\"%s\", item.get%s());\n", queryParam.key, funcName);
				} else {
					format += String.format("                parameters.put(\"%s\", %s);\n", queryParam.key, queryParam.value);
				}
			}
			format += "\n";
			format += String.format("                String sqlText = QueryGenerator.get(this.getClass(), \"%s.sql\", parameters);\n", fileNames.get(1))
					+ sqlPackSourceCode;
		}
		
		/*delete code*/
		if(!fileName.get(2).equals("x")){
			format += "        if(deleted != null){\n"                                                         
					+ String.format("            for(%s item : deleted){\n", this.usingModel)                    
					+ "                HashMap<String, Object> parameters = new HashMap<String, Object>();\n\n"; 
			for(QueryParameter queryParam : deleteParameter){
				if(queryParam.isVariable == true){
					String funcName = queryParam.value.substring(0, 1).toUpperCase() + queryParam.value.substring(1, queryParam.value.length());
					format += String.format("                parameters.put(\"%s\", item.get%s());\n", queryParam.key, funcName);
				} else {
					format += String.format("                parameters.put(\"%s\", %s);\n", queryParam.key, queryParam.value);
				}
			}
			format += "\n";
			format += String.format("                String sqlText = QueryGenerator.get(this.getClass(), \"%s.sql\", parameters);\n", fileNames.get(2))
					+ sqlPackSourceCode;
			
		}
		format += "        transaction.commit();\n"
				+ "    }catch(DzApplicationRuntimeException e){\n"
				+ "        if(transaction != null){\n"
				+ "            transaction.rollback();\n"
				+ "        }\n"
				+ "        throw e;\n"
				+ "    }catch(Exception e){\n"
				+ "        if(transaction != null){\n"
				+ "            transaction.rollback();\n"
				+ "        }\n"
				+ "        throw e;\n"
				+ "    }\n"
				+ "    return true;\n"
				+ "}";
	}
}
