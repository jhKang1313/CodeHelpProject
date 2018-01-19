package douzone.web.jhkang.backend.codegen;

import java.util.List;

public interface Parser {
	public void parse(Query query);
	public List<String> getParameters();
	public List<String> getColumns();
	public QueryType getType();
}
