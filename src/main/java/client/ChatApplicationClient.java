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
import java.util.concurrent.ExecutorService;
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
	
		System.out.println("Super10");
//		Executor executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
		Executor executor = Executors.newCachedThreadPool();
		System.out.println("Super100");
		Action action = null;
		System.out.println("Super1000");
		System.out.println(conn.getSocket());
		System.out.println(conn.toString());
//		ObjectInputStream ois = new ObjectInputStream(conn.getSocket().getInputStream());
		System.out.println(conn.getSocket().isInputShutdown());
		System.out.println(conn.getSocket().isClosed());
		System.out.println(conn.getSocket().isConnected());
		System.out.println();
//		System.out.println(ois.toString());
		System.out.println(conn.getSocket());
		System.out.println(conn.toString());
		System.out.println("Super10000");
		executor.execute(new Runnable(){
				
			@Override
			public void run() {				
				while(true) {
					try {
						System.out.println("Super100");
						conn.getFromServer(action, users, new ObjectInputStream(conn.getSocket().getInputStream()));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		Message message = new Message(null, user, null);
		Action actionA = new Action(Operation.SEND_MSG, message);
		
		Scanner nin = new Scanner(System.in);
		nin.reset();
		ObjectOutputStream oos = new ObjectOutputStream(conn.getSocket().getOutputStream());
		System.out.println("Super1000000");
		executor.execute(new Runnable(){
			
			@Override
			public void run() {
				while(true) {
					try {
						conn.sendToServer(message, actionA, users, nin, oos);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});		
	}
}
