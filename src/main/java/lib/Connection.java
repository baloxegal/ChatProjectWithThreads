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

public class Connection implements Serializable, Runnable {
	
	private static final long serialVersionUID = -8026900816337050671L;
	private transient Socket socket;
		
	private Connection(Socket socket) {
		this.socket = socket;		
	}
	
	@SuppressWarnings("resource")
	private Connection(Integer port) throws IOException {
		this(new ServerSocket(port).accept());
	}
	
	private Connection(InetAddress localhost, Integer port) throws UnknownHostException, IOException {
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
	
	public void send(Object object) throws IOException {
		
		ObjectOutputStream dout = new ObjectOutputStream(socket.getOutputStream());
		
		dout.writeObject(object);		
	}
	
	public Object fetch() throws IOException, ClassNotFoundException {
		
		ObjectInputStream din = new ObjectInputStream(socket.getInputStream());
	
		return din.readObject();
	}
	
	public void getAuthorizedConnection(Connection conn, Action action, InetAddress remoteHost, Integer remotePort) throws UnknownHostException, IOException, ClassNotFoundException {		
		do {
			System.out.print("Enter user name: ");
			Scanner in = new Scanner(System.in);		
			conn = new Connection(remoteHost, remotePort);
			action.setAction(Operation.SIGN_IN, new User(in.nextLine()));
			in.close();	
			conn.send(action);
			action = (Action)conn.fetch();		
			if(action.getOperation().equals(Operation.SUCCESS))
				break;
			System.out.println("Your try to sign in is failed! Try again!");
		}while(true);
	}
	
	@SuppressWarnings("unchecked")
	public void getUserList(Connection conn, Action action, Map<ProxyS, User> users) throws IOException, ClassNotFoundException {
		action.setAction(Operation.USER_LIST, null);
		conn.send(action);
		action = (Action)conn.fetch();
		if(action.getOperation().equals(Operation.SUCCESS))
			users = (Map<ProxyS, User>)action.getTarget();
	}

	@Override
	public String toString() {
		return "Connection [socket=" + socket + "]";
	}
	
	@Override
	public void run() {	
		
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
