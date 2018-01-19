package douzone.web.jhkang.backend.codegen;

import java.util.List;

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
