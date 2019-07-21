import java.util.*;

public class ShadowlambTravelDialogManager {
	
	private String msg;
	private int eta; // in milliseconds

	public ShadowlambTravelDialogManager() {}

	public ShadowlambTravelDialogManager(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
	    return msg;
	}
	 
	public void setMsg(String msg) {
	    this.msg = msg;
	}
}