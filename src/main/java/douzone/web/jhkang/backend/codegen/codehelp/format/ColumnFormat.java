package douzone.web.jhkang.backend.codegen.codehelp.format;

public class ColumnFormat {
	String codeField;
	String textField;
	boolean visible = true;
	String align;
	
	public ColumnFormat(String codeField, String textField) {
		this.codeField = codeField;
		this.textField = textField;
	}
	public String getColumnCode() {
		String resultString = "";
		setAlign();
		resultString += "\t\tcolumns.add(dzCHCommon.setColumns(\"" + this.textField + "\", \"" 
					  + this.codeField + "\", \"" + this.align + "\", null, 100, " + visible + "));";
		return resultString;
	}
	
	public void setAlign() {
		int offset = codeField.lastIndexOf("_");
		if(offset != -1) {
			switch(codeField.substring(offset)) {
			case "AMT":
				this.align = "right";
				break;
			case "DTS":
			case "DT":
			case "NO":
				this.align = "center";
				break;
			default : 
				this.align = "left";
				break;
			}
		} else {
			this.align = "left";
		}
		
	}
}
