package douzone.web.jhkang.backend.codegen;

public class QueryParameter {
	public String key;
	public boolean isVariable;
	public String value;
	
	public QueryParameter(String key, boolean isVariable, String value){
		this.key = key;
		this.isVariable = isVariable;
		this.value = value;
	}
}
