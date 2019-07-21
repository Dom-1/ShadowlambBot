public class IRCParserOutput {
	private String origin;
	private String nickname;
	private String command;
	private String target;
	private String content;
	private String noticeType;

	public IRCParserOutput() {}
	
	public String getNoticeType() {
	    return noticeType;
	}
	 
	public void setNoticeType(String noticeType) {
	    this.noticeType = noticeType;
	}

	public String getOrigin() {
	    return origin;
	}
	 
	public void setOrigin(String origin) {
	    this.origin = origin;
	}

	public String getNickname() {
	    return nickname;
	}
	 
	public void setNickname(String nickname) {
	    this.nickname = nickname;
	}
	 
	public String getCommand() {
	    return command;
	}
	 
	public void setCommand(String command) {
	    this.command = command;
	}

	public String getTarget() {
	    return target;
	}
	 
	public void setTarget(String target) {
	    this.target = target;
	}

	public String getContent() {
	    return content;
	}
	 
	public void setContent(String content) {
	    this.content = content;
	}
}