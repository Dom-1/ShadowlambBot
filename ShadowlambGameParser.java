public class ShadowlambGameParser {

	private IRCParserOutput output;
	
	public ShadowlambGameParser(IRCParserOutput output) {
		this.output = output;
	}

	public IRCParserOutput getOutput() {
	    return output;
	}
	 
	public void setOutput(IRCParserOutput output) {
	    this.output = output;
	}

	public String getAction() {
		return "";
	}
}