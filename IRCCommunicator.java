import java.net.*;
import java.io.*;
import java.util.*;

public abstract class IRCCommunicator implements Runnable {
	
	Socket conn;
	OutputStream output;
	String channel;
	IRCConf conf;
	boolean startingUp = true;

	IRCCommunicator(IRCConf conf) {
		this.conf = conf;
		channel = conf.getChannel();
		try {
			conn = new Socket(conf.getHost(), conf.getPort());
			output = conn.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void sendCommand(String command, ArrayList<String> options) {
		String msg = command;
		for(String option : options) {
			msg += " " + option;
		}
		System.out.println("[Query]: " + msg);

		byte[] bytes = (msg + "\r\n").getBytes();

		try {
			output.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void initChannel() {
		ArrayList<String> options = new ArrayList<>();
		options.add(channel);
		sendCommand("JOIN", options);
	}

	protected void joinChannel(String channel) {
		ArrayList<String> options = new ArrayList<>();
		options.add(channel);
		sendCommand("JOIN", options);
	}

	protected void leaveChannel(String channel) {
		ArrayList<String> options = new ArrayList<>();
		options.add(channel);
		sendCommand("PART", options);
	}

	protected void leaveServer(String reason) {
		ArrayList<String> options = new ArrayList<>();
		options.add(":Quit:");
		options.add(reason);
		sendCommand("QUIT", options);
	}

	protected void setUser(String username, String hostname, String servername, String realname) {
		ArrayList<String> options = new ArrayList<>();
		options.add(username);
		options.add(hostname);
		options.add(servername);
		options.add(realname);
		sendCommand("USER", options);
	}

	protected void setNick(String nickname) {
		ArrayList<String> options = new ArrayList<>();
		options.add(nickname);
		sendCommand("NICK", options);
	}

	protected void pong(String server) {
		ArrayList<String> options = new ArrayList<>();
		options.add(server);
		sendCommand("PONG", options);
	}

	protected void privmsg(String to, String msg) {
		ArrayList<String> options = new ArrayList<>();
		options.add(to);
		options.add(":");
		options.add(msg);
		sendCommand("PRIVMSG", options);
	}

	public IRCConf getConf() {
		return conf;
	}

	public abstract void processMessage(String ircMessage);

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
}

