package douzone.web.jhkang.backend.codegen.format;

import java.util.List;

import douzone.web.jhkang.backend.codegen.db.QueryParameter;
import douzone.web.jhkang.backend.codegen.gen.ApiParameter;

public class ApiFormat {
	public String url;
	public String apiDesc;
	public String httpMethod;
	public String apiType;
	public String returnType;
	public String usingModel;
	public String query;
	public List<ApiParameter> apiParameter;
	public List<QueryParameter> queryParameter;
	public ApiFormat(){
		
		
	}
	public void setApiParameter(List<String> parameters){
		
	}
	public void setQueryParameter(List<String> parameters){
		
	}
	public ApiFormat(String url, String apiDesc, String httpMethod, String apiType, String returnType, String usingModel, List<ApiParameter> apiParameter, List<QueryParameter> queryParameter, String query){
		this.url = url;
		this.apiDesc = apiDesc;
		this.httpMethod = httpMethod;
		this.apiType = apiType;
		this.returnType = returnType;
		this.usingModel = usingModel;
		this.apiParameter = apiParameter;
		this.queryParameter = queryParameter;
		this.query = query;
	}
}
