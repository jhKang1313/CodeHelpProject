package douzone.web.jhkang.backend.codegen.gen;

import java.util.List;

import douzone.web.jhkang.backend.codegen.format.ModelFormat;

public class ModelCodeGenerator extends ModelFormat implements CodeGenerator{
	
	public ModelCodeGenerator(String modelName, String modelDesc, List<ModelField> modelField) {
		super(modelName, modelDesc, modelField);
	}
	public ModelCodeGenerator(ModelFormat modelFormat){
		super(modelFormat.modelName, modelFormat.modelDesc, modelFormat.modelField);
	}
	public String codeGenerate() {
		return format;
	}
}
