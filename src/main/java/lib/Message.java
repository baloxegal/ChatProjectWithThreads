package lib;

import java.io.Serializable;

public class Message implements Serializable {
	
	private static final long serialVersionUID = -4727747511054555717L;
	private String body;
	private User fromUser;
	private User toUser;

	public Message(String body, User from, User to) {
		this.body = body;
		this.fromUser = from;
		this.toUser = to;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getToUser() {
		return toUser;
	}

	public void setTo(User toUser) {
		this.toUser = toUser;
	}

	@Override
	public String toString() {
		return "Message [text=" + body + ", fromUser=" + fromUser + ", toUser=" + toUser + "]";
	}
	
}
