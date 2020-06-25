package douzone.web.jhkang.backend.codegen.gen;

import java.util.List;

import douzone.web.jhkang.backend.codegen.db.QueryParameter;
import douzone.web.jhkang.backend.codegen.format.ApiFormat;
import douzone.web.jhkang.backend.codegen.format.SelectApiFormat;

public class SelectCodeGenerator extends SelectApiFormat implements CodeGenerator{
	public SelectCodeGenerator(String url, String apiDesc, String httpMethod,String apiType, String returnType, String usingModel,List<ApiParameter> apiParameter, List<QueryParameter> queryParameter, String query) {
		super(url, apiDesc, httpMethod, apiType, returnType, usingModel, apiParameter, queryParameter, query);
	}
	public SelectCodeGenerator(ApiFormat format){
		super(format.url, format.apiDesc, format.httpMethod, format.apiType, format.returnType, format.usingModel, format.apiParameter, format.queryParameter, format.query);
	}
	public String codeGenerate() {
		return this.format;
	}
}
