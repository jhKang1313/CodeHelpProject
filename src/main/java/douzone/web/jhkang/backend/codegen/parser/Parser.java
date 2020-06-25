package douzone.web.jhkang.backend.codegen.parser;

import java.util.List;

import douzone.web.jhkang.backend.codegen.db.Query;
import douzone.web.jhkang.backend.codegen.db.QueryType;

public interface Parser {
	public void parse(Query query);
	public List<String> getParameters();
	public List<String> getColumns();
	public String getQuery();
	public QueryType getType();
}
