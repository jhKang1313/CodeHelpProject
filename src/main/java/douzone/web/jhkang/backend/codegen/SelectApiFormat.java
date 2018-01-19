package douzone.web.jhkang.backend.codegen;

import java.util.ArrayList;
import java.util.List;

public class SelectApiFormat extends ApiFormat{
	public String format;
	public void setApiParameter(List<String> parameters){
		this.apiParameter = new ArrayList<ApiParameter>();
		for(String parameter : parameters){
			if(parameter.equals("P_CD_COMPANY"))
				continue;
			String key = parameter.replaceAll("^P_", "").toLowerCase();
			this.apiParameter.add(new ApiParameter(key, key, "", "QueryString", "String"));
		}
	}
	public void setQueryParameter(List<String> parameters){
		this.queryParameter = new ArrayList<QueryParameter>();
		
		for(String parameter : parameters){
			if(parameter.equals("P_CD_COMPANY")){
				this.queryParameter.add(new QueryParameter(parameter, true, "this.getCompanyCode()"));
			} else {
				this.queryParameter.add(new QueryParameter(parameter, true, parameter.replaceAll("^P_", "").toLowerCase()));
			}
			
		}
	}
	public SelectApiFormat(){
		
	}
	public SelectApiFormat(String url, String apiDesc, String httpMethod,String apiType, String returnType, String usingModel,List<ApiParameter> apiParameter, List<QueryParameter> queryParameter, String query) {
		super(url, apiDesc, httpMethod, apiType, returnType, usingModel, apiParameter, queryParameter, query);
		format = "/**\n"
				+" * @section Description \n"
				+" * @details {메뉴명} 조회 API\n"
				+" * @author \n"
				+String.format(" * @details **URL** - /api/{module}/{service}/%s?", url);
		for(int i = 0 ; i < apiParameter.size() ; i++){
			ApiParameter apiParam = apiParameter.get(i);
			format += apiParam.key + "=";
			if(i < apiParameter.size() - 1){
				format += "&";
			}
		}
		
		format += "\n";
		format += " * @details **Method** - GET\n";
		for(int i = 0 ; i < apiParameter.size() ; i++){
			if(i == 0){
				format += String.format(" * - @param String %s \n", apiParameter.get(i).key);
			}else{
				format += String.format(" * @param String %s \n", apiParameter.get(i).key);
			}
		}
		format += String.format(" * - @return models.%s 의 배열형태\n", usingModel)
				+ " */\n";
		
		format += String.format("@DzApi(url=\"/%s\", desc=\"%s\", httpMethod=DzRequestMethod.%s)\n", url, apiDesc, httpMethod) 
				+String.format("public %s<%s> %s(\n", returnType, usingModel, url);												 
		
		for(int i = 0 ; i < apiParameter.size() ; i++){
			ApiParameter apiParam = apiParameter.get(i);
			format += String.format("\t\t@DzParam(key = \"%s\", desc = \"%s\", paramType = DzParamType.%s) %s %s", apiParam.key, apiParam.desc, apiParam.type, apiParam.dataType, apiParam.attrName);
			if(i < apiParameter.size() - 1){
				format += ",\n";
			}
		}
		format += ") throws Exception{\n"
				+ String.format("\tList<%s> items = new ArrayList<%s>();\n", usingModel, usingModel)
				+ "\ttry{\n"
				+ "\t\tHashMap<String, Object> parameters = new HashMap<String, Object>();\n";
		
		if(queryParameter != null){
			for(QueryParameter queryParam : queryParameter){
				format += queryParam.isVariable? 
						"\t\tparameters.put(\"" + queryParam.key + "\", " + queryParam.value + ");\n": 
						"\t\tparameters.put(\"" + queryParam.key + "\", " + "\"queryParam.value\");\n";
				
			}
		}
		format += "\n";
		format += String.format("\t\tString sqlText = QueryGenerator.get(this.getClass(), \"%s.sql\", parameters);\n", query)
				+ "\t\tSqlPack so = new SqlPack();\n"
				+ "\t\tso.setStoreProcedure(false);\n"
				+ "\t\tso.setSqlText(sqlText);\n"
				+ String.format("\t\titems = this.queryForModel(so, %s.class);\n", usingModel)
				+ "\t}catch(DzApplicationRuntimeException e) {\n"
				+ "\t\tthrow e;\n"
				+ "\t}catch (Exception e) {\n"
				+ "\t\tthrow e;\n"
				+ "\t}\n"
				+ "\treturn items;\n"
				+ "}";
		
	}
}
