package douzone.web.jhkang.backend.codegen.codehelp.format;

import douzone.web.jhkang.backend.codegen.StringFormatting;
import douzone.web.jhkang.backend.codegen.codehelp.CodeHelpProperty;

public abstract class TemplateFormat {
	protected StringFormatting stringUtil = new StringFormatting();
	protected CodeHelpProperty codeHelpProperty;
	public TemplateFormat(CodeHelpProperty codeHelpProperty) {
		this.codeHelpProperty = codeHelpProperty;
	}
	//0 : 도움창 ID
	//1 : 도움창 명
	//2 : 유형
	protected String annotation = "/*\n"
					  + " * 도움창 ID : {0}\n"
					  + " * 도움창 명  : {1}\n"
					  + " * 유형 : {2}\n"
					  + "*/\n";
	//0 : 도움창 ID,
	//1 : 유형
	//2 : 도움창 명
	protected String commponentAnnotationStringFormat = "@DzCodeHelpComponent(value= \"{0}\", type = CodeHelpTemplateType.{1}, desc = \"{2}\")\n";
	
	//0 : 모델
	//1 : 파라미터 코드
	protected String MetaAnnotationStringFormat = "@DzCodeHelpMeta(model = {0}.class{1})\n";
	
	
	//0 : 파라미터 키
	//1 : 파라미터 명
	protected String MetaParamAnnotationStringFormat = "@DzCodeHelpParam(key = \"{0}\", desc = \"{1}\")";
	protected String MetaParamSourceCode;
	
	public void setMetaParamString() {
		if(codeHelpProperty.params.keySet().size() == 0) {
			MetaParamSourceCode = "";
		} else {
			MetaParamSourceCode = ", params = {\n";
			for(String key : codeHelpProperty.params.keySet()) {
				MetaParamSourceCode += "\t\t" + stringUtil.format(MetaParamAnnotationStringFormat, key, codeHelpProperty.params.get(key)) + ",\n";
			}
			MetaParamSourceCode += "}";
		}
		
	}
	//0 : 도움창 ID
	//1 : 템플릿 클래스
	//2 : 템플릿 속성 변수
	//3 : mybatis 설정
	//4 : 그리드 컬럼
	//5 : 추가 작업(드롭다운리스트)
	protected String codeHelpImple = "public class {0} extends {1} {\n"
						 + "\tprotected void setInit() throws Exception {\n"
						 + "\t\tList<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();\n"
						 + "\t\tDzCHCommon dzCHCommon = new DzCHCommon();\n"
						 + "\t\tthis.sortable = true;\n"
						 + "\t\tthis.rowNo = true;\n"
						 + "{2}"
						 + "\n"
						 + "{3}"
						 + "\n"
						 + "{4}"
						 + "\n"
						 + "\t\tthis.gridColumns = columns;\n"
						 + "\n"
						 + "{5}"
						 + "\n"
						 + "\t}\n"
						 + "}\n";
	
	protected String ddlSourceCode = "";
	//0 : 도움창 ID
	//1 : 모델
	protected String mybatisStringFormat = "\t\tthis.xmlFileName = \"mybatis/com/{0}.xml\";\n"
										  +"\t\tthis.queryElemId = \"list\";\n"
										  +"\t\tthis.modelClass = {1}.class;\n";
	protected String mybatisSourceCode = "";
	
	public void setMybatisSourceCode() {
		mybatisSourceCode = stringUtil.format(mybatisStringFormat, codeHelpProperty.id, codeHelpProperty.model);
	}
	
	
	protected String templatePropertiesSourceCode;
	
	
	
	protected String columnDefSourceCode = "";
	public void setColumnsDefSourceCode() {
		for(String key : codeHelpProperty.columns.keySet()) {
			ColumnFormat columnFormat = new ColumnFormat(key, codeHelpProperty.columns.get(key));
			columnDefSourceCode += columnFormat.getColumnCode() + "\n";
		}
	}
	public abstract void setTemplatePropertiesSourceCode();
	
	public String getCode() {
		String resultCode = "";
		setMetaParamString();
		setColumnsDefSourceCode();
		setTemplatePropertiesSourceCode();
		setMybatisSourceCode();
		resultCode += stringUtil.format(annotation, codeHelpProperty.id, codeHelpProperty.title, codeHelpProperty.templateEnum)
					+ stringUtil.format(commponentAnnotationStringFormat, codeHelpProperty.id, codeHelpProperty.templateEnum, codeHelpProperty.title)
					+ stringUtil.format(MetaAnnotationStringFormat, codeHelpProperty.model, MetaParamSourceCode)
					+ stringUtil.format(codeHelpImple, codeHelpProperty.id, codeHelpProperty.templateClass, templatePropertiesSourceCode,
							mybatisSourceCode, columnDefSourceCode, ddlSourceCode);
		
		
		return resultCode;
	}
}
