package douzone.web.jhkang.backend.codegen.db;
import douzone.web.jhkang.backend.codegen.gen.ModelField;

public interface DBConnector {
	public ModelField query(String table, String column);
}
