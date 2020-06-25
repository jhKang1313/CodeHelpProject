package douzone.web.jhkang.backend.codegen.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

public class FileOutput implements OutputMethod{
	public void display(String key, String msg) {
		fileWrite(key, msg);
	}
	public void display(String msg) {
		fileWrite(null, msg);
	}	
	public void fileWrite(String key, String msg) {
		try {
			key = key == null ? new Date().toString() : key;
			String filePath = "C:/codeGenOutput";
			File genFolder = new File(filePath);
			if(!genFolder.exists()) {
				if(!genFolder.mkdir()) {
					throw new Exception("폴더 생성이 안됨");
				}
			}
			System.out.println(key);
			System.out.println(msg);
			filePath += "/" + key;
			BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
			fileWriter.write(msg);
			fileWriter.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
