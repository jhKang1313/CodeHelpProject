package douzone.web.jhkang.backend.codegen.codehelp.format;

import douzone.web.jhkang.backend.codegen.codehelp.CodeHelpProperty;

public class ComboDatePeriodComboKeywordTemplate extends TemplateFormat{

	public ComboDatePeriodComboKeywordTemplate(CodeHelpProperty codeHelpProperty) {
		super(codeHelpProperty);
	}
	public void setTemplatePropertiesSourceCode() {
		String propertiesFormatCode = "\t\tthis.titleText = {0};\n"
				 +"\t\tthis.label0Text = \"기간\";\n"
				 +"\t\tthis.label1Text = \"구분\";\n"
				 +"\t\tthis.label2Text = \"검색어\";\n"
				 +"\t\tthis.startDate = monthFirstLastDate.get(\"firstDate\");\n"
				 +"\t\tthis.endDate = monthFirstLastDate.get(\"lastDate\");\n";

		this.templatePropertiesSourceCode = stringUtil.format(propertiesFormatCode, this.codeHelpProperty.title);
		
		this.ddlSourceCode = "this.dzCombo0DataSourceModels = dzCHCommon.getCombo0DataSourceModels(dzCHCommon.DzComboYNDataSourceModels());\n"
							+"this.dzCombo1DataSourceModels = dzCHCommon.getCombo1DataSourceModels(dzCHCommon.DzComboYNDataSourceModels());\n";
	}
}
