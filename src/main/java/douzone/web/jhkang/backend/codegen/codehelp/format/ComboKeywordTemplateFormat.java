package douzone.web.jhkang.backend.codegen.codehelp.format;

import douzone.web.jhkang.backend.codegen.codehelp.CodeHelpProperty;

public class ComboKeywordTemplateFormat extends TemplateFormat{
	public ComboKeywordTemplateFormat(CodeHelpProperty codeHelpProperty) {
		super(codeHelpProperty);
	}
	public void setTemplatePropertiesSourceCode() {
		String propertiesFormatCode = "\t\tthis.titleText = {0};\n"
								     +"\t\tthis.label0Text = \"검색\";\n";
		
		this.templatePropertiesSourceCode = stringUtil.format(propertiesFormatCode, this.codeHelpProperty.title);
		
		this.ddlSourceCode = "this.dzCombo0DataSourceModels = dzCHCommon.getCombo0DataSourceModels(dzCHCommon.DzComboYNDataSourceModels())";
	}
}
