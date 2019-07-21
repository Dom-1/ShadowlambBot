import java.io.*;
import java.util.*;

public class ShadowlambClient extends IRCCommunicator {

	private ShadowlambConf clientConf;
	private boolean startingUp = true;
	private boolean playingGame = false;
	
	public ShadowlambClient(IRCConf conf, ShadowlambConf clientConf) {
		super(conf);
		this.clientConf = clientConf;
	}

	public void sendGameCommand(String command, ArrayList<String> options) {

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
				break;
			default:
				break;
		}
	}

	public void playGame(String ircMessage) {
		IRCParserOutput ircParsed = IRCParser.parse(ircMessage);
		ShadowlambGameParser parser = new ShadowlambGameParser(ircParsed);
		parser.run();

		if(ircParsed.getCommand().equals("ping")) {
			pong(ircParsed.getTarget());
		}

		switch(parser.getAction()) {
			case "init":
				privmsg(ircConf.getChannel(), "#start " + clientConf.getRace() + " " + clientConf.getGender());
				break;
			case "start":
				privmsg(ircConf.getChannel(), "#enable bot");
				privmsg(ircConf.getChannel(), "#exp");
			case "status":
				privmsg(ircParsed.getChannel(), "#status");
				break;
			case "startGoing":
				travelDlgMnger.setMsg(ircParsed.getContent());
				break;
			default:
				break;
		}
	}

	public static void switchWaiting() {
		waiting = !waiting;
	}

	@Override
	protected void joinChannel(String channel) {
		if(channel.equals("#shadowlamb")) {
			startingUp = false;
			playingGame = true;
		}
		ArrayList<String> options = new ArrayList<>();
		options.add(channel);
		sendCommand("JOIN", options);
	}

	@Override 
	public void run() {
		InputStream stream = null;

		try {
			stream = conn.getInputStream();
			MessageBuffer messageBuffer = new MessageBuffer();
			byte[] buffer = new byte[512];
			int count;
			
			while (startingUp) {
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

			while(playingGame) {
				count = stream.read(buffer);
				if (count == -1)
					break;
				messageBuffer.append(Arrays.copyOfRange(buffer, 0, count));
				while (messageBuffer.hasCompleteMessage()) {
					String ircMessage = messageBuffer.getNextMessage();

                    System.out.println("\"" + ircMessage + "\"");
					playGame(ircMessage);
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
