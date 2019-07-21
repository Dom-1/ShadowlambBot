public class IRCParser {
	public static IRCParserOutput parse(String msg) {
		IRCParserOutput output = new IRCParserOutput();

		int spIndex;
		
		if (msg.startsWith(":")) {	
			spIndex = msg.indexOf(' ');
			if (spIndex > -1) {
				output.setOrigin(msg.substring(1, spIndex));
				msg = msg.substring(spIndex + 1);
				
				int uIndex = output.getOrigin().indexOf('!');
				if (uIndex > -1) {
					output.setNickname(output.getOrigin().substring(0, uIndex));
				}
			}
		}
		spIndex = msg.indexOf(' ');
		if (spIndex == -1) {
			output.setCommand("null");
			return output;
		}
		
		output.setCommand(msg.substring(0, spIndex).toLowerCase());
		msg = msg.substring(spIndex + 1);

		// parse privmsg params
		if (output.getCommand().equals("privmsg")) {
			spIndex = msg.indexOf(' ');
			output.setTarget(msg.substring(0, spIndex));
			msg = msg.substring(spIndex + 1);
			
			if (msg.startsWith(":")) {
				output.setContent(msg.substring(1));
			}
			else {
				output.setContent(msg);
			}
		}
		
		// parse quit/join
		if (output.getCommand().equals("quit") || output.getCommand().equals("join")) {
			if (msg.startsWith(":")) {
				output.setContent(msg.substring(1));
			}
			else {
				output.setContent(msg);
			}
		}
		
		// parse ping params
		if (output.getCommand().equals("ping")) {
			spIndex = msg.indexOf(' ');
			if (spIndex > -1) {
				output.setTarget(msg.substring(0, spIndex));
			}
			else {
				output.setTarget(msg);
			}
		}

		if(output.getCommand().equals("notice")) {
			spIndex = msg.indexOf(' ');
			if(spIndex > -1) {
				output.setNoticeType(msg.substring(0, spIndex));
				msg = msg.substring(spIndex + 1);
				if(msg.startsWith(":")) {
					output.setContent(msg.substring(1));
				} else {
					output.setContent(msg);
				}
			}
		}

		if(output.getCommand().equals("mode")) {
			spIndex = msg.indexOf(' ');
			if(spIndex > -1) {
				output.setContent(msg.substring(spIndex));
			} else {
				output.setContent(msg);
			}
		}

		try {
			if(Integer.parseInt(output.getCommand()) > 0) {
				output.setContent(msg);
			}
		} catch (Exception e) {}

		return output;
	}
}