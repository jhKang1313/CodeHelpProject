package douzone.web.jhkang.backend.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleInputMethod implements InputMethod{
	Scanner scanner = new Scanner(System.in);
	public String selectQueryFileInput(){
		String filePath = "";
		System.out.println("select query file path : ");
		filePath = scanner.nextLine();
		return filePath;
	}
	public boolean doDBConnect(){
		System.out.println("DataBase connect?");
		System.out.println("1. connect \n2. none");
		String input = scanner.nextLine();
		
		switch(input){
		case "1":
			return true;
		case "2":
			return false;
		}
		return false;
	}
	public List<String> saveQueryFileInput(){
		List<String> fileList = new ArrayList<String>();
		System.out.println("save query file path (except query => 'x' character input)");
		System.out.print("insert : ");
		fileList.add(scanner.nextLine());
		System.out.print("update : ");
		fileList.add(scanner.nextLine());
		System.out.print("delete : ");
		fileList.add(scanner.nextLine());
		
		return fileList;
	}
	public QueryType inputQueryType(){
		System.out.println("1. select api\n2. save api");
		String input = scanner.nextLine();
		
		switch(input){
		case "1":
			return QueryType.SELECT;
		case "2":
			return QueryType.UPDATE;
		default :
			return null;
		}
		
	}
	public ApiFormat apiInfoInput(ApiFormat format){
		System.out.println("API 정보 입력");
		System.out.print("url : ");
		String url = scanner.nextLine();
		System.out.print("model Name : ");
		String model = scanner.nextLine();
		
		format.url = url;
		format.usingModel = model;
		return format;
		
	}
	public String modelName(){
		System.out.println("Model Name : ");
		String modelName = scanner.nextLine();
		
		return modelName;
	}
	public ModelFormat modelInfoInput(ModelFormat format){
		System.out.println();
		System.out.println("*model ");
		System.out.println("name : ");
		String name = scanner.nextLine();
		format.modelName = name;
		format.modelDesc = "";
		return format;
	}
}
