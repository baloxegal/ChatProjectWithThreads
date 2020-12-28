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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChatApplicationClient {
	
	private static final InetAddress REMOTE_HOST = InetAddress.getLoopbackAddress();
	private static final Integer REMOTE_PORT = 7777;
	private static Map <Connection.ProxyS, User> users = new HashMap<Connection.ProxyS, User>();

	public static void main(String[] args) throws IOException, ClassNotFoundException {	
		
		Connection conn = new Connection();
		Action actionUsers = null;
		ObjectInputStream oisUsers = new ObjectInputStream(conn.getSocket().getInputStream());
		ObjectOutputStream ousUsers = new ObjectOutputStream(conn.getSocket().getOutputStream());
		
		conn.getAuthorizedConnection(conn, actionUsers, REMOTE_HOST, REMOTE_PORT, oisUsers, ousUsers);
		
		actionUsers.setAction(Operation.USER_LIST, null);		
		Executor executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
		executor.execute(new Runnable(){
			@Override
			public void run() {
				do {
					try {
						conn.getUserList(actionUsers, users, oisUsers, ousUsers);
						Thread.sleep(100);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}					
				}while(true);
			}
		});
		
		Action actionGetMessage;
		ObjectInputStream oisMes = new ObjectInputStream(conn.getSocket().getInputStream());
		executor.execute(new Runnable(){
			@Override
			public void run() {
				do {
					conn.getMessage(actionGetMessage, users, oisMes);
				}while(true);
			}
		});	
		
		Action actionSendMessage;
		ObjectOutputStream ousMes = new ObjectOutputStream(conn.getSocket().getOutputStream());
		executor.execute(new Runnable(){
			@Override
			public void run() {
				do {
					conn.sendMessage(actionMes, users, dout);
				}while(true);
			}
		});	
		
	}
}
