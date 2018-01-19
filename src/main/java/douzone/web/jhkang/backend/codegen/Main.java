package douzone.web.jhkang.backend.codegen;

public class Main {
	public static void main(String[] args){
		new Wizard(new ConsoleInputMethod(), new ConsoleOutputMethod(), new MyJSQLParser()).run();
	}
}


