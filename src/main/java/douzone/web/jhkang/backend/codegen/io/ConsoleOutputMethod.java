package douzone.web.jhkang.backend.codegen.io;

public class ConsoleOutputMethod implements OutputMethod{
	public void display(String msg) {
		System.out.println(msg);
	}
	public void display(String key, String msg) {
		System.out.println("=========== " + key + " =============");
		System.out.println(msg);
	}
}
