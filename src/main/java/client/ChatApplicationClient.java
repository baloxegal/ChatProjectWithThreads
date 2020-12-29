package client;

import lib.Connection;
import lib.Message;
import lib.Action;
import lib.Operation;
import lib.User;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class ChatApplicationClient {
	
	private static final InetAddress REMOTE_HOST = InetAddress.getLoopbackAddress();
	private static final Integer REMOTE_PORT = 7777;
	public static User user;

	public static void main(String[] args) throws IOException, ClassNotFoundException {	
		
		Connection conn = new Connection(REMOTE_HOST, REMOTE_PORT);
		Action action = new Action(Operation.SIGN_IN, null);
		ObjectInputStream ois = new ObjectInputStream(conn.getSocket().getInputStream());
		ObjectOutputStream oos = new ObjectOutputStream(conn.getSocket().getOutputStream());
		Map <Connection.ProxyS, User> users = new HashMap<Connection.ProxyS, User>();
		
		conn.getAuthorizedConnection(user, action, ois, oos);
			
		Executor executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
		executor.execute(new Runnable(){
			@Override
			public void run() {
				do {
					try {
						conn.getFromServer(action, users, ois);
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
		
		Message message = new Message(null, user, null);
		Action actionMessage = new Action(Operation.SEND_MSG, message);
		executor.execute(new Runnable(){
			@Override
			public void run() {
				do {
					try {
						conn.sendToServer(actionMessage, message, users, oos);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}while(true);
			}
		});		
	}
}
