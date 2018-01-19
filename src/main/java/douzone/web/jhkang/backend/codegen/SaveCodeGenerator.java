package douzone.web.jhkang.backend.codegen;

import java.util.List;

public class SaveCodeGenerator extends SaveApiFormat implements CodeGenerator{
	public SaveCodeGenerator(SaveApiFormat saveApiFormat, List<QueryParameter> updateParameter, List<QueryParameter> deleteParameter, List<String> fileName){
		super(saveApiFormat.url, saveApiFormat.apiDesc, saveApiFormat.httpMethod, saveApiFormat.apiType, 
				saveApiFormat.returnType, saveApiFormat.usingModel, saveApiFormat.apiParameter, saveApiFormat.queryParameter, 
				saveApiFormat.query, updateParameter, deleteParameter, fileName);
	}
	public String codeGenerate(){
		return format;
	}
}
