import java.io.*;
import java.util.*;

public class ShadowlambClient extends IRCCommunicator {

	ShadowlambConf clientConf;
	
	public ShadowlambClient(IRCConf conf, ShadowlambConf clientConf) {
		super(conf);
		this.clientConf = clientConf;
	}

	public void processMessage(String ircMessage) {
		IRCParserOutput parsed = IRCParser.parse(ircMessage);

		switch (parsed.getCommand()) {
			case "notice":
				if(parsed.getContent().contains("Looking")) {
					setUser(clientConf.getUsername(), "0", "*", ":"+clientConf.getUsername());
				} else if(parsed.getContent().contains("please choose a different nick.")) {
					try {
						privmsg("NickServ", "identify " + clientConf.getPassword());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case "ping":
				pong(parsed.getTarget());
				break;
			case "privmsg":
				if(parsed.getContent().contains("\001VERSION\001")) {
					privmsg(parsed.getNickname(), clientConf.getUsername() + " Bot Client");
					break;
				}
				System.out.println(parsed.getNickname() + ": " + parsed.getContent());
				break;
			case "mode":
				if(parsed.getContent().contains("+r")) {
					joinChannel("#shadowlamb");
				}
		}
	}

	@Override 
	public void run() {
		InputStream stream = null;

		try {

			stream = conn.getInputStream();
			MessageBuffer messageBuffer = new MessageBuffer();
			byte[] buffer = new byte[512];
			int count;
			
			while (true) {
				count = stream.read(buffer);
				if (count == -1)
					break;
				messageBuffer.append(Arrays.copyOfRange(buffer, 0, count));
				while (messageBuffer.hasCompleteMessage()) {
					String ircMessage = messageBuffer.getNextMessage();

                    System.out.println("\"" + ircMessage + "\"");
					processMessage(ircMessage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
 	}

	public static void main(String[] args) {
		IRCConf ircConf = new IRCConf("irc.wechall.net", 6668, "#shadowlamb");
		ShadowlambConf shadowBotConf = new ShadowlambConf("ShadowBot", "12345");
		ShadowlambClient client = new ShadowlambClient(ircConf, shadowBotConf);
		try {
			client.setNick(":" + shadowBotConf.getUsername());
			client.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
