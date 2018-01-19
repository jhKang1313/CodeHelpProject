package douzone.web.jhkang.backend.codegen;

import java.util.List;
import java.util.regex.Pattern;

import net.sf.jsqlparser.parser.CCJSqlParserDefaultVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserTreeConstants;
import net.sf.jsqlparser.parser.SimpleNode;

public class TripParsingTree extends CCJSqlParserDefaultVisitor{
	private List<String> columnList;
	private Pattern pattern = Pattern.compile("^P_[.]*");
	public TripParsingTree(List<String> columnList){
		this.columnList = columnList;
	}
	public boolean isDupleElement(String newParameter){
		for(String parameter : columnList ){
			if(parameter.equals(newParameter)){
				return true;
			}
		}
		return false;
	}
	public Object visit(SimpleNode node, Object data) {
		if (node.getId() == CCJSqlParserTreeConstants.JJTCOLUMN) {
        	String column = node.jjtGetValue().toString().toUpperCase();
        	if(pattern.matcher(column).find()){
        		if(!isDupleElement(column)){
        			columnList.add(column);
        		}
        	}
            return super.visit(node, data);
        } else {
            return super.visit(node, data);
        }
	}
}
