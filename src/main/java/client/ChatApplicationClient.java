package client;

import lib.Connection;
import lib.Message;
import lib.Action;
import lib.Operation;
import lib.User;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class ChatApplicationClient {
	
	private static final InetAddress REMOTE_HOST = InetAddress.getLoopbackAddress();
	private static final int REMOTE_PORT = 7777;
	private static List<User> users = new ArrayList<>();
	public static User user;

	public static void main(String[] args) throws IOException, ClassNotFoundException {		
		
		Connection conn = Connection.getAuthorizedConnection(REMOTE_HOST, REMOTE_PORT, user);
		System.out.println("Super1");
		Executor executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
		executor.execute(new Runnable(){
			Action action;
			ObjectInputStream ois = new ObjectInputStream(conn.getSocket().getInputStream());			
			@Override
			public void run() {				
				while(true) {
					try {
						conn.getFromServer(action, users, ois);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		executor.execute(new Runnable(){
			Message message = new Message(null, user, null);
			Action action = new Action(Operation.SEND_MSG, message);
			Scanner in = new Scanner(System.in);
			ObjectOutputStream oos = new ObjectOutputStream(conn.getSocket().getOutputStream());
			@Override
			public void run() {
				while(true) {
					try {
						conn.sendToServer(message, action, users, in, oos);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});		
	}
}
