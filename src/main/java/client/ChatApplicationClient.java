package client;

import lib.Connection;
import lib.Connection.ProxyS;
import lib.Action;
import lib.Operation;
import lib.User;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.io.IOException;

public class ChatApplicationClient {
	
	private static final InetAddress REMOTE_HOST = InetAddress.getLoopbackAddress();
	private static final Integer REMOTE_PORT = 7777;
	private static Connection conn;
	private static Action action;
	private static Map <Connection.ProxyS, User> users = new HashMap<Connection.ProxyS, User>();

	public static void main(String[] args) throws IOException, ClassNotFoundException {		
			
		conn.getAuthorizedConnection(conn, action, REMOTE_HOST, REMOTE_PORT);
		
		conn.getUserList(conn, action, users);
		
		Executor executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
		executor.execute(conn);
		
	}
}
