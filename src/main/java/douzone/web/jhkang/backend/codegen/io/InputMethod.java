package douzone.web.jhkang.backend.codegen.io;

import java.util.List;

import douzone.web.jhkang.backend.codegen.db.QueryType;
import douzone.web.jhkang.backend.codegen.format.ApiFormat;
import douzone.web.jhkang.backend.codegen.format.ModelFormat;

public interface InputMethod {
	public List<String> saveQueryFileInput();
	public String selectQueryFileInput();
	public ApiFormat apiInfoInput(ApiFormat format);
	public ModelFormat modelInfoInput(ModelFormat format);
	public QueryType inputQueryType();
	public String modelName();
	public boolean doDBConnect();
	public String input();
}
