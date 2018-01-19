package douzone.web.jhkang.backend.codegen;

public class Query {
	
	private String originString;
	private String trimString;
	
	public Query(String query){
		this.originString = query;
		this.trimString = query.replaceAll("[\\{\\}@;]", "");
	}
	public String getTrimString(){
		return this.trimString;
	}
	public String getOriginString(){
		return this.originString;
	}
}
