package lib;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Scanner;
import java.io.IOException;

public class Connection implements Serializable {
	
	private static final long serialVersionUID = -8026900816337050671L;
	private transient Socket socket;
		
	public Connection(Socket socket) {
		this.socket = socket;		
	}
	
	@SuppressWarnings("resource")
	public Connection(Integer port) throws IOException {
		this(new ServerSocket(port).accept());
	}
	
	public Connection(InetAddress localhost, Integer port) throws IOException{
		this(new Socket(localhost, port));
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public ProxyS getProxyS() {
		return new ProxyS();			
	}
	
	public void send(Object object, ObjectOutputStream dout) throws IOException {
		
		dout.writeObject(object);		
	}
	
	public Object fetch(ObjectInputStream din) throws IOException, ClassNotFoundException {
		
		return din.readObject();
	}
	
	public void getAuthorizedConnection(User user, Action action, ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {		
		do {
			System.out.print("Enter user name: ");
			Scanner in = new Scanner(System.in);
			user = new User(in.nextLine());
			action.setTarget(user);
			in.close();	
			this.send(action, oos);
			action = (Action)this.fetch(ois);		
			if(action.getOperation().equals(Operation.SUCCESS))
				break;
			System.out.println("Your try to sign in is failed! Try again!");
		}while(true);
	}
		
	@SuppressWarnings("unchecked")
	public void getFromServer(Action action, Map<ProxyS, User> users, ObjectInputStream ois) throws IOException, ClassNotFoundException {
		action = (Action)this.fetch(ois);		
		if(action.getOperation().equals(Operation.USER_LIST))
			users = (Map<ProxyS, User>) action.getTarget();
		else if(action.getOperation().equals(Operation.SEND_MSG))
			System.out.println(action.getTarget());
	}	
	
	public void sendToServer(Action action, Message message, Map<ProxyS, User> users, ObjectOutputStream oos) throws IOException{
		System.out.print("Who is receiver: ");
		Scanner in = new Scanner(System.in);
		User user = new User (in.nextLine());
		if(users.containsValue(user)) {
			System.out.print("Enter your message: ");
			message.setBody(in.nextLine());
			in.close();			
			message.setToUser(user);
			this.send(action, oos);						
		}	
		else
			System.out.print("This receiver is offline!!!");		
	}	

	@Override
	public String toString() {
		return "Connection [socket=" + socket + "]";
	}
	
	public class ProxyS implements Serializable {

		private static final long serialVersionUID = -8000241752157437968L;
		
		private InetAddress localAddress;
		private InetAddress remoteAddress;
		private Integer localPort;
		private Integer remotePort;
				
		private ProxyS() {
			
			this.localAddress = socket.getLocalAddress();
			this.remoteAddress = socket.getInetAddress();
			this.localPort = socket.getLocalPort();
			this.remotePort = socket.getPort();
		}

		public InetAddress getLocalAddress() {
			return localAddress;
		}
		
		public InetAddress getRemoteAddress() {
			return remoteAddress;
		}

		public Integer getLocalPort() {
			return localPort;
		}

		public Integer getRemotePort() {
			return remotePort;
		}

		@Override
		public String toString() {
			return "ProxyS [localAddress=" + localAddress + ", remoteAddress=" + remoteAddress + ", localPort="
					+ localPort + ", remotePort=" + remotePort + "]";
		}	
		
	}
		
}
