package lib;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = -2528527689894598775L;
	private String name;

	public User(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [name = " + name + "]";
	}	
}
