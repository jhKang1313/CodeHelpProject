package douzone.web.jhkang.backend.codegen;

import douzone.web.jhkang.backend.codegen.io.ConsoleInputMethod;
import douzone.web.jhkang.backend.codegen.io.ConsoleOutputMethod;
import douzone.web.jhkang.backend.codegen.parser.MyJSQLParser;

public class Main {
	public static void main(String[] args) throws Exception{
		new Wizard(new ConsoleInputMethod(), new ConsoleOutputMethod(), new MyJSQLParser()).run();
	}
}


