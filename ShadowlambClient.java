import java.io.*;
import java.util.*;

public class ShadowlambClient extends IRCCommunicator {

	private IRCConf ircConf;
	private ShadowlambConf clientConf;
	private boolean startingUp = true;
	private boolean playingGame = false;
	private boolean waiting = false;
	private ShadowlambTravelDialogManager travelDlgMnger = new ShadowlambTravelDialogManager();
	private ShadowlambBattleDialog battleDlgMnger = new ShadowlambBattleDialog();
	private ShadowlambAttributeManager attrDlgMnger = new ShadowlambAttributeManager();
	private ShadowlambKarmaManager karmaMnger = new ShadowlambKarmaManager();
	private ShadowlambLocationManager locationMnger = new ShadowlambLocationManager();
	private Timer timer = new Timer();
	
	public ShadowlambClient(IRCConf conf, ShadowlambConf clientConf) {
		super(conf);
		this.clientConf = clientConf;
		ircConf = conf;
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
					privmsg("NickServ", "identify " + clientConf.getPassword());
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
		if(!waiting){
		switch(parser.getAction()) {
			case "init":
				privmsg(ircConf.getChannel(), "#start " + clientConf.getRace() + " " + clientConf.getGender());
				break;
			case "explore":
				privmsg(ircConf.getChannel(), "#exp");
				break;
			case "checkHP":
				String msg = ircParsed.getContent();
				double hp = Double.parseDouble(msg.substring(msg.indexOf(':'), msg.indexOf('/')));
				if(hp < 15) {
					switchWaiting();
					gotoHotel(ircConf.getChannel());
				}
				break;
			case "atHotel":
				switchWaiting();
				sleep();
				break;
			case "status":
				privmsg(ircConf.getChannel(), "#status");
				break;
			case "startGoing":
				travelDlgMnger.setMsg(ircParsed.getContent());
				break;
			case "repeatLoop":
				
				break;
			default:
				break;
			}
		}
	}

	public void switchWaiting() {
		waiting = !waiting;
	}

	private void gotoHotel(String to) {		
		privmsg(to, "#g 1");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				switchWaiting();
			}
		}, 1000*60*5);
	}

	private void sleep() {
		privmsg(ircConf.getChannel(), "#sleep");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				switchWaiting();
			}
		}, 1000*60*3);
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
		ShadowlambConf shadowBotConf = new ShadowlambConf("f0rk", "12345", "halftroll", "male");
		ShadowlambClient client = new ShadowlambClient(ircConf, shadowBotConf);
		try {
			client.setNick(":" + shadowBotConf.getUsername());
			client.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
