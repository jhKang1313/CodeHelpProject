package douzone.web.jhkang.backend.codegen.test;

import java.io.StringReader;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

public class App 
{
    public static void main( String[] args )
    {
    	 String sql = "SELECT\n"
                 + "    Something\n"
                 + "FROM\n"
                 + "    Sometable\n"
                 + "WHERE\n"
                 + "    Somefield > \t = Somevalue\n"
                 + "    AND Somefield <   = Somevalue\n"
                 + "    AND Somefield <\t\t> Somevalue\n";
    	 try{
	    	 Statement statement = CCJSqlParserUtil.parse(sql);
	    	 Select ss = (Select)statement;
	    	 CCJSqlParserManager  parserManager = new CCJSqlParserManager();
	    	 //statement.getClass()
	    	 Select select = (Select) parserManager.parse(new StringReader(sql));
	    	 Expression expression = ((SelectExpressionItem) ((PlainSelect) select.getSelectBody()).getSelectItems().get(0)).getExpression();
	    	 
	    	 Column col = (Column) expression;
	    	 System.out.println(col.getColumnName());
	    	 //((DoubleValue) ((BinaryExpression) ((PlainSelect) select.getSelectBody()).getWhere()).getRightExpression()).getValue();
	    	 //((BinaryExpression) ((PlainSelect) select.getSelectBody()).getWhere()).getLeftExpression()
	    	 PlainSelect plainSelect = (PlainSelect)((Select) parserManager.parse(new StringReader(sql))).getSelectBody();
	    	 System.out.println(plainSelect);
	    	 ((BinaryExpression) plainSelect.getWhere()).getLeftExpression();
    	 }catch(Exception e){
    		 e.printStackTrace();
    	 }

    }
}
