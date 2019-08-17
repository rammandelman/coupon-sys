package DataBases;

import controler.userType;

public class Users {
	
	public String user_name;
	public String password;
	public userType type;
	
	public Users(String user_name, String password, userType type) {
		this.user_name = user_name;
		this.password = password;
		this.type = type;
	}

	
}
