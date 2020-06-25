package douzone.web.jhkang.backend.codegen.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
	private String query = "";
	private BufferedReader reader;
	public String fileName = "";
	public String getQuery(){
		return query;
	}
	public void read(File file){
		query = "";
		try{
			reader = new BufferedReader(new FileReader(file));
			fileName = file.getName();
			String line = "";
			while((line = reader.readLine()) != null){
				query += line + "\n";
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
