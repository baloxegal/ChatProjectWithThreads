package lib;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import lib.Connection.ProxyS;

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
	
	public Connection(InetAddress localhost, Integer port) throws UnknownHostException, IOException {
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
	
	public void getAuthorizedConnection(Connection conn, Action actionUser, InetAddress remoteHost, Integer remotePort, ObjectInputStream din, ObjectOutputStream dout) throws UnknownHostException, IOException, ClassNotFoundException {		
		do {
			System.out.print("Enter user name: ");
			Scanner in = new Scanner(System.in);		
			conn = new Connection(remoteHost, remotePort);
			actionUser.setAction(Operation.SIGN_IN, new User(in.nextLine()));
			in.close();	
			this.send(actionUser, dout);
			actionUser = (Action)conn.fetch(din);		
			if(actionUser.getOperation().equals(Operation.SUCCESS))
				break;
			System.out.println("Your try to sign in is failed! Try again!");
		}while(true);
	}
	
	@SuppressWarnings("unchecked")
	public void getUserList(Action actionUser, Map<ProxyS, User> users, ObjectInputStream din, ObjectOutputStream dout) throws IOException, ClassNotFoundException {
		this.send(actionUser, dout);
		actionUser = (Action)this.fetch(din);
		if(actionUser.getOperation().equals(Operation.SUCCESS))
			users = (Map<ProxyS, User>)actionUser.getTarget();
	}	
	
	public void getMessage(Action actionMes, Map<ProxyS, User> users, ObjectInputStream din) throws IOException, ClassNotFoundException {
		actionMes = (Action)this.fetch(din);		
		System.out.println(actionMes.getTarget());		
	}	
	
	public void sendMessage(Action actionMes, Map<ProxyS, User> users, ObjectOutputStream dout) throws IOException, ClassNotFoundException {
		System.out.print("Who is receiver: ");
		Scanner in = new Scanner(System.in);
		String user = in.nextLine();
		if(users.containsValue(user)) {
			System.out.print("Enter your message: ");
			actionMes.setAction(Operation.SEND_MSG, user);
			in.close();	
			this.send(actionUser, dout);
		}	
		else
			System.out.print("This receiver is offline");		
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
