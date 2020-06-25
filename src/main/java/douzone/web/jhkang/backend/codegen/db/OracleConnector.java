package douzone.web.jhkang.backend.codegen.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import douzone.web.jhkang.backend.codegen.gen.ModelField;

public class OracleConnector implements DBConnector{
	Connection con;
	PreparedStatement ps;
	ResultSet rs;
	
	
	public OracleConnector(){
		connect();
	}
	public Connection connect() {
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@172.16.112.51:15253:orcl";
			con = DriverManager.getConnection(url, "COMET", "COMET");
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}

	public ModelField query(String tableName, String columnName) {
		String queryFormat =  "SELECT C.TABLE_NAME, C.COLUMN_NAME, CM.COMMENTS, C.DATA_TYPE, "
							+ "CASE C.DATA_TYPE "
							+ "WHEN 'VARCHAR2' THEN C.DATA_LENGTH / 4 || '' "
							+ "WHEN 'NUMBER' THEN C.DATA_PRECISION || ',' || C.DATA_SCALE "
							+ "END AS DATA_LENGTH "
							+ "FROM COLS C "
							+ "LEFT JOIN USER_COL_COMMENTS CM "
							+ "ON C.TABLE_NAME = CM.TABLE_NAME "
							+ "AND C.COLUMN_NAME = CM.COLUMN_NAME "
							+ "WHERE C.TABLE_NAME = '" + tableName + "' "
							+ "AND C.COLUMN_NAME = '" + columnName + "'";
		ModelField modelField = null;
		try{
			ps = con.prepareStatement(queryFormat);
			rs = ps.executeQuery();
			
			modelField = new ModelField();
			modelField.desc = "";
			modelField.columnDataType = "";
			modelField.colName = columnName;
			modelField.tableName = tableName;
			modelField.dataType = "String";
			
			while(rs.next()){
				
				
				String dataType = rs.getString("DATA_TYPE");
				String capa = rs.getString("DATA_LENGTH");
				String comment = rs.getString("COMMENTS");
				
				modelField.desc = comment;
				modelField.colName = columnName;
				modelField.tableName = tableName;
				switch(dataType){
				case "VARCHAR2" :
					modelField.dataType = "String";
					modelField.columnDataType = dataType + "(" + capa + ")";
					break;
				case "NUMBER" :
					modelField.dataType = "BigDecimal";
					modelField.columnDataType = dataType + "(" + capa + ")";
					break;
					
				case "DATE" :
					modelField.dataType = "Date";
					modelField.columnDataType = dataType;
					break;
				}
			}
			ps.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return modelField;
	}
	public void close(){
		try{
			con.close();
			ps.close();
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
