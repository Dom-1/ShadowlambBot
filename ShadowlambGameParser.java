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
				action = "explore";
			} else if(output.getContent().contains("You did not #start the game yet")) {
				action = "init";
			} else if(output.getContent().contains("ETA:")) {
				action = "startGoing";
			} else if(output.getContent().contains("male halftroll")) {
				System.out.println("Checking HP");
				action = "checkHP";
			} else if(output.getContent().contains("ENCOUNTER")) {
				action = "fight";
			} else if(output.getContent().contains("You continue exploring")) {
				action = "doneFighting";
			} else if(output.getContent().contains("This is a test")) {
				action = "status";
			} else if(output.getContent().contains("You explored Redmond again, but it seems you know every single corner of it")) {
				action = "status";
			} else if(output.getContent().contains("You enter the Redmond Hotel")){
				action = "atHotel";
			} else {
				action = "nothing";
			}
		}
	}

	// public void harvestStatus();
}