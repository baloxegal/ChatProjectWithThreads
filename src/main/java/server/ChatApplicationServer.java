package server;

import java.net.Socket;
import lib.Connection;
import lib.Message;
import lib.Action;
import lib.Operation;
import lib.User;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChatApplicationServer {
	
	public static final Integer LOCAL_PORT = 7777;
	public static Map <Socket, User> users = new HashMap<Socket, User>();
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {		
				
		Executor executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
		
		executor.execute(new Runnable(){
			@Override
			public void run() {
				Connection connUsers = new Connection();
				ObjectInputStream ois = new ObjectInputStream(conn.getSocket().getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(conn.getSocket().getOutputStream());
				Action action = new Action(null, null);
				do {
					if(!users.isEmpty())
						for(var u : users.keySet()) {
							
						}
							
					
				}while(true);
			}
		});
		
		while(true){
			Connection conn = new Connection(LOCAL_PORT);
			ObjectInputStream ois = new ObjectInputStream(conn.getSocket().getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(conn.getSocket().getOutputStream());
			Action action = new Action(null, null);
			executor.execute(new Runnable(){
				@Override
				public void run() {
										
					executor.execute(new Runnable(){
						@Override
						public void run() {
							do {
									
									
									
							}while(true);
						}
					});
						
					executor.execute(new Runnable(){
						@Override
						public void run() {
							do {
							
								
								
							}while(true);
						}
					});						
				}				
			});				
		}
	}
}
