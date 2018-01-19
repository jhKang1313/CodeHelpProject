package douzone.web.jhkang.backend.codegen;
import java.sql.ResultSet;

public interface DBConnector {
	public ModelField query(String table, String column);
}
