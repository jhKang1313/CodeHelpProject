package douzone.web.jhkang.backend.codegen;

import java.util.List;

public interface InputMethod {
	public List<String> saveQueryFileInput();
	public String selectQueryFileInput();
	public ApiFormat apiInfoInput(ApiFormat format);
	public ModelFormat modelInfoInput(ModelFormat format);
	public QueryType inputQueryType();
	public String modelName();
	public boolean doDBConnect();
}
