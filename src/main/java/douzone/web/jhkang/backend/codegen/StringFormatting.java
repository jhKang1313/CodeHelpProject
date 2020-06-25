package douzone.web.jhkang.backend.codegen;

public class StringFormatting {
	public String format(String sourceString, String...params) {
		String targetString = sourceString;
		for(int i = 0 ; i < params.length ; i++) {
			targetString = targetString.replaceAll("\\{" + i + "\\}", params[i]);
		}
		return targetString;
	}
}
