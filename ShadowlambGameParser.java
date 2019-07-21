import java.util.*;

public class ShadowlambGameParser {

	private IRCParserOutput output;
	private String action = "";
	
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
		return action;
	}

	public void run() {
		if(!Objects.isNull(output.getContent())) {
			if(output.getContent().contains("#shadowlamb :End of /NAMES list")) {
				action = "start";
			} else if(output.getContent().contains("You did not #start the game yet")) {
				action = "init";
			} else if(output.getContent().contains("ETA:")) {
				action = "startGoing";
			} else if(output.getContent().contains("This is a test")) {
				action = "status";
			} else if(output.getContent().contains("")) {
				
			} else {
				action = "nothing";
			}
		}
	}

	// public void harvestStatus();
}