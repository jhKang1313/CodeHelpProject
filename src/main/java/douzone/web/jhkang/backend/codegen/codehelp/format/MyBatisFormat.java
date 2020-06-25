package douzone.web.jhkang.backend.codegen.codehelp.format;

import douzone.web.jhkang.backend.codegen.StringFormatting;
import douzone.web.jhkang.backend.codegen.codehelp.CodeHelpProperty;

public class MyBatisFormat {
	public String format = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
							"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n" + 
							"<mapper namespace=\"com.douzone.comet.service.commonhelp.fcm.models.{0}\">\r\n" + 
							"\t<select id=\"list\" parameterType=\"hashmap\" resultType=\"com.douzone.comet.service.commonhelp.fcm.models.{0}\">\n" +
							"\t\t{1}\n" + 
							"\t</select>\n" +
							"</mapper>";
	public String getMyBatisString(CodeHelpProperty codeHelpProperty) {
		String returnString = null;
		StringFormatting stringUtil = new StringFormatting();
		returnString = stringUtil.format(format, codeHelpProperty.model, codeHelpProperty.sqlText);
		return returnString;
	}

}
