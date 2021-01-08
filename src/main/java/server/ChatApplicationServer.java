package server;

import java.net.Socket;
import lib.Connection;
import lib.Connection.ProxyS;
import lib.Message;
import lib.Action;
import lib.Operation;
import lib.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChatApplicationServer {
	
	public static final Integer LOCAL_PORT = 7777;
	public static Map <User, Socket> users = new HashMap<User, Socket>();
	public static List <User> usersList = new ArrayList<User>();
	public static List <Message> messages = new ArrayList<Message>();
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {		
				
		Executor executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());	
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(true) {
					new Thread(new Runnable(){
						@Override
						public void run() {
							Connection connNewUsers = null;
							Action action = null;
							try {
								connNewUsers = new Connection(LOCAL_PORT);
							} catch (IOException e) {
								e.printStackTrace();
							}				
							try {
								action = (Action) connNewUsers.fetch(new ObjectInputStream(connNewUsers.getSocket().getInputStream()));
							} catch (ClassNotFoundException | IOException e) {
								e.printStackTrace();
							}
							if(action.getOperation().equals(Operation.SIGN_IN)) {
								User user = (User) action.getTarget();
								if(users.putIfAbsent(user, connNewUsers.getSocket()) == null) {							
									usersList.add(user);
									action.setAction(Operation.SUCCESS, null);
								}
								else
									action.setAction(Operation.UNSUCCESS, null);
								try {
									connNewUsers.send(action, new ObjectOutputStream(connNewUsers.getSocket().getOutputStream()));
								} catch (IOException e) {
									e.printStackTrace();
								}											
							}
						}
					});	
				}				
			}
		});
		executor.execute(new Runnable(){
			@Override
			public void run() {
				Connection connFetchMessage = null;
				ObjectInputStream ois = null;
				Message message;
				Action action;
				while(true) {
					if(!users.isEmpty()) {
						for(var s : users.values()) {
							connFetchMessage = new Connection(s);
							try {
								ois = new ObjectInputStream(s.getInputStream());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							try {
								if(ois.available() > 0) {
									try {
										action = (Action) connFetchMessage.fetch(ois);
										if(action.getOperation().equals(Operation.SEND_MSG)) {
											message = (Message) action.getTarget();
											messages.add(message);
										}									
									} catch (ClassNotFoundException e) {
										e.printStackTrace();
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}				
			}				
		});
		
		executor.execute(new Runnable(){
			@Override
			public void run() {
				Connection connSendMessage = null;
				ObjectOutputStream oos = null;
				Action action = null;
				while(true) {
					if(!messages.isEmpty()) {
						for(var m : messages) {
							connSendMessage = new Connection(users.get(m.getToUser()));
							try {
								oos = new ObjectOutputStream(connSendMessage.getSocket().getOutputStream());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							try {
								action = new Action(Operation.SEND_MSG, m);
								connSendMessage.send(action, oos);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}				
			}				
		});
		
		executor.execute(new Runnable(){
			@Override
			public void run() {
				Connection connSendUsersList = null;
				ObjectOutputStream oos = null;
				Action action = null;
				while(true) {
					if(!users.isEmpty()) {
						for(var u : users.entrySet()) {
							connSendUsersList = new Connection(u.getValue());
							try {
								oos = new ObjectOutputStream(u.getValue().getOutputStream());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							try {
								action = new Action(Operation.USER_LIST, usersList);
								connSendUsersList.send(action, oos);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}				
			}				
		});
	}
}
