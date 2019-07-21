public class IRCConf {
	private String host;
	private int port;
	private String channel;

	public IRCConf(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public IRCConf(String host, int port, String channel) {
		this.host = host;
		this.port = port;
		this.channel = channel;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getChannel() {
		return channel;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
}
