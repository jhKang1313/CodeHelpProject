package douzone.web.jhkang.backend.codegen.codehelp.format;

import douzone.web.jhkang.backend.codegen.codehelp.CodeHelpProperty;

public class DateKeywordTemplate extends TemplateFormat{
	public DateKeywordTemplate(CodeHelpProperty codeHelpProperty) {
		super(codeHelpProperty);
	}

	public void setTemplatePropertiesSourceCode() {
		String propertiesFormatCode = "\t\tthis.titleText = {0};\n"
			     +"\t\tthis.label0Text = \"날짜\";\n";

		this.templatePropertiesSourceCode = stringUtil.format(propertiesFormatCode, this.codeHelpProperty.title);
	}
}
