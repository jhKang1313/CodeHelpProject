package douzone.web.jhkang.backend.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.update.Update;

public class MyJSQLParser implements Parser{
	private Statement statement;
	private SimpleNode node;
	private QueryType type;
	public void parse(Query query) {
		try{
			statement = CCJSqlParserUtil.parse(query.getTrimString());
			node = (SimpleNode) CCJSqlParserUtil.parseAST(query.getTrimString());
			switch(statement.getClass().getSimpleName()){
			case "Select" :
				type = QueryType.SELECT;
				break;
			case "Insert":
				type = QueryType.INSERT;
				break;
			case "Update":
				type = QueryType.UPDATE;
				break;
			case "Delete":
				type = QueryType.DELETE;
				break;
			default :
				break;
			}
		}catch(Exception e){
			System.out.println("invalid query");
			e.printStackTrace();
		}
	}
	public QueryType getType(){
		return type;
	}
	public List<String> getParameters(){
		List<String> parameters = new ArrayList<String>();
		node.jjtAccept(new TripParsingTree(parameters), null);
		return parameters;
	}
	
	public List<String> getColumns(){
		List<String> columns = new ArrayList<String>();
		switch(type){
		case SELECT:
			Select select = (Select) statement;
			
			String detailType = select.getSelectBody().getClass().getSimpleName();
			if(detailType.equals("PlainSelect")){
				PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
				Map<String, String> tableAliasMap = new HashMap<String, String>();
				
				if(plainSelect.getFromItem().getClass().getSimpleName().equals("Table")){
					String[] fromTableName = plainSelect.getFromItem().toString().split(" ");
					if(fromTableName.length == 1){
						tableAliasMap.put("", fromTableName[0]);
					}else{
						tableAliasMap.put(fromTableName[1], fromTableName[0]);
					}
				}
				List<Join> joinTableName = plainSelect.getJoins();
				if(joinTableName != null){
					for(Join join : joinTableName){
						if(join.getRightItem().getClass().getSimpleName().equals("Table")){
							
							String[] tableName = join.getRightItem().toString().split(" ");
							tableAliasMap.put(tableName[1], tableName[0]);
						}else{
							tableAliasMap.put(join.getRightItem().getAlias().toString(), "SUB_QUERY");
						}
						
					}
				}
				for(SelectItem item : plainSelect.getSelectItems()){
					SelectExpressionItem selectItem = (SelectExpressionItem) item;
					columns.add(selectItem.getAlias() == null ? selectItem.toString().toUpperCase() : selectItem.getAlias().getName().toUpperCase());
				}
				columns = convertAliasToTable(columns, tableAliasMap);
			}
			else if(detailType.equals("SetOperationList")){
				SetOperationList operationList = (SetOperationList) select.getSelectBody();
				for(SelectBody body : operationList.getSelects()){
					PlainSelect plainSelect = (PlainSelect)body;
					Map<String, String> tableAliasMap = new HashMap<String, String>();
					
					if(plainSelect.getFromItem().getClass().getSimpleName().equals("Table")){
						String[] fromTableName = plainSelect.getFromItem().toString().split(" ");
						if(fromTableName.length == 1){
							tableAliasMap.put("", fromTableName[0]);
						}else{
							tableAliasMap.put(fromTableName[1], fromTableName[0]);
						}
					}
					List<Join> joinTableName = plainSelect.getJoins();
					for(Join join : joinTableName){
						if(join.getRightItem().getClass().getSimpleName().equals("Table")){
							String[] tableName = join.getRightItem().toString().split(" ");
							tableAliasMap.put(tableName[1], tableName[0]);
						}else{
							tableAliasMap.put(join.getRightItem().getAlias().toString(), "SUB_QUERY");
						}
					}
					for(SelectItem item : plainSelect.getSelectItems()){
						SelectExpressionItem selectItem = (SelectExpressionItem) item;
						columns.add(selectItem.getAlias() == null ? selectItem.toString().toUpperCase() : selectItem.getAlias().getName().toUpperCase());
					}
					columns = convertAliasToTable(columns, tableAliasMap);
				}
			}
			
			break;
		case INSERT:
			Insert insert = (Insert) statement;
			List<Column> insertColumns = insert.getColumns();
			for(Column column : insertColumns){
				columns.add(column.getColumnName().toUpperCase());
			}
			break;
		case UPDATE:
			Update update = (Update) statement;
			List<Column> updateColumns = update.getColumns();
			for(Column column : updateColumns){
				columns.add(column.getColumnName().toUpperCase());
			}
			break;
		default :
			break;
		}
		return columns;
	}
	public List<String> convertAliasToTable(List<String> columns, Map<String, String> tableMap){
		List<String> returnColumn = new ArrayList<String>();
		if(tableMap.size() == 1){
			for(String column : columns){
				for(Entry<String, String> item : tableMap.entrySet()){
					returnColumn.add(item.getValue() + "." + column.substring(column.indexOf('.') +1));
					
				}
			}
		}
		else {
			for(String column : columns){
				String alias = column.indexOf('.') == -1 ? "" : column.substring(0, column.indexOf('.'));
				String columnName = column.substring(column.indexOf('.') +1);
				String fullName = tableMap.get(alias) == null ? "" : tableMap.get(alias) + "."; 
				returnColumn.add(fullName + columnName);
			}
		}
		return returnColumn;
	}
	public Statement getStatement() {
		return statement;
	}
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
}
