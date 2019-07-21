public class ShadowlambConf {
	private String username;
	private String password;
	private String race;
	private String gender;

	public ShadowlambConf(String username, String password, String race, String gender) {
		this.username = username;
		this.password = password;
		this.race = race;
		this.gender = gender;
	}

	public String getUsername() {
	    return username;
	}
	 
	public void setUsername(String username) {
	    this.username = username;
	}

	public String getPassword() {
	    return password;
	}
	 
	public void setPassword(String password) {
	    this.password = password;
	}

	public String getRace() {
	    return race;
	}
	 
	public void setRace(String race) {
	    this.race = race;
	}

	public String getGender() {
	    return gender;
	}
	 
	public void setGender(String gender) {
	    this.gender = gender;
	}
}